package com.zhangzemiao.www.springdemo.domain.service.impl;

import com.zhangzemiao.www.springdemo.domain.client.GithubClient;
import com.zhangzemiao.www.springdemo.domain.service.IGithubService;
import com.zhangzemiao.www.springdemo.domain.valueobject.Contributor;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GithubServiceImpl implements IGithubService {

    private final GithubClient client;

    @Autowired
    public GithubServiceImpl(GithubClient client){
        this.client = client;
    }

    @Override
    public List<Contributor> getContributor(String owner, String repo) {
        try {
            return client.getContributors(owner, repo);
        } catch (Exception ex){
            //Todo
            ex.printStackTrace();
            return null;
        }
    }
}
