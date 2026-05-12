package com.example.VideoRent.config;

import com.example.VideoRent.entity.Role;
import com.example.VideoRent.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = (User) request.getSession().getAttribute("currentUser");
        if (user == null || user.getRole() != Role.ADMIN) {
            response.sendRedirect("/");
            return false;
        }
        return true;
    }
}
