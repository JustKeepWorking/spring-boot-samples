package com.github.nduyhai.security.service;

import com.github.nduyhai.security.entity.UserEntity;
import com.github.nduyhai.security.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {
    private static Logger LOG = LoggerFactory.getLogger(AuthenticationSuccessEventListener.class);

    private UserRepository userRepository;

    public AuthenticationSuccessEventListener(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        WebAuthenticationDetails auth = (WebAuthenticationDetails) event.getAuthentication().getDetails();
        final String username = event.getAuthentication().getName();

        LOG.info("{} login success from {}", username, auth.getRemoteAddress());
        final UserEntity userEntity = this.userRepository.findOne(username);

        if (userEntity.getLoginAttempt() != null && userEntity.getLoginAttempt() != 0) {
            userEntity.setLoginAttempt(0);
            this.userRepository.save(userEntity);
        }
    }
}
