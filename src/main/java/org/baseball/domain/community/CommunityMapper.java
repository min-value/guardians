package org.baseball.domain.community;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.baseball.dto.CommentDTO;
import org.baseball.dto.PostDto;

@Mapper
public interface CommunityMapper {
    List<PostDto> getPostPageWithSearch(Map<String, Object> param);
    int countFiltered(Map<String, Object> param);
    PostDto selectPostById(int postPk);
    List<CommentDTO> getCommentPage(Map<String, Object> param);
    int countComment(int postPk);
    void deletePost(int postPk);
    void deleteCommentsInPost(int postPk);
    void insertComment(CommentDTO dto);
    void deleteComment(int postPk);
    void addPost(PostDto dto);
    String getUserNameByPk(int user_pk);
    void modifyPost(PostDto dto);
    void updateComment(CommentDTO dto);
}