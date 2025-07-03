package org.baseball.domain.community.gpt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/gpt")
public class GptController {

    @Autowired
    private GptService gptService;

    @PostMapping("/aiRecommend")
    @ResponseBody
    public Map<String, String> recommendContent(@RequestParam String title) throws IOException{
        try {
            //String title = req.get("title");

            String prompt =
                    "신한 가디언즈는 가상의 프로야구 구단으로, 팀워크와 근성, 팬 서비스가 뛰어난 팀입니다.\n" +
                            "수도권을 연고로 하며, 젊은 선수들의 성장과 활기찬 응원이 특징입니다.\n" +
                            "특히 최근에는 빠른 주루와 짜임새 있는 수비로 주목받고 있습니다.\n" +

                            "다음은 야구 팬 커뮤니티에 올릴 글입니다.\n" +
                            String.format("제목은 \"%s\"이고, 신한 가디언즈 팀과 관련된 글 내용을 3~5문장으로 자연스럽게 작성해주세요.", title);
            System.out.println("req:"+title);
            String aiContent = gptService.generateContext(prompt);

            Map<String, String> res = new HashMap<>();
            res.put("recommendation", aiContent);
            System.out.println("받은 제목: " + title);
            return res;
        }catch(Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
