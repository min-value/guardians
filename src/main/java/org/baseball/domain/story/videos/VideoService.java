package org.baseball.domain.story.videos;

import org.baseball.dto.NewsDTO;
import org.baseball.dto.VideoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VideoService {

    @Autowired
    private VideoMapper mapper;

    public void addVideo(VideoDTO dto){
        mapper.insertVideo(dto);
    }

    public Map<String, Object> getVideo(int page, String type, String keyword){
        int size = 6;
        int offset = (page - 1) * size;

        Map<String, Object> param = new HashMap<>();
        param.put("offset", offset);
        param.put("size", size);
        param.put("type", type);
        param.put("keyword", keyword);

        List<NewsDTO> list = mapper.getVideo(param);
        int count = mapper.countVideo(param);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("totalCount", count);

        return result;
    }
}
