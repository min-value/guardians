package org.baseball.domain.payment;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
@Controller
@PropertySource("classpath:iamport.properties")
public class PaymentController {

    @Value("${apiKey}")
    private String apiKey;

    @Value("${apiSecret}")
    private String apiSecret;

    private IamportClient api;

    // 결제
    @RequestMapping("/verifyPayment/{imp_uid}")
    @ResponseBody
    public IamportResponse<Payment> paymentByImpUid(@PathVariable("imp_uid") String imp_uid)
            throws IamportResponseException, IOException {
        if (api == null) {
            api = new IamportClient(apiKey, apiSecret);
        }
        return api.paymentByImpUid(imp_uid);
    }

    // 결제 취소
    @PostMapping("/cancelPayment/{imp_uid}")
    @ResponseBody
    public IamportResponse<Payment> cancelPayment(@PathVariable("imp_uid") String impUid) throws Exception {
        if (api == null) {
            api = new IamportClient(apiKey, apiSecret);
        }
        CancelData cancelData = new CancelData(impUid, true); // 전액 환불
        return api.cancelPaymentByImpUid(cancelData);
    }
}
