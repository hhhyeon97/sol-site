package com.sol.shop.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

public class Util {

    // 사용자가 요청한 이전 페이지의 URL을 가져오는 메서드
    public static String getPreviousPageUrl(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            SavedRequest savedRequest = (SavedRequest) session.getAttribute("SPRING_SECURITY_SAVED_REQUEST");
            if (savedRequest != null) {
                return savedRequest.getRedirectUrl();
            }
        }
        return null;
    }

    // 이전 페이지의 URL을 세션에 저장하는 메서드
    public static void savePreviousPageUrl(HttpServletRequest request, HttpServletResponse response) {
        HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
        requestCache.saveRequest(request, response);
    }
}


