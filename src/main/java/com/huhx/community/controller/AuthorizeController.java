package com.huhx.community.controller;

import com.huhx.community.dto.AccessTokenDTO;
import com.huhx.community.dto.GithubUser;
import com.huhx.community.mapper.UserMapper;
import com.huhx.community.model.User;
import com.huhx.community.provider.GithubProvider;
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
    private UserMapper userMapper;
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

        if(githubUser != null){
            User user = new User();
            user.setName(githubUser.getName());
            user.setAccountID(String.valueOf(githubUser.getID()));
            user.setToken(UUID.randomUUID().toString());
            user.setGmtCreat(System.currentTimeMillis());
            user.setGmtModify(user.getGmtCreat());
            userMapper.insert(user);
            response.addCookie(new Cookie("token", user.getToken()));
            return "redirect:/";
        }else{
            return "redirect:/";
        }
    }
}
