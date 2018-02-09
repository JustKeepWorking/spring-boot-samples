package com.github.nduyhai.security.service;

import com.github.nduyhai.security.entity.UserEntity;
import com.github.nduyhai.security.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
    private static Logger LOG = LoggerFactory.getLogger(AuthenticationFailureListener.class);

    private UserRepository userRepository;

    public static final int MAX_ATTEMPT = 2;

    public AuthenticationFailureListener(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        final WebAuthenticationDetails auth = (WebAuthenticationDetails) event.getAuthentication().getDetails();

        final String username = event.getAuthentication().getName();
        LOG.info("{} login fail from {}", username, auth.getRemoteAddress());

        final UserEntity userEntity = this.userRepository.findOne(username);
        if (userEntity != null) {
            final Integer loginAttempt = userEntity.getLoginAttempt();
            userEntity.setLoginAttempt(loginAttempt == null ? 1 : loginAttempt + 1);

            if (userEntity.getLoginAttempt() >= MAX_ATTEMPT) {
                userEntity.setAccountNonLocked(false);
            }
            this.userRepository.save(userEntity);
        }
    }
}
