package org.baseball.domain.story.videos;

import lombok.RequiredArgsConstructor;
import org.baseball.dto.VideoDTO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
@RequiredArgsConstructor
public class VideoScheduler {
    @Autowired
    private final VideoService videoService;

    @Value("${youtube.api.key}")
    private String youtubeApiKey;

    @Scheduled(cron = "0 30 00 * * *")
    public void crawlYoutube()throws Exception{
        String query = "nc다이노스 하이라이트";
        String urlString = "https://www.googleapis.com/youtube/v3/search?part=snippet&q="
                + java.net.URLEncoder.encode(query, "UTF-8")
                + "&type=video&maxResults=6&key=" + youtubeApiKey;
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try(InputStreamReader reader = new InputStreamReader(conn.getInputStream())) {
            StringBuilder sb = new StringBuilder();
            int c;
            while((c = reader.read()) != -1) sb.append((char) c);

            JSONObject json = new JSONObject(sb.toString());
            JSONArray items = json.getJSONArray("items");

            for(int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                JSONObject snippet = item.getJSONObject("snippet");

                String title = snippet.getString("title");
                String publishedAt = snippet.getString("publishedAt");
                String thumbnailUrl = snippet.getJSONObject("thumbnails").getJSONObject("medium").getString("url");
                String videoId = item.getJSONObject("id").getString("videoId");
                String videoUrl = "https://www.youtube.com/watch?v=" + videoId;

                VideoDTO dto = new VideoDTO();
                dto.setV_title(title);
                dto.setV_date(publishedAt.substring(0,10));
                dto.setV_url(videoUrl);
                dto.setV_id(videoId);
                dto.setThumbnail_url(thumbnailUrl);

                videoService.addVideo(dto);
            }
        }
    }
}
