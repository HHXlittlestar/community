package com.huhx.community.controller;

import com.huhx.community.dto.PageInfoDTO;
import com.huhx.community.model.User;
import com.huhx.community.service.NotificationService;
import com.huhx.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action") String action,
                          HttpServletRequest request,
                          Model model,
                          @RequestParam(value = "page", defaultValue = "1") Integer page,
                          @RequestParam(value = "size", defaultValue = "5") Integer size){
        User user = (User)request.getSession().getAttribute("user");
        if(user == null){
            return "redirect:/";
        }

        if ("questions".equals(action)){
            //如果是questions，就显示我的问题
            PageInfoDTO pageInfoDTO = questionService.findMyQuestionByPage(user.getId(), page, size);
            model.addAttribute("nowPage", "我的问题");
            model.addAttribute("section", "questions");
            model.addAttribute("pageInfo", pageInfoDTO);
        }else if("replies".equals(action)){
            //如果是replies，就显示我的回复
            PageInfoDTO pageInfoDTO = questionService.findMyQuestionByPage(user.getId(), page, size);
            model.addAttribute("nowPage", "我的回复");
            model.addAttribute("section", "replies");
            model.addAttribute("pageInfo", pageInfoDTO);
        }else{
            //如果是messages，就显示我的消息
            PageInfoDTO pageInfoDTO = notificationService.list(user.getId(), page, size);
            model.addAttribute("nowPage", "我的消息");
            model.addAttribute("section", "messages");
            model.addAttribute("pageInfo", pageInfoDTO);
        }
        return "profile";
    }
}
