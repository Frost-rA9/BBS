package org.droplet.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public interface AuthorizeService extends UserDetailsService {
    String sendValidateEmail(String email, String sessionId, Boolean hasAccount);

    String validateAndRegister(String username, String password, String email, String code, String sessionId);

    String validaOnly(String email, String code, String sessionId);

    Boolean resetPassword(String password,String email);
}
