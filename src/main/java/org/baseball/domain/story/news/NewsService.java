package org.baseball.domain.story.news;

import org.baseball.dto.NewsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NewsService {

    @Autowired
    private NewsMapper mapper;

    public void addNews(NewsDTO dto){
        mapper.insertNews(dto);
    }

    public Map<String, Object> getNews(int page, String type, String keyword){
        int size = 4;
        int offset = (page - 1) * size;

        Map<String, Object> param = new HashMap<>();
        param.put("offset", offset);
        param.put("size", size);
        param.put("type", type);
        param.put("keyword", keyword);

        List<NewsDTO> list = mapper.getNews(param);
        int count = mapper.countNews(param);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("totalCount", count);

        return result;
    }
}
