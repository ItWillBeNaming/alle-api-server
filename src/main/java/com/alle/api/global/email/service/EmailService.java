package com.alle.api.global.email.service;

import com.alle.api.domain.member.constant.RoleType;
import com.alle.api.domain.member.domain.Member;
import com.alle.api.domain.member.service.MemberService;
import com.alle.api.global.exception.custom.EmailException;
import com.alle.api.global.exception.ExceptionCode;
import com.alle.api.global.redis.service.RedisService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.ServletContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@EnableAsync
@RequiredArgsConstructor
@Transactional
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final ServletContext servletContext;
    private final RedisService redisService;

    @Async
    public void sendAuthCode(String toEmail) {
        String authCode = createRandomCode();
        saveAuthCode(toEmail, authCode);
        sendEmail(toEmail, authCode, "이메일 인증 코드", "authCode.html");
    }

    private void saveAuthCode(String email, String authCode) {
        redisService.setData(authCode, email, 5L, TimeUnit.MINUTES); // 5분 설정
    }

    @Async
    public void sendEmail(String toEmail, String code, String subject, String templateName) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try{
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setFrom("tjsfhdhkd@naver.com");
            helper.setSubject(subject);
            helper.setText(servletContext(code,templateName), true);

            javaMailSender.send(mimeMessage);
        }catch (MessagingException e){
            throw new EmailException(ExceptionCode.INVALID_AUTH_CODE);
        }

    }

    public String servletContext(String code, String templateName) {
        Context context = new Context();
        log.info(code);

        context.setVariable("code",code);
        return templateEngine.process(templateName, context);
    }

    public String createRandomCode() {
        Random random = new Random();
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            switch (random.nextInt(3)) {
                case 0:
                    key.append((char) ((int)random.nextInt(26)+97));
                    break;
                case 1:
                    key.append((char)((int)random.nextInt(26)+65));
                    break;
                case 2:
                    key.append(random.nextInt(10));
                    break;
            }
        }
        return key.toString();
    }


    public void validateMember(String email) {
        if (!isMemberExist(email)) {
            throw new EmailException(ExceptionCode.NOT_FOUND_MEMBER);
        }
    }

    public boolean isMemberExist(String email) {
        return(memberService.getMemberByLoginIdAndRole(email, RoleType.MEMBER_NORMAL)) != null;
    }



    public void validateAuthCode(String email, String authCode) {
        findByEmailAndAuthCode(authCode)
                .filter(e -> e.equals(email))
                .orElseThrow(() -> new EmailException(ExceptionCode.INVALID_AUTH_CODE));
    }

    private Optional<String> findByEmailAndAuthCode(String authCode) {
        Object email = redisService.getData(authCode);
        return Optional.ofNullable(email != null ? email.toString() : null);
    }

    @Async
    public void sendTemporaryPassword(String toEmail) {
        String tempPassword = createRandomCode();
        sendEmail(toEmail, tempPassword, "임시 비밀번호", "tempPassword.html");

        Member member = memberService.getMemberByEmailAndRole(toEmail, RoleType.MEMBER_NORMAL);
        member.updatePassword(passwordEncoder.encode(tempPassword));
    }
}

