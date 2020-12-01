package com.zhangzemiao.www.springdemo.domain.valueobject;

public class Contributor {

    private String login;
    private long contributions;

    public Contributor(String login, long contributions){
        this.login = login;
        this.contributions = contributions;
    }

    public String getLogin() {
        return login;
    }

    public long getContributions() {
        return contributions;
    }
}
