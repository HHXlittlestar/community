package com.huhx.community.service;

import com.huhx.community.dto.PageInfoDTO;
import com.huhx.community.dto.QuestionDTO;
import com.huhx.community.mapper.QuestionMapper;
import com.huhx.community.mapper.UserMapper;
import com.huhx.community.model.Question;
import com.huhx.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class ShowQuestion {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    //完成PageInfoDTO对象的封装
    public PageInfoDTO findQuestionByPage(int page, int size){
        int starIndex = (page - 1) * size;
        List<Question> questions = questionMapper.findByPage(starIndex, size);
        int totalCount = questionMapper.findAll();
        List<QuestionDTO> questionDTOs = new LinkedList<>();
        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOs.add(questionDTO);
        }
        PageInfoDTO pageInfoDTO = new PageInfoDTO();
        pageInfoDTO.setQuestions(questionDTOs);

        pageInfoDTO.setPageInfo(totalCount, page, size);
        return pageInfoDTO;
    }
}
