package org.baseball.domain.community;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.baseball.dto.PostDto;

@Mapper
public interface CommunityMapper {
    int countAll();
    List<PostDto> getPostList(Map<String, Object> param);
    List<PostDto> getPostPage(Map<String, Object> param);
}