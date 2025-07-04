package org.baseball.domain.reservation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.baseball.domain.Redis.RedisService;
import org.baseball.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ReservationService {
    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired
    private RedisService redisService;

    //게임 정보(상대팀) 반환
    @Transactional
    public ReserveGameInfoDTO getGameInfo(int gamePk) {
        return reservationMapper.getGameInfo(gamePk);
    }

    //팔린 좌석 목록 반환
    @Transactional
    public List<String> getSoldSeats(SoldSeatsReqDTO dto) {
        return reservationMapper.getSoldSeats(dto);
    }

    //구역 정보 반환
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

    @Transactional
    public ZoneDTO getZoneInfo(int zonePk) {
        return reservationMapper.getZoneInfo(zonePk);
    }

    // 구역 정보 가져오기
    @Transactional
    public boolean getSeatInfo(int gamePk, HttpSession session) throws JsonProcessingException {
        //구역 정보 가져오기
        List<ZoneDTO> zoneDTOList = getZones(gamePk);

        //구역 별 남은 좌석 수 저장
//        Map<ZoneDTO, Integer> zoneMap = new LinkedHashMap<>();
        Map<Integer, ZoneDTO> zoneInfo = new LinkedHashMap<>();
        Map<Integer, List<String>> zoneDetail = new LinkedHashMap<>();

        //팔린 좌석들 가져오기
        for(ZoneDTO zoneDTO : zoneDTOList){
            List<String> seats = getSoldSeats(new SoldSeatsReqDTO(gamePk, zoneDTO.getZonePk()));
            zoneInfo.put(zoneDTO.getZonePk(), zoneDTO);
            zoneDetail.put(zoneDTO.getZonePk(), seats);
        }

        //구역 당 남은 좌석 수 세션에 저장
        session.setAttribute("zoneMap", zoneDTOList);
        session.setAttribute("zoneInfo", new ObjectMapper().writeValueAsString(zoneInfo));

        //구역 당 상세 정보 세션에 저장
        session.setAttribute("zoneMapDetail", new ObjectMapper().writeValueAsString(zoneDetail));

        return true;
    }

    // 구역 정보 가져오기
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

    @Transactional
    public PreemptionResDTO preemptSeat(PreemptionDTO preemptionDTO, UserDTO userDTO, PreemptionResDTO result, String reserveCode) {
        int zonePk = preemptionDTO.getZonePk();
        int gamePk =  preemptionDTO.getGamePk();


        //선점 여부 확인
        for(String seatNum : preemptionDTO.getSeats()) {
            int check = reservationMapper.confirmPreemption(zonePk, seatNum, gamePk);

            if(check == 1) {
                result.setPreempted(0);
                result.setErrorMsg("이미 선점된 좌석입니다.");
                return result;
            }
        }
        result.setPreempted(1);

        //선점/판매가 되지 않았다면 선점
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

        return result;
    }

    @Transactional
    public PreemptionResDTO getBleachers(PreemptionDTO preemptionDTO, UserDTO userDTO, PreemptionResDTO result, String reserveCode) {
        int zonePk = preemptionDTO.getZonePk();
        int gamePk =  preemptionDTO.getGamePk();
        int quantity = preemptionDTO.getQuantity();
        List<String> seats =  preemptionDTO.getSeats();
        int userPk = userDTO.getUserPk();

        result.setPreempted(1);

        //선점
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

        for(String seatNum : preemptionDTO.getSeats()) {
            PreemptionReserveDTO preemptionReserveDTO = PreemptionReserveDTO
                    .builder()
                    .reservelistPk(reservelistPk)
                    .zonePk(zonePk)
                    .seatNum(seatNum)
                    .build();
            reservationMapper.setPreemptionReserve(preemptionReserveDTO);
        }

        return result;
    }

    @Transactional
    public void deletePreemption(int reservelistPk) {
        reservationMapper.deletePreemptionReserve(reservelistPk);
        reservationMapper.deletePreemptionList(reservelistPk);
    }

    @Transactional
    public List<DiscountDTO> getDiscountInfo(){
        return reservationMapper.getDiscountInfo();
    }

    @Transactional
    public boolean isOurGame(int gamePk) { return reservationMapper.isOurGame(gamePk); }

    @Transactional
    public int countUserReserve(int gamePk, int userPk) {
        return reservationMapper.countUserReserve(gamePk, userPk);
    }

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

    @Transactional
    public PreemptionResDTO preemptSeat(PreemptionDTO preemptionDTO, UserDTO user) {
        PreemptionResDTO result = new  PreemptionResDTO();

        int userPk =  user.getUserPk();
        int quantity = preemptionDTO.getQuantity();
        int gamePk = preemptionDTO.getGamePk();
        int zonePk = preemptionDTO.getZonePk();
        List<String> seats =  preemptionDTO.getSeats();


        //Redis에 선점 정보 넣기 (int gamePk, List<String> seats, int userPk)
        if(zonePk == 1101 || zonePk == 1100) {
            //외야석이라면
            try {
                //락 잡기
                if(!redisService.tryLockSeat(gamePk, null, zonePk)) {
                    result.setPreempted(0);
                    result.setErrorMsg("이미 선점된 좌석입니다.");
                    return result;
                }
                //DB 탐색해 남은 좌석 개수 판단
                SoldSeatsReqDTO soldSeatsReqDTO = new SoldSeatsReqDTO(gamePk, zonePk);

                List<String> soldSeats = getSoldSeats(soldSeatsReqDTO); //남은 좌석 정보

                ZoneDTO zoneDTO = getZoneInfo(preemptionDTO.getZonePk()); //구역 정보

                int remainingSeats = zoneDTO.getTotalNum() - soldSeats.size(); //남은 좌석 수

                if(remainingSeats < quantity) {
                    //좌석 수가 충분하지 않다면
                    result.setPreempted(2);
                    result.setErrorMsg("좌석 수가 충분하지 않습니다.");
                    return result;
                }

                if(!redisService.getBleachers(gamePk, userPk, zonePk)) {
                    result.setPreempted(0);
                    result.setErrorMsg("이미 선점된 좌석입니다.");
                    return result;
                }

                //좌석 수 OK -> Redis에 선점
                //DB에 등록
                result = getBleachers(preemptionDTO, user, result, createReserveCode(gamePk, zonePk, userPk));
            } catch(Exception e) {
                //롤백
                //redis 롤백
                redisService.cancelBleachers(gamePk, userPk, zonePk);
                //오류메시지
                result.setPreempted(3);
                result.setErrorMsg("내부 오류로 선점이 취소되었습니다.");
            } finally{
                //락 해제
                redisService.unlockSeat(gamePk, null, zonePk);
            }
        } else {
            //그 외 구역이라면

            //좌석 선점하기

            //DB에 선점 정보 넣기
            try {
                if(redisService.preemptSeat(gamePk, seats, userPk, zonePk)) {
                    result = preemptSeat(preemptionDTO, user, result, createReserveCode(gamePk, zonePk, userPk));
                } else {
                    result.setPreempted(0);
                    result.setErrorMsg("이미 선점된 좌석입니다.");
                }
            } catch (Exception e) {
                //롤백
                //redis 롤백
                redisService.cancelPreempt(gamePk, seats, userPk, zonePk);
                result.setPreempted(2);
                result.setErrorMsg("내부 오류로 선점이 취소되었습니다.");
            }
        }
        return result;
    }

    @Transactional
    public int deletePreemption(PreemptDeleteReqDTO preemptdeleteReqDTO, UserDTO user) {
        List<String> seats = preemptdeleteReqDTO.getSeats();
        int gamePk = preemptdeleteReqDTO.getGamePk();
        int zonePk = preemptdeleteReqDTO.getZonePk();
        int userPk = user.getUserPk();
        int reservelistPk = preemptdeleteReqDTO.getReservelistPk();

        try {
            // Redis 선점 해제
            boolean redisDeleted = true;
            if(zonePk == 1100 || zonePk == 1101) {
                redisDeleted = redisService.cancelBleachers(gamePk, userPk, zonePk);
            } else {
                redisDeleted = redisService.cancelPreempt(gamePk, seats, userPk, zonePk);
            }

            if (!redisDeleted) return 2; // Redis 해제 실패


            //해당 정보의 예약이 있는지 확인 todo
            if(zonePk == 1100 || zonePk == 1101) {
                //외야석의 경우
                if(reservationMapper.getReservelistPkAuto(gamePk, userPk, zonePk) != reservelistPk) {
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
            deletePreemption(reservelistPk);
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


    public void deletePreemptionAuto(@RequestParam int gamePk, @RequestParam int zonePk, UserDTO user) {
        //Redis 삭제
        redisService.cancelBleachers(gamePk, user.getUserPk(), zonePk);

        //DB 삭제
        Integer reservelistPk = reservationMapper.getReservelistPkAuto(gamePk, user.getUserPk(), zonePk);

        reservationMapper.deletePreemptionReserve(reservelistPk);
        reservationMapper.deletePreemptionList(reservelistPk);
    }

    public String createReserveCode(int gamePk, int userPk, int zonePk) {
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
