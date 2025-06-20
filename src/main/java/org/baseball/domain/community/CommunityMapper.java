package org.baseball.domain.community;

import org.apache.ibatis.annotations.Mapper;
import org.baseball.dto.PostDto;

@Mapper
public interface CommunityMapper {
    int count(PostDto postDto);
}
