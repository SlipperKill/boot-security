package com.boot.security.server.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.boot.security.server.model.CodeMan;

@Mapper
public interface CodeManDao {

    @Select("select * from code_man t where t.id = #{id}")
    CodeMan getById(String id);

    @Delete("delete from code_man where id = #{id}")
    int delete(String id);

    int update(CodeMan codeMan);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into code_man(id,man_code, man_name,phone_account,phone_password credit_quota, surplus_quota, receivables, business_code, status, create_time, update_time) values(#{id},#{manCode}, #{manName},#{phoneAccount},#{phonePassword}, #{creditQuota}, #{surplusQuota}, #{receivables}, #{businessCode}, #{status}, #{createTime}, #{updateTime})")
    int save(CodeMan codeMan);

    int count(@Param("params") Map<String, Object> params);

    List<CodeMan> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset, @Param("limit") Integer limit);

    @Select("select * from code_man t where t.phone_account=#{account} and t.phone_password=#{password}")
    CodeMan login(@Param("account")String account, @Param("password")String password);
}
