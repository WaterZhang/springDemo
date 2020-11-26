package com.zhangzemiao.www.springdemo.servlet;

import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "buildInfo", value = "/buildInfo", loadOnStartup = 1)
public class BuildInfoServlet extends HttpServlet {

    private static final String JSON_RESPONSE =
        "{ \"version\": \"%s\", \"buildTime\": \"%s\", \"branch\": \"%s\", \"applicationVersion\": \"%s\", \"name\": \"%s\", \"bomVersion\":\"%s\", \"parentArtifact\":\"%s\", \"parentVersion\":\"%s\",\"bomInfo\":\"%s\"}";

    public BuildInfoServlet(){
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-cache,no-store,max-age=0");
        response.setStatus(200);
        response.getWriter().write(getJsonResponse());
    }

    protected String getJsonResponse() {
        return String.format(JSON_RESPONSE, "test", new Date().toString(),
                             "test", "test",
                             "test", "test",
                             "test", "test",
                             "test");
    }


}
