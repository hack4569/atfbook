package com.shsb.atfbook.application.login;

import com.shsb.atfbook.application.SessionConst;
import com.shsb.atfbook.application.member.MemberRepository;
import com.shsb.atfbook.domain.member.Member;
import com.shsb.atfbook.domain.member.MemberRegisterRequest;
import com.shsb.atfbook.domain.member.PasswordEncoder;
import com.shsb.atfbook.domain.shared.ScriptUtils;
import com.shsb.atfbook.domain.shared.SessionUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = {"/users"})
public class LoginController {
    private final LoginService loginService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                        LoginForm loginForm) {

        return "user/login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form,
                        BindingResult bindingResult,
                        HttpServletRequest request,
                        HttpServletResponse response) throws Exception{

        if (bindingResult.hasErrors()) {
            return "user/login";
        }
        Member loginMember = loginService.login(form.getLoginId());
        if (loginMember == null || !loginMember.checkPassword(form.getPassword(), passwordEncoder)) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            ScriptUtils.alert(response,"아이디 또는 비밀번호가 맞지 않습니다.");
            return "user/login";
        }
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        session.setMaxInactiveInterval(1800);
        SessionUtils.setMemberId(loginMember.getId());
        if (form.isAutoLogin()) {
            //세션 아이디 저장
            loginMember.setSessionId(session.getId());
            memberRepository.save(loginMember);
            //쿠키저장
            Cookie loginCookie = new Cookie(SessionConst.LOGIN_MEMBER, session.getId());
            loginCookie.setMaxAge(60*60*24*7);
            loginCookie.setPath("/");
            response.addCookie(loginCookie);
        }
        return "redirect:/";
    }
    @GetMapping("/join")
    public String join(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember
        , @ModelAttribute("form") MemberRegisterRequest form) {

        return "user/join";
    }

    @PostMapping("/join")
    public String joinAction(@Validated @ModelAttribute("form") MemberRegisterRequest form, BindingResult bindingResult, HttpServletResponse response) {
        try {
            if (!bindingResult.hasErrors()) {
                Member member = memberRepository.findMemberByLoginId(form.loginId()).orElse(null);
                if ( member != null) {
                    //ScriptUtils.alert(response, form.getLoginId() + " 아이디는 이미 존재합니다.");
                    bindingResult.reject("joinedMember");
                }
                if (!member.checkPassword(form.password(), passwordEncoder)) {
                    //ScriptUtils.alert(response, "비밀번호가 일치하지 않습니다.");
                    bindingResult.reject("passwordNotEquals");
                }
            }

            if (bindingResult.hasErrors()) {
                return "user/join";
            }

            Member member = Member.register(form, passwordEncoder);

            loginService.saveMember(member);
            //response.sendRedirect("/users/login");
            //ScriptUtils.alertAndRedirect(response, "회원가입이 완료되었습니다. 좋은 책 찾길 바랍니다^^", "/users/login");
        } catch (Exception e) {
            log.error("joinAction error = {}", e.getMessage(), e);
            return "user/join";
        }
        return "redirect:/users/login?msg=joinSuccessMsg";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        //기존에 세션이 있으면 가져오고, 없으면 생성하지 않음.
        HttpSession session = request.getSession(false);
        if (session != null) {
            Cookie loginCookie = WebUtils.getCookie(request, SessionConst.LOGIN_MEMBER);
            if (loginCookie != null) {
                loginCookie.setPath("/");
                loginCookie.setMaxAge(0);
                response.addCookie(loginCookie);
                Member member = memberRepository.findMemberBySessionId(session.getId()).orElse(null);
                if (member != null) {
                    member.setSessionId(null);
                    memberRepository.save(member);
                    session.invalidate();
                }
            }
        }
        return "redirect:/";
    }

}
