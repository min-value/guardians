package org.baseball.domain.community.gpt;

//import com.openai.client.OpenAIClient;
//import com.openai.client.okhttp.OpenAIOkHttpClient;
//import com.openai.models.ChatCompletion;
//import com.openai.models.ChatCompletionCreateParams;
//import com.openai.models.ChatModel;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatCompletion;
import com.openai.models.ChatCompletionCreateParams;
import com.openai.models.ChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@PropertySource("classpath:application.properties")
@Service
public class GptService {

    @Value("${gpt.api.key}")
    private String apiKey;

    public String generateContext(String prompt) throws IOException {
        try {
            OpenAIClient client = OpenAIOkHttpClient.builder()
                    .apiKey(apiKey)
                    .build();
            ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                    .addUserMessage(prompt)
                    .model(ChatModel.GPT_3_5_TURBO)
                    .build();
            ChatCompletion chatCompletion = client.chat().completions().create(params);
            System.out.println(chatCompletion);

            return chatCompletion.choices().get(0).message().content().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
      }

}
