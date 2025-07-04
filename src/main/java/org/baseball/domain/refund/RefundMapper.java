package org.baseball.domain.refund;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.baseball.dto.RefundDTO;

import java.util.List;

@Mapper
public interface RefundMapper {

    // 환불 대상 조회
    List<RefundDTO> getRefundTargets();

    // 예약 상태를 CANCELED로 변경 (reservations 테이블)
    void updateReservationState(@Param("reservelistPk") int reservelistPk, @Param("state") String state);

    // 예약리스트 상태를 취소로 변경 (reservation_list 테이블)
    void updateReservationCancel(@Param("reservelistPk") int reservelistPk);

    // 환불 시간 기록
    void updateCancelDate(RefundDTO dto);

    // 포인트 테이블에 환불 기록
    void insertPoint(RefundDTO dto);

    // 유저 total_point 증가
    void updateUserPoint(@Param("userPk") int userPk, @Param("point") int point);
}
