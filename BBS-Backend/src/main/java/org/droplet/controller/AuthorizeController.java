package org.droplet.controller;

import jakarta.validation.constraints.Pattern;
import org.droplet.Entity.ResultBean;
import org.droplet.service.AuthorizeService;
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

    private final String EMAIL_REGEXP = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$\n";

    @Autowired
    private AuthorizeService authorizeService;

    @PostMapping("/valid-email")
    public ResultBean<String> validateEmail(@Pattern(regexp = EMAIL_REGEXP)
                                            @RequestParam("email") String email) {
        if (authorizeService.sendValidateEmail(email)) {
            return ResultBean.success("邮件发送成功，请注意查收");
        } else {
            return ResultBean.failure(400, "邮件发送失败，请联系管理员");
        }
    }
}
