package org.droplet.service.impl;

import org.droplet.Entity.auth.Account;
import org.droplet.mapper.UserMapper;
import org.droplet.service.AuthorizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class AuthorizeServiceImpl implements AuthorizeService {

    @Value("${spring.mail.username}")
    String from;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    MailSender mailSender;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null) {
            throw new UsernameNotFoundException("用户名不能为空");
        }
        Account account = userMapper.findAccountByNameOrEmail(username);
        if (account == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        return User
                .withUsername(account.getUsername())
                .password(account.getPassword())
                .roles("user")
                .build();
    }

    @Override
    public String sendValidateEmail(String email, String sessionId, Boolean hasAccount) {
        String key = "email:" + sessionId + ":" + email + ":" + hasAccount;
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))) {
            Long expire = Optional.ofNullable(stringRedisTemplate.getExpire(key, TimeUnit.SECONDS)).orElse(0L);
            if (expire > 120) {
                return "请求过于频繁，请稍后再试";
            }
        }
        Account account = userMapper.findAccountByNameOrEmail(email);
        if (hasAccount && account == null) {
            return "不存在此    账户";
        }
        if (!hasAccount && account != null) {
            return "此邮箱已被注册";
        }
        Random random = new Random();
        int code = random.nextInt(899999) + 100000;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        message.setSubject("验证邮件");
        message.setText("验证码为：" + code);
        try {
            mailSender.send(message);
            stringRedisTemplate.opsForValue().set(key, String.valueOf(code), 3, TimeUnit.MINUTES);
            return null;
        } catch (MailException e) {
            e.printStackTrace();
            return "邮件发送失败，请检查邮件地址是否有效";
        }
    }

    @Override
    public String validateAndRegister(String username, String password, String email, String code, String sessionId) {
        String key = "email:" + sessionId + ":" + email + ":false";
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))) {
            String cachedCode = stringRedisTemplate.opsForValue().get(key);
            if (cachedCode == null) {
                return "验证码失效，请重新发送验证码";
            }
            if (cachedCode.equals(code)) {
                Account account = userMapper.findAccountByNameOrEmail(username);
                if (account != null) {
                    return "此用户名已被注册，请更换用户名";
                }
                stringRedisTemplate.delete(key);
                password = bCryptPasswordEncoder.encode(password);
                if (userMapper.createAccount(username, password, email) > 0) {
                    return null;
                } else {
                    return "内部错误，请联系管理员";
                }
            } else {
                return "验证码错误";
            }
        } else {
            return "请先发送验证码";
        }
    }

    @Override
    public String validaOnly(String email, String code, String sessionId) {
        String key = "email:" + sessionId + ":" + email + ":true";
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))) {
            String cachedCode = stringRedisTemplate.opsForValue().get(key);
            if (cachedCode == null) {
                return "验证码失效，请重新发送验证码";
            }
            if (cachedCode.equals(code)) {
                stringRedisTemplate.delete(key);
                return null;
            } else {
                return "验证码错误";
            }
        } else {
            return "请先发送验证码";
        }
    }

    @Override
    public Boolean resetPassword(String password, String email) {
        password = bCryptPasswordEncoder.encode(password);
        return userMapper.resetPasswordByEmail(password, email) > 0;
    }
}
