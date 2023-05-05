package org.droplet.controller;

import org.droplet.Entity.ResultBean;
import org.droplet.Entity.user.AccountUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/info")
    public ResultBean<AccountUser> info(@SessionAttribute("account") AccountUser accountUser) {
        return ResultBean.success(accountUser);
    }
}
