package org.baseball.domain.community;

import org.baseball.dto.PostDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommunityService {

    @Autowired
    private CommunityMapper mapper;

    public int checkCount(PostDto postDto) {
        return mapper.count(postDto);
    }
}
