package com.korea.basic1.User;

import com.korea.basic1.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SiteUser create(String userid, String nickname, String password, String email) {
        SiteUser user = new SiteUser();
        user.setUserid(userid);
        user.setNickname(nickname);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        this.userRepository.save(user);
        return user;
    }

    public SiteUser getUser(String userid) {
        Optional<SiteUser> siteUser = this.userRepository.findByuserid(userid);
        if (siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
    }
}