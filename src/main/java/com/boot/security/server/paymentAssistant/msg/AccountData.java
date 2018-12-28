package com.boot.security.server.paymentAssistant.msg;

public class AccountData {

    private String account;

    private String accuntType;

    private String userName;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccuntType() {
        return accuntType;
    }

    public void setAccuntType(String accuntType) {
        this.accuntType = accuntType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
