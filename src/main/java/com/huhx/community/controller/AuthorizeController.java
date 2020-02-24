package com.huhx.community.controller;

import com.huhx.community.dto.AccessTokenDTO;
import com.huhx.community.dto.GithubUser;
import com.huhx.community.model.User;
import com.huhx.community.provider.GithubProvider;
import com.huhx.community.service.UserService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    @Autowired
    private AccessTokenDTO accessTokenDTO;
    @Autowired
    private UserService userService;

    @Value("${github.client.id}")
    private String clientID;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectURI;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletResponse response){
        HashMap<String, String> map = new HashMap<>();
        map.put("client_id", clientID);
        map.put("client_secret", clientSecret);
        map.put("code", code);
        map.put("redirect_uri", redirectURI);
        map.put("state", state);
        try {
            BeanUtils.populate(accessTokenDTO, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);

        //如果返回的User信息不为空，则先检查数据库中是否存在该用户信息，account_id是用户的唯一标识
        //如果存在，则将用户的信息更新，如果不存在则插入
        if(githubUser != null){
            User user = new User();
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getID()));
            user.setToken(UUID.randomUUID().toString());
            user.setAvatarUrl(githubUser.getAvatarUrl());
            userService.creatorUpdate(user);
            response.addCookie(new Cookie("token", user.getToken()));
            return "redirect:/";
        }else{
            return "redirect:/";
        }
    }
}
