package com.korea.basic1.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
    Optional<SiteUser> findByuserid(String userid);
//    Optional<SiteUser> findByuserNickname(String nickname);
}
