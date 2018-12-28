package com.boot.security.server.model;

import java.math.BigDecimal;
import java.util.Date;

public class CodeMan extends BaseEntity<String> {

    private String manCode;
    private String manName;
    private String phoneAccount;
    private String phonePassword;
    private BigDecimal creditQuota;
    private BigDecimal surplusQuota;
    private BigDecimal receivables;
    private String businessCode;
    private Integer status;

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

    public String getManCode() {
        return manCode;
    }

    public void setManCode(String manCode) {
        this.manCode = manCode;
    }

    public String getManName() {
        return manName;
    }

    public void setManName(String manName) {
        this.manName = manName;
    }

    public BigDecimal getCreditQuota() {
        return creditQuota;
    }

    public void setCreditQuota(BigDecimal creditQuota) {
        this.creditQuota = creditQuota;
    }

    public BigDecimal getSurplusQuota() {
        return surplusQuota;
    }

    public void setSurplusQuota(BigDecimal surplusQuota) {
        this.surplusQuota = surplusQuota;
    }

    public BigDecimal getReceivables() {
        return receivables;
    }

    public void setReceivables(BigDecimal receivables) {
        this.receivables = receivables;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
