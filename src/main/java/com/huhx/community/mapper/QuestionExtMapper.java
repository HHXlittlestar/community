package com.huhx.community.mapper;

import com.huhx.community.model.Question;

public interface QuestionExtMapper {
    void incView(Question question);
    void incCommentCount(Question question);
}
