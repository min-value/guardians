package org.baseball.domain.story.videos;

import org.baseball.dto.VideoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.json.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/story/videos")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @GetMapping("")
    public String videos(@RequestParam(defaultValue = "1") int page,
                         @RequestParam(required = false) String type,
                         @RequestParam(required = false) String keyword,
                         Model model){
        Map<String, Object> result = videoService.getVideo(page, type, keyword);
        model.addAttribute("list", result.get("list"));
        model.addAttribute("totalCount", result.get("totalCount"));
        model.addAttribute("type", result.get("type"));
        model.addAttribute("keyword", result.get("keyword"));
        model.addAttribute("page", result.get("page"));
        return "story/videos";
    }

    @GetMapping("/page")
    @ResponseBody
    public Map<String, Object> getVideosPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword){
        return videoService.getVideo(page, type, keyword);
    }

    @GetMapping("/crawl")
    public void crawlYoutube()throws Exception{
        String apiKey = "AIzaSyD6khCzj3c_ggnzMWFjCHOoa-EEO5-SvpA";
        String query = "nc다이노스 하이라이트";
        String urlString = "https://www.googleapis.com/youtube/v3/search?part=snippet&q="
                + java.net.URLEncoder.encode(query, "UTF-8")
                + "&type=video&maxResults=14&key=" + apiKey;
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
                dto.setThumbnail_url(thumbnailUrl);

                videoService.addVideo(dto);
            }
        }
    }
}
