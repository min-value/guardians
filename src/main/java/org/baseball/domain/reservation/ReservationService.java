package org.baseball.domain.reservation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.baseball.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ReservationService {
    @Autowired
    private ReservationMapper reservationMapper;

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
    public List<ZoneDTO> getZones() {
        return reservationMapper.getZones();
    }

    // 구역 정보 가져오기
    @Transactional
    public boolean getSeatInfo(int gamePk, HttpSession session) throws JsonProcessingException {
        //구역 정보 가져오기
        List<ZoneDTO> zoneDTOList = getZones();

        //구역 별 남은 좌석 수 저장
//        Map<ZoneDTO, Integer> zoneMap = new LinkedHashMap<>();
        Map<Integer, ZoneDTO> zoneInfo = new LinkedHashMap<>();
        Map<Integer, List<String>> zoneDetail = new LinkedHashMap<>();

        //팔린 좌석들 가져오기
        for(ZoneDTO zoneDTO : zoneDTOList){
            List<String> seats = getSoldSeats(new SoldSeatsReqDTO(gamePk, zoneDTO.getZonePk()));
            zoneDTO.setRemainingNum(zoneDTO.getTotalNum() - seats.size());
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

    @Transactional
    public PreemptionResDTO preemptSeat(PreemptionDTO preemptionDTO, UserDTO userDTO) {
        PreemptionResDTO response = new PreemptionResDTO();
        int zonePk = preemptionDTO.getZonePk();
        int gamePk =  preemptionDTO.getGamePk();


        //선점 여부 확인
        for(String seatNum : preemptionDTO.getSeats()) {
            int result = reservationMapper.confirmPreemption(zonePk, seatNum, gamePk);

            if(result == 1) {
                response.setPreempted(false);
                return response;
            }
        }
        response.setPreempted(true);

        //선점/판매가 되지 않았다면 선점
        PreemptionListDTO preemptionListDTO = PreemptionListDTO
                .builder()
                .quantity(preemptionDTO.getQuantity())
                .userPk(userDTO.getUserPk())
                .gamePk(preemptionDTO.getGamePk())
                .build();

        reservationMapper.setPreemptionList(preemptionListDTO);
        int reservelistPk = preemptionListDTO.getReservelistPk();
        response.setReservelistPk(reservelistPk);

        for(String seatNum : preemptionDTO.getSeats()) {
            PreemptionReserveDTO preemptionReserveDTO = PreemptionReserveDTO
                    .builder()
                    .reservelistPk(reservelistPk)
                    .zonePk(preemptionDTO.getZonePk())
                    .seatNum(seatNum)
                    .build();
            reservationMapper.setPreemptionReserve(preemptionReserveDTO);
        }

        return response;
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
}
