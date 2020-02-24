package com.huhx.community.service;

import com.huhx.community.dto.PageInfoDTO;
import com.huhx.community.dto.QuestionDTO;
import com.huhx.community.exception.CustomizeErrorCode;
import com.huhx.community.exception.CustomizeException;
import com.huhx.community.mapper.QuestionExtMapper;
import com.huhx.community.mapper.QuestionMapper;
import com.huhx.community.mapper.UserMapper;
import com.huhx.community.model.Question;
import com.huhx.community.model.QuestionExample;
import com.huhx.community.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;
    //完成PageInfoDTO对象的封装
    PageInfoDTO getPageInfoDTO(List<Question> questions){
        List<QuestionDTO> questionDTOs = new LinkedList<>();
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOs.add(questionDTO);
        }
        PageInfoDTO pageInfoDTO = new PageInfoDTO();
        pageInfoDTO.setQuestions(questionDTOs);
        return pageInfoDTO;

    }

    //查找所有的问题
    public PageInfoDTO findAllQuestionByPage(int page, int size){
        int starIndex = (page - 1) * size;
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(new QuestionExample(), new RowBounds(starIndex, size));
        int totalCount = (int)questionMapper.countByExample(new QuestionExample());
        PageInfoDTO pageInfoDTO = getPageInfoDTO(questions);
        pageInfoDTO.setPageInfo(totalCount, page, size);
        return pageInfoDTO;
    }

    //查找所有我提问的问题
    public PageInfoDTO findMyQuestionByPage(Long userId, int page, int size){
        int starIndex = (page - 1) * size;
        QuestionExample example = new QuestionExample();
        example.createCriteria().andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(example, new RowBounds(starIndex, size));
        int totalCount = (int)questionMapper.countByExample(new QuestionExample());
        PageInfoDTO pageInfoDTO = getPageInfoDTO(questions);
        pageInfoDTO.setPageInfo(totalCount, page, size);
        return pageInfoDTO;
    }

    //根据问题的ID查找
    public QuestionDTO getQuestionById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if(question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void updateOrInsertQuestion(Question question) {
        QuestionExample example = new QuestionExample();
        example.createCriteria().andIdEqualTo(question.getId());
        List<Question> dbQuestions = questionMapper.selectByExample(example);
        if (dbQuestions.size() != 0) {
            //更新
            Question dbQuestion = dbQuestions.get(0);
            Question updateQuestion = new Question();
            updateQuestion.setGmtModify(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());

            QuestionExample example1 = new QuestionExample();
            example1.createCriteria().andIdEqualTo(dbQuestion.getId());
            int update = questionMapper.updateByExampleSelective(updateQuestion, example1);
            if(update != 1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }else{
            //插入
            question.setGmtCreat(System.currentTimeMillis());
            question.setGmtModify(question.getGmtCreat());
            questionMapper.insert(question);
        }
    }

    public void incView(Long id){
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
    }
}
