package com.zhangzemiao.www.springdemo.domain.service;

import com.zhangzemiao.www.springdemo.domain.valueobject.Contributor;
import com.zhangzemiao.www.springdemo.domain.valueobject.ContributorsRs;
import java.util.List;

public interface IGithubService {

    ContributorsRs getContributor(String owner, String repo);

}
