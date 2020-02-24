package com.huhx.community.service;

import com.huhx.community.mapper.UserMapper;
import com.huhx.community.model.User;
import com.huhx.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public void creatorUpdate(User user) {
        UserExample example = new UserExample();
        example.createCriteria().andAccountIdEqualTo(user.getAccountId());
        List<User> dbUsers = userMapper.selectByExample(example);
        if(dbUsers.size() != 0){
            //找到了，则更新用户信息
            User dbUser = dbUsers.get(0);

            User updateUser = new User();
            updateUser.setName(user.getName());
            updateUser.setToken(user.getToken());
            updateUser.setAvatarUrl(user.getAvatarUrl());
            updateUser.setGmtModify(System.currentTimeMillis());

            UserExample example1 = new UserExample();
            example1.createCriteria().andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(updateUser, example1);
        }else{
            //没有找到，则直接插入
            user.setGmtCreat(System.currentTimeMillis());
            user.setGmtModify(user.getGmtCreat());
            userMapper.insert(user);
        }
    }
}
