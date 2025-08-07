package org.baseball.interceptor;

import org.baseball.dto.UserDTO;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);

        // 먼저 null 체크를 하고 나서 loginUser를 꺼내야 안전함
        if (session == null || session.getAttribute("loginUser") == null) {
            String requestType = request.getHeader("X-Requested-With");

            if ("XMLHttpRequest".equals(requestType)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setHeader("Location", "/reservation/errors/needLogin");
            } else {
                response.sendRedirect("/reservation/errors/needLogin");
            }
            return false;
        }

        return true;
    }
}
