package org.baseball.domain.community;

import org.baseball.dto.PostDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommunityService {

    @Autowired
    private CommunityMapper mapper;

    public Map<String, Object> getPostList(String type, String keyword){
        Map<String, Object> param = new HashMap<>();
        param.put("type", type);
        param.put("keyword", keyword);

        List<PostDto> list = mapper.getPostList(param);
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        return map;
    }
}
