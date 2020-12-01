package com.zhangzemiao.www.springdemo.controllers;

import com.zhangzemiao.www.springdemo.domain.service.IGithubService;
import com.zhangzemiao.www.springdemo.domain.valueobject.Contributor;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GithubDataController {

    private IGithubService githubService;

    @Autowired
    public GithubDataController(IGithubService githubService){
        this.githubService = githubService;
    }

    @GetMapping("/api/github/{owner}/{repo}/contributors")
    @ApiOperation(value = "greeting")
    List<Contributor> getContributors(@PathVariable("owner") String owner,
                                      @PathVariable("repo") String repo){
        return githubService.getContributor(owner, repo);
    }

}
