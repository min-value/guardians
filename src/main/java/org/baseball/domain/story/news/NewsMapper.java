package org.baseball.domain.story.news;

import org.apache.ibatis.annotations.Mapper;
import org.baseball.dto.NewsDTO;

import java.util.Map;
import java.util.List;

@Mapper
public interface NewsMapper {
    void insertNews(NewsDTO newsDTO);
    List<NewsDTO> getNews(Map<String, Object> param);
    int countNews(Map<String, Object> param);
}
