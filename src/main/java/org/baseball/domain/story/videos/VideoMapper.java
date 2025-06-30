package org.baseball.domain.story.videos;

import org.apache.ibatis.annotations.Mapper;
import org.baseball.dto.NewsDTO;
import org.baseball.dto.VideoDTO;

import java.util.List;
import java.util.Map;

@Mapper
public interface VideoMapper {
    void insertVideo(VideoDTO dto);
    List<NewsDTO> getVideo(Map<String, Object> param);
    int countVideo(Map<String, Object> param);
}
