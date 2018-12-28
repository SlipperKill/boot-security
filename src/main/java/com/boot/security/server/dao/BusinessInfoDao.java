package com.boot.security.server.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.boot.security.server.model.BusinessInfo;

@Mapper
public interface BusinessInfoDao {

    @Select("select * from business_info t where t.id = #{id}")
    BusinessInfo getById(String id);

    @Delete("delete from business_info where id = #{id}")
    int delete(String id);

    int update(BusinessInfo businessInfo);
    
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into business_info(id,business_code, business_name, login_password, phone_account, phone_password, aliPay_poundage, wechat_poundage, min_amount, max_amount, create_time, update_time) values(#{id},#{businessCode}, #{businessName}, #{loginPassword}, #{phoneAccount}, #{phonePassword}, #{aliPayPoundage}, #{wechatPoundage}, #{minAmount}, #{maxAmount}, #{createTime}, #{updateTime})")
    int save(BusinessInfo businessInfo);
    
    int count(@Param("params") Map<String, Object> params);

    List<BusinessInfo> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset, @Param("limit") Integer limit);
}
