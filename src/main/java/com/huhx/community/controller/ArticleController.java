package com.huhx.community.controller;

import com.huhx.community.dto.QuestionDTO;
import com.huhx.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ArticleController {
    @Autowired
    private QuestionService questionService;
    @GetMapping("/article/{id}")
    public String content(@PathVariable("id") Long id,
                          Model model){
        QuestionDTO questionDTO = questionService.getQuestionById(id);
        questionService.incView(id);
        model.addAttribute("questionDTO", questionDTO);
        return "article";
    }

}
