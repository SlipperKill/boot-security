package com.boot.security.server.model;

import java.math.BigDecimal;
import java.util.Date;

public class BusinessInfo extends BaseEntity<String> {

	private String businessCode;
	private String businessName;
	private String loginPassword;
	private String phoneAccount;
	private String phonePassword;
	private BigDecimal aliPayPoundage;
	private BigDecimal wechatPoundage;
	private BigDecimal minAmount;
	private BigDecimal maxAmount;

	public String getBusinessCode() {
		return businessCode;
	}
	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}
	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	public String getLoginPassword() {
		return loginPassword;
	}
	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}
	public String getPhoneAccount() {
		return phoneAccount;
	}
	public void setPhoneAccount(String phoneAccount) {
		this.phoneAccount = phoneAccount;
	}
	public String getPhonePassword() {
		return phonePassword;
	}
	public void setPhonePassword(String phonePassword) {
		this.phonePassword = phonePassword;
	}
	public BigDecimal getAliPayPoundage() {
		return aliPayPoundage;
	}
	public void setAliPayPoundage(BigDecimal aliPayPoundage) {
		this.aliPayPoundage = aliPayPoundage;
	}
	public BigDecimal getWechatPoundage() {
		return wechatPoundage;
	}
	public void setWechatPoundage(BigDecimal wechatPoundage) {
		this.wechatPoundage = wechatPoundage;
	}
	public BigDecimal getMinAmount() {
		return minAmount;
	}
	public void setMinAmount(BigDecimal minAmount) {
		this.minAmount = minAmount;
	}
	public BigDecimal getMaxAmount() {
		return maxAmount;
	}
	public void setMaxAmount(BigDecimal maxAmount) {
		this.maxAmount = maxAmount;
	}

}
