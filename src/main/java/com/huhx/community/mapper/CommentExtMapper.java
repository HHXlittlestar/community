package com.huhx.community.mapper;

import com.huhx.community.model.Comment;

public interface CommentExtMapper {
    void incCommentCount(Comment comment);
}
