package com.zhangzemiao.www.springdemo.domain.service;

import com.zhangzemiao.www.springdemo.domain.valueobject.ContributorsRs;

public interface IGithubService {

    ContributorsRs getContributor(String owner, String repo);

}
