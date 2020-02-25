package com.huhx.community.dto;

import com.huhx.community.model.User;
import lombok.Data;

@Data
public class CommentDTO {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Long gmtCreat;
    private Long gmtModify;
    private Long likeCount;
    private String content;
    private Integer commentCount;
    private User user;
}
