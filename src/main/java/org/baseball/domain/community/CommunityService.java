package org.baseball.domain.community;

import org.baseball.dto.CommentDTO;
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

    public Map<String, Object> getPostpageWithSearch(int page, String type, String keyword){
        int size = 10;
        int offset = (page - 1) * size;

        Map<String, Object> param = new HashMap<>();
        param.put("offset", offset);
        param.put("size", size);
        param.put("type", type);
        param.put("keyword", keyword);

        List<PostDto> list = mapper.getPostPageWithSearch(param);
        int count = mapper.countFiltered(param);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("totalCount", count);

        return result;
    }

    public PostDto getPostById(int postPk){
        return mapper.selectPostById(postPk);
    }

    public Map<String, Object> getCommentById(int postPk, int page){
        int size = 5;
        int offset = (page - 1) * size;

        Map<String, Object> param = new HashMap<>();
        param.put("post_pk", postPk);
        param.put("offset", offset);
        param.put("size", size);
        List<CommentDTO> list = mapper.getCommentPage(param);
        int totalCount = mapper.countComment(postPk);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("totalCount", totalCount);

        return result;
    }
}
