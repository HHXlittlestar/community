package com.huhx.community.controller;

import com.huhx.community.pojo.AccessTokenDTO;
import com.huhx.community.pojo.GithubUser;
import com.huhx.community.provider.GithubProvider;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    @Autowired
    private AccessTokenDTO accessTokenDTO;
    @Value("${github.client.id}")
    private String clientID;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectURI;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state){
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
        GithubUser user = githubProvider.getUser(accessToken);
        System.out.println(user.getName());
        return "index";
    }
}
