package com.shsb.atfbook.application.like;

import com.shsb.atfbook.application.SessionConst;
import com.shsb.atfbook.application.member.MemberRepository;
import com.shsb.atfbook.application.recommend.argumentresolver.Login;
import com.shsb.atfbook.application.user.LoginForm;
import com.shsb.atfbook.application.user.LoginService;
import com.shsb.atfbook.domain.like.Like;
import com.shsb.atfbook.domain.member.Member;
import com.shsb.atfbook.domain.member.MemberRegisterRequest;
import com.shsb.atfbook.domain.member.PasswordEncoder;
import com.shsb.atfbook.domain.shared.ScriptUtils;
import com.shsb.atfbook.domain.shared.SessionUtils;
import com.shsb.atfbook.domain.shared.SpringUtils;
import com.sun.net.httpserver.HttpsServer;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = {"/like"})
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/{itemId}")
    @ResponseBody
    public ResponseEntity likeAction(@Login Member member, @PathVariable("itemId") int itemId) {
        if (member == null) return new ResponseEntity(HttpStatus.BAD_REQUEST);
        likeService.like(Like.create(itemId, member.getLoginId()));
        return ResponseEntity.ok(new LikeResponse(true));
    }
}
