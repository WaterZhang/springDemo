package com.zhangzemiao.www.springdemo.domain.service;

import com.zhangzemiao.www.springdemo.domain.valueobject.Contributor;
import java.util.List;

public interface IGithubService {

    List<Contributor> getContributor(String owner, String repo);

}
