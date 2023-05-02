package org.droplet.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Pattern;
import org.droplet.Entity.ResultBean;
import org.droplet.service.AuthorizeService;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthorizeController {

    private final String EMAIL_REGEXP = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    private final String USERNAME_REGEXP = "^[\\u4E00-\\u9FA5A-Za-z0-9_]+$";

    @Autowired
    private AuthorizeService authorizeService;

    @PostMapping("/valid-email")
    public ResultBean<String> validateEmail(@Pattern(regexp = EMAIL_REGEXP)
                                            @RequestParam("email") String email, HttpSession session) {
        String resultStr = authorizeService.sendValidateEmail(email, session.getId());
        if (resultStr == null) {
            return ResultBean.success("邮件发送成功，请注意查收");
        } else {
            return ResultBean.failure(400, resultStr);
        }
    }

    @PostMapping("/register")
    public ResultBean<String> registerUser(@Pattern(regexp = USERNAME_REGEXP)
                                           @Length(min = 2, max = 8)
                                           @RequestParam("username") String username,
                                           @Length(min = 6, max = 16)
                                           @RequestParam("password") String password,
                                           @Pattern(regexp = EMAIL_REGEXP)
                                           @RequestParam("email") String email,
                                           @Length(min = 6, max = 6)
                                           @RequestParam("code") String code,
                                           HttpSession session) {
        String resultStr = authorizeService.validateAndRegister(username, password, email, code, session.getId());
        if (resultStr == null) {
            return ResultBean.success("注册成功");
        } else {
            return ResultBean.failure(400, resultStr);
        }
    }
}
