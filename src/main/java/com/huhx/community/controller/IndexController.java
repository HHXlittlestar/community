package com.huhx.community.controller;

import com.huhx.community.dto.QuestionDTO;
import com.huhx.community.mapper.UserMapper;
import com.huhx.community.model.User;
import com.huhx.community.service.ShowQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ShowQuestion showQuestion;

    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model){
        Cookie[] cookies = request.getCookies();
        if(cookies.length != 0 && cookies != null){
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    User user = userMapper.findByToken(token);
                    if (user != null){
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }
        
        //在返回首页之前进行话题内容查询，并携带到主页去显示
        List<QuestionDTO> questions = showQuestion.findAllQuestion();
        model.addAttribute("questions", questions);
        return "index.html";
    }
}
