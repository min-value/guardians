package org.baseball.domain.community;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.baseball.dto.CommentDTO;
import org.baseball.dto.PostDTO;

@Mapper
public interface CommunityMapper {
    List<PostDTO> getPostPageWithSearch(Map<String, Object> param);
    int countFiltered(Map<String, Object> param);
    PostDTO selectPostById(int postPk);
    List<CommentDTO> getCommentPage(Map<String, Object> param);
    int countComment(int postPk);
    void deletePost(int postPk);
    void deleteCommentsInPost(int postPk);
    void insertComment(CommentDTO dto);
    void deleteComment(int postPk);
    void addPost(PostDTO dto);
    String getUserNameByPk(int user_pk);
    void modifyPost(PostDTO dto);
    void updateComment(CommentDTO dto);
}