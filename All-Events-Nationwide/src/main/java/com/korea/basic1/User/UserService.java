package com.korea.basic1.User;

import com.korea.basic1.Category.Category;
import com.korea.basic1.DataNotFoundException;
import com.korea.basic1.Question.Question;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<SiteUser> getUserList(String role){
        if ("admin".equals(role) || "user".equals(role)) {
            List<String> roles = Arrays.asList("admin", "user");
            return userRepository.findAllByRoleIn(roles);
        } else {
            return Collections.emptyList();
        }
    }

    public SiteUser create(String userid, String nickname, String password, String email) {
        SiteUser user = new SiteUser();
        user.setUserid(userid);
        user.setNickname(nickname);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRole("user");
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

    public SiteUser getUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname);
    }

    public void delete(SiteUser siteUser) {
        this.userRepository.delete(siteUser);
    }

    @PostConstruct
    public void init() {
        saveDefaultAdmin();
    }


    public void saveDefaultAdmin() {
        if (userRepository.findByuserid("admin").isEmpty()) {
            SiteUser siteUser = new SiteUser();
            siteUser.setUserid("admin");
            siteUser.setRole("super_admin");
            siteUser.setPassword(passwordEncoder.encode("1234"));
            siteUser.setEmail("admin@naver.com");
            siteUser.setNickname("관리자");
            userRepository.save(siteUser);
        }
    }
}