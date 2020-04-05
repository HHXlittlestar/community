package com.huhx.community.controller;

import com.huhx.community.dto.CommentCreateDTO;
import com.huhx.community.dto.CommentDTO;
import com.huhx.community.dto.ResultDTO;
import com.huhx.community.enums.CommentTypeEnum;
import com.huhx.community.exception.CustomizeErrorCode;
import com.huhx.community.model.Comment;
import com.huhx.community.model.User;
import com.huhx.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;
    @ResponseBody
    @RequestMapping(value ="/comment", method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        if (commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())){
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }
        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtModify(System.currentTimeMillis());
        comment.setGmtCreat(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0L);
        commentService.insert(comment, user);
        return ResultDTO.okOf();
    }
    @ResponseBody
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.GET)
    public ResultDTO subComment(@PathVariable("id")Long id){
        List<CommentDTO> comments = commentService.findAllComments(id, CommentTypeEnum.COMMENT);
        return ResultDTO.okOf(comments);
    }
}
