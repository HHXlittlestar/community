package com.huhx.community.mapper;

import com.huhx.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UserMapper {
    @Insert("insert into user (name, account_id, token, gmt_creat, gmt_modify) values(#{name}, #{accountID}, #{token}, #{gmtCreat}, #{gmtModify})")
    void insert(User user);

    @Select("select * from user where token = #{token}")
    User findByToken(String token);
}
