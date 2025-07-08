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
            String prompt =
                    "당신은 신한 가디언즈의 열혈 야구 팬이야.\n" +
                            "이 팀은 수도권을 연고로 하는 가상의 프로야구 구단으로,젊은 선수들의 성장 가능성과 빠른 주루 플레이, 그리고 열정적인 팬층이 특징이야.\n" +
                            "지금은 팬 커뮤니티 게시판에 글을 쓰려는 상황이야.\n" +
                            String.format("작성할 글의 제목: \"%s\"", title) +
                            "이 제목을 바탕으로 팬이 직접 쓴 것처럼 자연스럽고 친근한 말투로 글을 3~5문장 이내로 작성해줘.\n" +
                            "제목에 경기력에 대한 부정적인 내용이 담겨있다면, 나쁜 말도 쓰면서 확실하게 부정적으로 써줘.\n" +
                            "인공지능이 쓴 것처럼 보이는 문장은 지양해줘.";
            String aiContent = gptService.generateContext(prompt);
            String cleaned = aiContent.replaceAll("^\"|\"$", "");

            Map<String, String> res = new HashMap<>();
            res.put("recommendation", cleaned);
            return res;
        }catch(Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
