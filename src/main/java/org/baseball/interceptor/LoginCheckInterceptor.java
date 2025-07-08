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
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");

        if (loginUser == null) {
            String requestType = request.getHeader("X-Requested-With");

            if("XMLHttpRequest".equals(requestType)) {
                //ajax 요청일 경우(return type: JSON)
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
