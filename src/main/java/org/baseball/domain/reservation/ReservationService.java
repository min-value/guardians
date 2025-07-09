package org.baseball.domain.reservation;

import lombok.extern.slf4j.Slf4j;
import org.baseball.domain.redis.QueueService;
import org.baseball.domain.redis.RedisService;
import org.baseball.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class ReservationService {
    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private QueueService queueService;

    /* 게임 정보(상대팀) 반환 */
    @Transactional
    public ReserveGameInfoDTO getGameInfo(int gamePk) {
        return reservationMapper.getGameInfo(gamePk);
    }

    /* 팔린 좌석 목록 반환 */
    @Transactional
    public List<String> getSoldSeats(SoldSeatsReqDTO dto) {
        return reservationMapper.getSoldSeats(dto);
    }

    /* 구역 정보 반환 */
    @Transactional
    public List<ZoneDTO> getZones(int gamePk) {
        List<ZoneDTO> result = reservationMapper.getZones();

        //팔린 좌석들 가져오기
        for(ZoneDTO zoneDTO :result){
            List<String> seats = getSoldSeats(new SoldSeatsReqDTO(gamePk, zoneDTO.getZonePk()));
            zoneDTO.setRemainingNum(zoneDTO.getTotalNum() - seats.size());
        }

        return result;
    }

    /* 구역 상세 정보 반환 */
    @Transactional
    public ZoneDTO getZoneInfo(int zonePk) {
        return reservationMapper.getZoneInfo(zonePk);
    }


    /* 구역 전체 정보 가져오기 */
    @Transactional
    public void getSeatsInfo(int gamePk, Map<String, Object> result) {
        List<ZoneDTO> zoneDTOList = getZones(gamePk);

        //구역 별 남은 좌석 수 저장
        Map<Integer, ZoneDTO> zoneInfo = new LinkedHashMap<>();
        Map<Integer, List<String>> zoneMapDetail = new LinkedHashMap<>();

        //팔린 좌석들 가져오기
        for(ZoneDTO zoneDTO : zoneDTOList){
            List<String> seats = getSoldSeats(new SoldSeatsReqDTO(gamePk, zoneDTO.getZonePk())); //구역 별로 팔린 좌석 목록 불러오기

            zoneDTO.setRemainingNum(zoneDTO.getTotalNum() - seats.size()); //남은 좌석 수 계산

            zoneInfo.put(zoneDTO.getZonePk(), zoneDTO); //zoneMap 세팅
            zoneMapDetail.put(zoneDTO.getZonePk(), seats); //zoneMapDetail 세팅
        }

        //구역 당 남은 좌석 수 저장
        result.put("zoneInfo", zoneInfo);

        //구역 당 상세 정보 세션에 저장
        result.put("zoneMapDetail", zoneMapDetail);
    }

    /* 할인 정보 로드 */
    @Transactional
    public List<DiscountDTO> getDiscountInfo(){
        return reservationMapper.getDiscountInfo();
    }

    /* 해당 gamePk가 우리 구장 경기인지 확인 */
    @Transactional
    public boolean isOurGame(int gamePk) { return reservationMapper.isOurGame(gamePk); }

    /* seat 페이지 로드 시 필요 로직 */
    @Transactional
    public SeatLoadResDTO seatLoad(int gamePk, int check, List<String> seats, Integer zonePk, UserDTO user, int reservelistPk, HttpSession session) {
        try {
            List<HeldSeatDTO> reservePkList = reservationMapper.selectHeldAll(gamePk, user.getUserPk());

            if(!reservePkList.isEmpty()){
                //seats에 있는 정보 제외하고 모두 삭제
                for(HeldSeatDTO row : reservePkList){
                    int pk = row.getReservelistPk();
                    int zone = row.getZonePk();
                    String seat = row.getSeatNum();

                    //지금 선점 정보가 localStorage에 남아있는 정보면 건너뛰기
                    if(pk == reservelistPk) continue;

                    // DB 선점 정보 삭제
                    deletePreemptionInfo(pk);

                    // Redis 선점 해제
                    boolean redisDeleted = true;
                    List<String> seatL = Collections.singletonList(seat);

                    if(zone == 1100 || zone == 1101) {
                        redisDeleted = redisService.cancelBleachers(gamePk, user.getUserPk(), zone);
                    } else {

                        redisDeleted = redisService.cancelPreempt(gamePk, seatL, user.getUserPk(), zone);
                    }
                }
            }

            SeatLoadResDTO result = new SeatLoadResDTO();
            result.setError(false);
            result.setResult(new HashMap<>());

            //우리팀 경기인지 확인
            if (!isOurGame(gamePk)) {
                result.setError(true);
                result.setErrorMsg("존재하지 않는 경기입니다.");
                return result;
            }

            //해당 유저가 해당 게임에서 구매한 좌석이 4개 이하인지 확인
            int cnt = countUserReserve(gamePk, user.getUserPk());

            //남은 좌석 수 저장 - available(세션)
            if (cnt >= 4) {
                result.setError(true);
                result.setErrorMsg("최대 4장까지만 구매가 가능합니다");
                return result;
            }
            int remainingCnt = 4 - cnt;
            session.setAttribute("available", remainingCnt); //세션에 저장
            result.getResult().put("available", remainingCnt);

            //경기 정보 저장 (gameInfo) - gameInfo(세션), gamePk
            ReserveGameInfoDTO reserveGameInfoDTO = getGameInfo(gamePk);
            result.getResult().put("gameInfo", reserveGameInfoDTO);
            session.setAttribute("gameInfo", reserveGameInfoDTO); //세션에 저장(JSON으로 파싱)

            //좌석 정보 저장 - zoneInfo(구역 별 상세 정보), zoneMapDetail(구역별 팔린 좌석 목록)
            getSeatsInfo(gamePk, result.getResult());

            //할인 정보 저장 - discountInfo
            List<DiscountDTO> discountDTOList = getDiscountInfo();
            result.getResult().put("discountInfo", discountDTOList);

            //대기열 키가 존재하지 않으면 오류
            if(!queueService.checkTTL(gamePk, user.getUserPk())) {
                result.setCheck(4); //4: 대기열 키가 존재하지 않는 상태
                return result;
            }

            //선점 여부에 따라 렌더링 화면 변경
            if(check == 1) {
                result.setCheck(1);
            } else  if(check == 2 || check == 3) {
                //선점 확인
                PreemptConfirmReqDTO dto = new PreemptConfirmReqDTO();
                dto.setGamePk(gamePk);
                dto.setSeats(seats);
                dto.setZonePk(zonePk);

                int confirmResult = confirmPreempt(dto, user);

                if(confirmResult == 1) {
                    //선점 되어있는 경우
                    if(check == 2) result.setCheck(2);
                    else result.setCheck(3);
                } else {
                    //선점이 되어 있지 않은 경우
                    result.setCheck(1);
                }
            }

            return result;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    /* 일반 구역 선점 */
    @Transactional
    public PreemptionResDTO getPreempt(PreemptionDTO preemptionDTO, UserDTO userDTO, PreemptionResDTO result, String reserveCode) {
        int zonePk = preemptionDTO.getZonePk();
        int gamePk =  preemptionDTO.getGamePk();


        //Redis상 선점/판매 여부 확인
        for(String seatNum : preemptionDTO.getSeats()) {
            int check = reservationMapper.confirmPreemption(zonePk, seatNum, gamePk);

            if(check >= 1) {
                result.setPreempted(0);
                result.setErrorMsg("이미 선점된 좌석입니다.");
                return result;
            }
        }
        result.setPreempted(1);

        //선점/판매가 되지 않았다면 DB 상 선점
        PreemptionListDTO preemptionListDTO = PreemptionListDTO
                .builder()
                .quantity(preemptionDTO.getQuantity())
                .userPk(userDTO.getUserPk())
                .gamePk(preemptionDTO.getGamePk())
                .reserveCode(reserveCode)
                .build();

        reservationMapper.setPreemptionList(preemptionListDTO);
        int reservelistPk = preemptionListDTO.getReservelistPk();
        result.setReservelistPk(reservelistPk);

        for(String seatNum : preemptionDTO.getSeats()) {
            PreemptionReserveDTO preemptionReserveDTO = PreemptionReserveDTO
                    .builder()
                    .reservelistPk(reservelistPk)
                    .zonePk(preemptionDTO.getZonePk())
                    .seatNum(seatNum)
                    .build();
            reservationMapper.setPreemptionReserve(preemptionReserveDTO);
        }

        //Redis 상 선점
        if(!redisService.preemptSeat(gamePk, preemptionDTO.getSeats(), userDTO.getUserPk(), zonePk)) {
            //redis 선점 실패 시 DB 롤백
            deletePreemptionInfo(reservelistPk);

            result.setPreempted(0);
            result.setErrorMsg("이미 선점된 좌석입니다.");
            return result;
        }

        return result;
    }

    /* 외야석 선점 */
    @Transactional
    public PreemptionResDTO getBleachers(PreemptionDTO preemptionDTO, UserDTO userDTO, PreemptionResDTO result, String reserveCode) {
        int zonePk = preemptionDTO.getZonePk();
        int gamePk =  preemptionDTO.getGamePk();
        int quantity = preemptionDTO.getQuantity();
        List<String> seats =  preemptionDTO.getSeats();
        int userPk = userDTO.getUserPk();

        try {
            //락 잡기
            if (!redisService.tryLockSeat(gamePk, null, zonePk)) {
                result.setPreempted(0);
                result.setErrorMsg("이미 선점된 좌석입니다.");
                return result;
            }

            //이전 기록이 있는지 확인
            if(redisService.confirmPreempt(gamePk, null, userPk, zonePk)) {
                result.setPreempted(0);
                result.setErrorMsg("이미 선점된 좌석입니다.");
                return result;
            }

            //DB 탐색해 남은 좌석 개수 판단
            SoldSeatsReqDTO soldSeatsReqDTO = new SoldSeatsReqDTO(gamePk, zonePk);

            List<String> soldSeats = getSoldSeats(soldSeatsReqDTO); //남은 좌석 정보

            ZoneDTO zoneDTO = getZoneInfo(preemptionDTO.getZonePk()); //구역 정보

            int remainingSeats = zoneDTO.getTotalNum() - soldSeats.size(); //남은 좌석 수

            if (remainingSeats < quantity) {
                //좌석 수가 충분하지 않다면
                result.setPreempted(2);
                result.setErrorMsg("좌석 수가 충분하지 않습니다.");
                return result;
            }

            result.setPreempted(1);

            //DB상 선점
            PreemptionListDTO preemptionListDTO = PreemptionListDTO
                    .builder()
                    .quantity(quantity)
                    .userPk(userPk)
                    .gamePk(gamePk)
                    .reserveCode(reserveCode)
                    .build();
            reservationMapper.setPreemptionList(preemptionListDTO);

            int reservelistPk = preemptionListDTO.getReservelistPk();
            result.setReservelistPk(reservelistPk);

            for (String seatNum : preemptionDTO.getSeats()) {
                PreemptionReserveDTO preemptionReserveDTO = PreemptionReserveDTO
                        .builder()
                        .reservelistPk(reservelistPk)
                        .zonePk(zonePk)
                        .seatNum(seatNum)
                        .build();
                reservationMapper.setPreemptionReserve(preemptionReserveDTO);
            }

            //Redis에 선점 정보 등록
            if (!redisService.getBleachers(gamePk, userPk, zonePk)) {
                //롤백
                deletePreemptionInfo(reservelistPk);

                result.setPreempted(0);
                result.setErrorMsg("이미 선점된 좌석입니다.");
                return result;
            }
        } finally {
            //락 해제
            redisService.unlockSeat(gamePk, null, zonePk);
        }
        return result;
    }

    /* 선점 함수 - 자동배정 및 일반 구역 */
    @Transactional
    public PreemptionResDTO preemptSeat(PreemptionDTO preemptionDTO, UserDTO user) {
        PreemptionResDTO result = new  PreemptionResDTO();

        int zonePk = preemptionDTO.getZonePk();


        //Redis에 선점 정보 넣기 (int gamePk, List<String> seats, int userPk)
        if(zonePk == 1101 || zonePk == 1100) {
            //외야석이라면
            result = getBleachers(preemptionDTO, user, result, createReserveCode());
        } else {
            //그 외 구역이라면
            result = getPreempt(preemptionDTO, user, result, createReserveCode());
        }
        return result;
    }

    /* 해당 게임에서 유저가 예매한 표 개수 반환 */
    @Transactional
    public int countUserReserve(int gamePk, int userPk) {
        return reservationMapper.countUserReserve(gamePk, userPk);
    }

    /* 선점 확인 */
    @Transactional
    public int confirmPreempt(PreemptConfirmReqDTO preemptConfirmReqDTO, UserDTO user) {
        int gamePk = preemptConfirmReqDTO.getGamePk();
        int zonePk = preemptConfirmReqDTO.getZonePk();
        int userPk = user.getUserPk();

        if(zonePk == 1100 || zonePk == 1101) {
            if (!redisService.confirmPreempt(gamePk, null, userPk, zonePk)) {
                return 2; //선점 끝
            }
        } else {
            for (String seat : preemptConfirmReqDTO.getSeats()) {
                if (!redisService.confirmPreempt(gamePk, seat, userPk, zonePk)) {
                    return 2; //선점 끝
                }
            }
        }
        return 1; //정상
    }

    /* reservelistPk에 해당하는 모든 예약 정보 삭제 */
    @Transactional
    public void deletePreemptionInfo(int reservelistPk) {
        reservationMapper.deletePreemptionReserve(reservelistPk);
        reservationMapper.deletePreemptionList(reservelistPk);
    }

    /* 선점 해제 */
    @Transactional
    public int deletePreemption(PreemptDeleteReqDTO preemptdeleteReqDTO, UserDTO user) {
        List<String> seats = preemptdeleteReqDTO.getSeats();
        int gamePk = preemptdeleteReqDTO.getGamePk();
        int zonePk = preemptdeleteReqDTO.getZonePk();
        int userPk = user.getUserPk();
        int reservelistPk = preemptdeleteReqDTO.getReservelistPk();

        try {
            //해당 정보의 예약이 있는지 확인
            if(zonePk == 1100 || zonePk == 1101) {
                //외야석의 경우
                int check = reservationMapper.getReservelistPkAuto(gamePk, userPk, zonePk);
                if(check != reservelistPk) {
                    return 2;
                }
            } else {
                //그 외 구역의 경우
                for(String seat: seats) {
                    if(reservationMapper.getReservelistPkByUser(gamePk, seat, zonePk, userPk) == null) {
                        return 2;
                    }
                }
            }

            // DB 선점 정보 삭제
            deletePreemptionInfo(reservelistPk);

            // Redis 선점 해제
            boolean redisDeleted = true;
            if(zonePk == 1100 || zonePk == 1101) {
                redisDeleted = redisService.cancelBleachers(gamePk, userPk, zonePk);
            } else {
                redisDeleted = redisService.cancelPreempt(gamePk, seats, userPk, zonePk);
            }

            if (!redisDeleted) {
                //DB 선점 복구
                PreemptionDTO preemptionDTO = new PreemptionDTO();
                preemptionDTO.setGamePk(gamePk);
                preemptionDTO.setZonePk(zonePk);
                preemptionDTO.setQuantity(seats.size());
                preemptionDTO.setSeats(seats);

                preemptSeat(preemptionDTO, user);
                return 2; // Redis 해제 실패
            }

            return 1;
        } catch (Exception e) {
            e.printStackTrace();

            // Redis 복구 시도
            if(zonePk == 1100 || zonePk == 1101) {
                redisService.getBleachers(gamePk, userPk, zonePk);
            } else {
                redisService.preemptSeat(gamePk, seats, userPk, zonePk);
            }

            return 2; // 실패
        }
    }


    /* 자동배정 좌석 선점 해제 */
    public void deletePreemptionAuto(@RequestParam int gamePk, @RequestParam int zonePk, UserDTO user) {
        //Redis 삭제
        redisService.cancelBleachers(gamePk, user.getUserPk(), zonePk);

        //DB 삭제
        Integer reservelistPk = reservationMapper.getReservelistPkAuto(gamePk, user.getUserPk(), zonePk);

        reservationMapper.deletePreemptionReserve(reservelistPk);
        reservationMapper.deletePreemptionList(reservelistPk);
    }

    /* reserve code 생성 */
    public String createReserveCode() {
        StringBuilder sb = new StringBuilder();

        String timePart = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        sb.append(timePart)
            .append(generateRandomMixStr(5, true));

        return sb.toString();
    }

    public static String generateRandomMixStr(int length, boolean isUpperCase) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return isUpperCase ? sb.toString() : sb.toString().toLowerCase();
    }
}
