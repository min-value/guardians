package org.baseball.domain.payment;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
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

    @RequestMapping("/verifyPayment/{imp_uid}")
    @ResponseBody
    public IamportResponse<Payment> paymentByImpUid(@PathVariable("imp_uid") String imp_uid)
            throws IamportResponseException, IOException {
        if (api == null) {
            api = new IamportClient(apiKey, apiSecret);
        }
        return api.paymentByImpUid(imp_uid);
    }
}
