package com.multilang.memo.controller;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/session")
public class SessionController {
    //@Value("${cookie.secure:false}")
    //private boolean cookieSecure;

    //private final UserRepository userRepository; // 他の依存関係があれば

    // コンストラクタ
    //public <UserRepository> SessionController(UserRepository userRepository) {
    //    this.userRepository = userRepository;
    //}

    @PostMapping("/init")
    public ResponseEntity<String> initSession(
            @CookieValue(value = "user_id", required = false) String existingUserId,
            HttpServletResponse response) {

        if (existingUserId != null) {
            return ResponseEntity.ok(existingUserId); // 既存ユーザー
        }

        // 新規ユーザー
        String userId = UUID.randomUUID().toString();

        ResponseCookie cookie = ResponseCookie.from("user_id", userId)
                .httpOnly(true)
                .secure(false)   // ローカル開発用（本番ではtrue）
                .path("/")
                .maxAge(365 * 24 * 60 * 60)  // 1年
                .sameSite("Lax")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
        return ResponseEntity.ok(userId);
    }

}
