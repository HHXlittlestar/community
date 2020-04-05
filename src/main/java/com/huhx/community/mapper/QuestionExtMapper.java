package com.huhx.community.mapper;

import com.huhx.community.dto.QuestionQueryDTO;
import com.huhx.community.model.Question;
import java.util.List;

public interface QuestionExtMapper {
    void incView(Question question);
    void incCommentCount(Question question);
    List<Question> findRelatedQuestion(Question question);
    int countBySearch(QuestionQueryDTO questionQueryDTO);

    List<Question> selectBySearchWithRowbounds(QuestionQueryDTO questionQueryDTO);
}
