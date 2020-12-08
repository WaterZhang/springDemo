package com.zhangzemiao.www.springdemo.domain.valueobject;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContributorsRs implements IResponseMessage<List<Contributor>>{

    private List<Contributor> response;
    private ErrorDetails errorDetails;

    public ContributorsRs(final List<Contributor> contributors){
        this.response = contributors;
    }

    public ContributorsRs(final ErrorDetails errorDetails){
        this.errorDetails = errorDetails;
    }

    @Override
    public List<Contributor> getResponse() {
        return this.response;
    }

    @Override
    public ErrorDetails getErrorDetails() {
        return this.errorDetails;
    }

    @Override
    public boolean isSuccessful() {
        return errorDetails == null;
    }
}
