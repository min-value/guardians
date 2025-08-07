package org.baseball.domain.story.news;

import org.springframework.ui.Model;
import org.baseball.dto.NewsDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

@Controller
@RequestMapping("/story/news")
public class    NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping("")
    public String news(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword,
            Model model){
        Map<String, Object> result = newsService.getNews(page, type, keyword);
        model.addAttribute("list", result.get("list"));
        model.addAttribute("totalCount", result.get("totalCount"));
        model.addAttribute("type", result.get("type"));
        model.addAttribute("keyword", result.get("keyword"));
        model.addAttribute("page", result.get("page"));

        return "story/news";
    }

    @GetMapping("/page")
    @ResponseBody
    public Map<String, Object> getNewsPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword){
        return newsService.getNews(page, type, keyword);
    }
}
