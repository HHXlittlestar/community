package com.huhx.community.controller;

import com.huhx.community.dto.PageInfoDTO;
import com.huhx.community.mapper.UserMapper;
import com.huhx.community.service.ShowQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ShowQuestion showQuestion;

    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(value = "page", defaultValue = "1") Integer page,
                        @RequestParam(value = "size", defaultValue = "5") Integer size){


        //在返回首页之前进行话题内容查询，并携带到主页去显示
        PageInfoDTO pageInfo = showQuestion.findAllQuestionByPage(page, size);
        model.addAttribute("pageInfo", pageInfo);
        return "index.html";
    }
}
