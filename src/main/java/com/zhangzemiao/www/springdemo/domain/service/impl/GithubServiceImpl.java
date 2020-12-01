package com.zhangzemiao.www.springdemo.domain.service.impl;

import com.zhangzemiao.www.springdemo.domain.client.GithubClient;
import com.zhangzemiao.www.springdemo.domain.service.IGithubService;
import com.zhangzemiao.www.springdemo.domain.valueobject.ContributorsRs;
import com.zhangzemiao.www.springdemo.domain.valueobject.ErrorDetails;
import com.zhangzemiao.www.springdemo.log.SpringLogger;
import com.zhangzemiao.www.springdemo.log.SystemEvent;
import com.zhangzemiao.www.springdemo.log.SystemEventData;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GithubServiceImpl implements IGithubService {

    private static final SpringLogger LOGGER = SpringLogger.getLogger(GithubServiceImpl.class);
    private final GithubClient client;

    @Autowired
    public GithubServiceImpl(GithubClient client){
        this.client = client;
    }

    @Override
    public ContributorsRs getContributor(String owner, String repo) {
        SystemEventData systemEventData = new SystemEventData()
                                              .with("owner", owner)
                                              .with("repo", repo);
        try {
            return new ContributorsRs(client.getContributors(owner, repo));
        } catch (Exception ex){
            LOGGER.error(SystemEvent.GITHUB_CONTRIBUTORS_ERROR, ExceptionUtils.getStackTrace(ex), systemEventData);
            return new ContributorsRs(new ErrorDetails(SystemEvent.GITHUB_CONTRIBUTORS_ERROR.getId(),
                                                       SystemEvent.GITHUB_CONTRIBUTORS_ERROR.getDescription()));
        }
    }
}
