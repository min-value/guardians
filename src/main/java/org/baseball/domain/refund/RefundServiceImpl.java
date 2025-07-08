package org.baseball.domain.refund;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.baseball.dto.RefundDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@PropertySource("classpath:iamport.properties")
public class RefundServiceImpl implements RefundService {

    private final RefundMapper refundMapper;

    @Value("${apiKey}")
    private String apiKey;

    @Value("${apiSecret}")
    private String apiSecret;

    @Override
    public int refundCanceledGames() {
        int refundCount = 0;

        List<RefundDTO> targetList = refundMapper.getRefundTargets();
        log.info("환불 건수: {}", targetList.size());

        for (RefundDTO dto : targetList) {
            try {
                IamportClient iamportClient = new IamportClient(apiKey, apiSecret);

                // 아임포트 환불
                if (dto.getImpUid() != null && !dto.getImpUid().isEmpty() && dto.getPaidAmount() > 0) {
                    CancelData cancelData = new CancelData(dto.getImpUid(), false);
                    IamportResponse<Payment> response = iamportClient.cancelPaymentByImpUid(cancelData);
                    log.info("환불 성공 - impUid: {}", dto.getImpUid());
                }

                // 포인트 환불
                if (dto.getUsedPoint() > 0) {
                    dto.setPoint(dto.getUsedPoint());
                    dto.setType("티켓 예매 포인트 환불");
                    dto.setDescription("사용 환불");
                    refundMapper.insertPoint(dto);
                    refundMapper.updateUserPoint(dto.getUserPk(), dto.getUsedPoint());
                    log.info("포인트 {} 환불 - userPk: {}", dto.getUsedPoint(), dto.getUserPk());
                }

                // 결제 상태 변경
                refundMapper.updateReservationState(dto.getReservelistPk(), "CANCELED");

                // 예약 상태 변경
                refundMapper.updateReservationCancel(dto.getReservelistPk());

                // 환불 시간 기록
                dto.setCancelDate(Timestamp.valueOf(LocalDateTime.now()));
                refundMapper.updateCancelDate(dto);

                refundCount++;

            } catch (IamportResponseException | IOException e) {
                log.error("아임포트 환불 실패 - impUid: {}, 이유: {}", dto.getImpUid(), e.getMessage());
            } catch (Exception e) {
                log.error("환불 처리 중 에러 - userPk: {}, reservelistPk: {}", dto.getUserPk(), dto.getReservelistPk());
                log.error("message : ", e);
            }
        }

        return refundCount;
    }
}
