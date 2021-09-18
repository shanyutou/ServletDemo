package com.shanyutou.servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

public class HelloWorld extends HttpServlet {

    private String message;

    @Override
    public void init() throws ServletException {
        message = "Hello World";
        super.init();
        System.out.println("init");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<h1>" + message + "</h1>");
    }

    @Override
    public void destroy() {
        System.out.println("destory");
        super.destroy();
    }
}
