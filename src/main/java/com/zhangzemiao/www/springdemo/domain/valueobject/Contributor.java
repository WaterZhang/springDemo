package com.zhangzemiao.www.springdemo.domain.valueobject;

public class Contributor {

    private final String login;
    private final long contributions;

    public Contributor(final String login, final long contributions){
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
