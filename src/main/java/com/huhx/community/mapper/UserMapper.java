package com.huhx.community.mapper;

import com.huhx.community.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UserMapper {
    @Insert("insert into user (name, account_id, token, gmt_creat, gmt_modify, avatar_url) values(#{name}, #{accountID}, #{token}, #{gmtCreat}, #{gmtModify}, #{avatarUrl})")
    void insert(User user);

    @Select("select * from user where token = #{token}")
    User findByToken(@Param("token")String token);

    @Select("select * from user where id = #{id}")
    @Results({
            @Result(column = "account_id", property = "accountID"),
            @Result(column = "gmt_creat", property = "gmtCreat"),
            @Result(column = "gmt_modify", property = "gmtModify"),
            @Result(column = "avatar_url", property = "avatarUrl"),
    })
    User findById(@Param("id")int id);
}
