package com.huhx.community.mapper;

import com.huhx.community.model.Question;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface QuestionMapper {
    @Insert("insert into question(title, description, gmt_creat, gmt_modify, creator, tag) values(#{title}, #{description}, #{gmtCreat}, #{gmtModify}, #{creator}, #{tag})")
    void insert(Question question);

    @Select("select * from question limit #{startIndex}, #{range}")
    @Results()
    List<Question> findByPage(@Param("startIndex")int startIndex, @Param("range")int range);

    @Select("select * from question")
    @Results(id = "map",value = {
            @Result(column = "gmt_creat", property = "gmtCreat"),
            @Result(column = "gmt_modify", property = "gmtModify"),
            @Result(column = "view_count", property = "viewCount"),
            @Result(column = "comment_count", property = "commentCount"),
            @Result(column = "like_count", property = "likeCount"),
    })
    List<Question> findAll();
}
