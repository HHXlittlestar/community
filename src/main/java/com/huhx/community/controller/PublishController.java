package com.huhx.community.controller;

import com.huhx.community.mapper.QuestionMapper;
import com.huhx.community.model.Question;
import com.huhx.community.model.User;
import com.huhx.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish")
    public String publish(){return "publish";}

    @PostMapping("/publish")
    public String doPublish(@RequestParam(name="title")String title,
                            @RequestParam(name="description") String description,
                            @RequestParam(name="tag") String tag,
                            @RequestParam(name="id", defaultValue = "") String id,
                            HttpServletRequest request,
                            Model model){
        //将传入的信息置入model用于回显数据
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);
        model.addAttribute("id", id);
        //判断用户填写的消息是否为空
        if(title == null || title == ""){
            model.addAttribute("error", "题目不能为空！");
            return "publish";
        }
        if(description == null || description == ""){
            model.addAttribute("error", "问题补充不能为空！");
            return "publish";
        }
        if(tag == null || tag == ""){
            model.addAttribute("error", "标签不能为空！");
            return "publish";
        }

        //将用户提交的问题存入数据库
        User user = (User)request.getSession().getAttribute("user");
        if(user == null){
            model.addAttribute("error", "用户未登陆");
            return "publish";
        }

        Question question = new Question();

        question.setTitle(title);
        question.setDescription(description);
        question.setCreator(user.getId());
        question.setTag(tag);

        if(id != null && !"".equals(id)){
            question.setId(Long.parseLong(id));
            questionService.updateOrInsertQuestion(question);
        }else{
            question.setGmtCreat(System.currentTimeMillis());
            question.setGmtModify(question.getGmtCreat());
            questionMapper.insertSelective(question);
        }
        return "redirect:/";
    }

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable("id")String id,
                       Model model){
        Question question = questionMapper.selectByPrimaryKey(Long.parseLong(id));
        if(question != null){
            model.addAttribute("title", question.getTitle());
            model.addAttribute("description", question.getDescription());
            model.addAttribute("tag", question.getTag());
            model.addAttribute("id", question.getId());
        }

        return "publish";
    }
}
