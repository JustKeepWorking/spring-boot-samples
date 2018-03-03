package com.github.nduyhai.security.service;

import com.github.nduyhai.security.entity.AuthorityEntity;
import com.github.nduyhai.security.entity.UserEntity;
import com.github.nduyhai.security.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class InMemoryUserDetailService implements UserDetailsService {

    private UserRepository userRepository;

    public InMemoryUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        final PasswordEncoder encoder = new BCryptPasswordEncoder(10);
        final AuthorityEntity adminAuthority = new AuthorityEntity();
        {
            adminAuthority.setAuthority("ROLE_ADMIN");
        }

        final UserEntity admin = new UserEntity();
        {
            admin.setUsername("admin");
            admin.setPassword(encoder.encode("admin"));
            admin.setAccountNonExpired(true);
            admin.setAccountNonLocked(true);
            admin.setCredentialsNonExpired(true);
            admin.setEnabled(true);
            admin.setAuthorities(new LinkedList<AuthorityEntity>() {{
                add(adminAuthority);
            }});
        }
        this.userRepository.save(admin);

        final AuthorityEntity userAuthority = new AuthorityEntity();
        {
            userAuthority.setAuthority("ROLE_ADMIN");
        }

        final UserEntity user = new UserEntity();
        {
            user.setUsername("user");
            user.setPassword(encoder.encode("user"));
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setEnabled(true);
            user.setAuthorities(new LinkedList<AuthorityEntity>() {{
                add(userAuthority);
            }});
        }
        this.userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Optional<UserEntity> optionalEntity = this.userRepository.findById(username);
        if (optionalEntity.isPresent()) {
            final UserEntity user = optionalEntity.get();
            return new User(user.getUsername(), user.getPassword(), user.isEnabled(), user.isAccountNonExpired(), user.isCredentialsNonExpired(), user.isAccountNonLocked(), this.getGrantedAuthority(user.getAuthorities()));
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    private List<GrantedAuthority> getGrantedAuthority(List<AuthorityEntity> authorities) {
        final List<GrantedAuthority> grantedAuthorities = new LinkedList<>();
        if (authorities != null) {
            authorities.forEach(e -> grantedAuthorities.add(new SimpleGrantedAuthority(e.getAuthority())));
        }
        return grantedAuthorities;
    }
}
