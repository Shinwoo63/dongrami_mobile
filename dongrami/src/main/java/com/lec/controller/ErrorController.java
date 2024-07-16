package com.lec.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String errorMsg = "알 수 없는 오류가 발생했습니다.";
        int statusCode = 500; // 기본값 설정

        if (status != null) {
            statusCode = Integer.valueOf(status.toString());
            HttpStatus httpStatus = HttpStatus.valueOf(statusCode);

            errorMsg = switch (httpStatus) {
                case NOT_FOUND ->
                    String.format("요청하신 페이지를 찾을 수 없습니다.", statusCode);
                case INTERNAL_SERVER_ERROR ->
                    String.format("서버 내부 오류가 발생했습니다.", statusCode);
                case FORBIDDEN ->
                    String.format("접근이 거부되었습니다.", statusCode);
                case BAD_REQUEST ->
                    String.format("잘못된 요청입니다.", statusCode);
                case UNAUTHORIZED ->
                    String.format("인증이 필요합니다.", statusCode);
                case METHOD_NOT_ALLOWED ->
                    String.format("허용되지 않은 메소드입니다.", statusCode);
                default ->
                    String.format("오류가 발생했습니다.", statusCode);
            };
        }

        System.out.println("에러 메시지: " + errorMsg);
        System.out.println("상태 코드: " + statusCode);

        model.addAttribute("errorMsg", errorMsg);
        model.addAttribute("statusCode", statusCode);
        
        return "error"; // error.html 템플릿을 사용
    }
}
