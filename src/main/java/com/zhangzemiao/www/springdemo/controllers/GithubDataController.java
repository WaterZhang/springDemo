package com.zhangzemiao.www.springdemo.controllers;

import com.zhangzemiao.www.springdemo.domain.mapper.ResponseEntityMapper;
import com.zhangzemiao.www.springdemo.domain.service.IGithubService;
import com.zhangzemiao.www.springdemo.domain.valueobject.IResponseMessage;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GithubDataController {

    private IGithubService githubService;
    private ResponseEntityMapper responseEntityMapper;

    @Autowired
    public GithubDataController(final IGithubService githubService,
                                final ResponseEntityMapper responseEntityMapper){
        this.githubService = githubService;
        this.responseEntityMapper = responseEntityMapper;
    }

    @GetMapping("/api/github/{owner}/{repo}/contributors")
    @ApiOperation(value = "get github contributors")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success", response = IResponseMessage.class),
        @ApiResponse(code = 500, message = "Error", response = IResponseMessage.class)})
    public ResponseEntity<IResponseMessage> getContributors(@PathVariable("owner") final String owner,
                                                            @PathVariable("repo") final String repo){
        return responseEntityMapper.mapWithRequestId(githubService.getContributor(owner, repo));
    }

}
