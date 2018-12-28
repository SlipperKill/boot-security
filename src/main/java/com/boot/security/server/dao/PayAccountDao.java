package com.boot.security.server.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.boot.security.server.model.PayAccount;

@Mapper
public interface PayAccountDao {

    @Select("select * from pay_account t where t.id = #{id}")
    PayAccount getById(String id);

    @Delete("delete from pay_account where id = #{id}")
    int delete(String id);

    int update(PayAccount payAccount);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into pay_account(id,pay_account, account_type, code_man, user_name, status, online_time, offline_time, level, create_time) values(#{id},#{payAccount}, #{accountType}, #{codeMan}, #{userName}, #{status}, #{onlineTime}, #{offlineTime}, #{level}, #{createTime})")
    int save(PayAccount payAccount);

    int count(@Param("params") Map<String, Object> params);

    List<PayAccount> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset, @Param("limit") Integer limit);

    @Select("select * from pay_account t where t.pay_account = #{account} and t.account_type = #{accountType}")
    PayAccount getByAccount(@Param("account") String account,@Param("accountType") String accoutType);

    @Select("select * from pay_account t where t.code_man = #{codeMan}")
    PayAccount getByCodeMan(@Param("codeMan") String codeMan);
}
