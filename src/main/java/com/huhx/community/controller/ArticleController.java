package com.huhx.community.controller;

import com.huhx.community.dto.CommentDTO;
import com.huhx.community.dto.QuestionDTO;
import com.huhx.community.enums.CommentTypeEnum;
import com.huhx.community.service.CommentService;
import com.huhx.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ArticleController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;
    @GetMapping("/article/{id}")
    public String content(@PathVariable("id") Long id,
                          Model model){
        QuestionDTO questionDTO = questionService.getQuestionById(id);
        questionService.incView(id);
        List<CommentDTO> comments = commentService.findAllComments(id, CommentTypeEnum.QUESTION);
        model.addAttribute("questionDTO", questionDTO);
        model.addAttribute("comments", comments);
        return "article";
    }

}
