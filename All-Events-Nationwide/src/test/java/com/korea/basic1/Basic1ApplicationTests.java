package com.korea.basic1;

import org.apache.catalina.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Basic1ApplicationTests {
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
//	private ManagerRepository managerRepository;

	@Test
	void contextLoads() {
//		ManagerController m = new ManagerController();
//		m.setAdminId("deka2904");
//		m.setPassword(passwordEncoder.encode("cyj2512147!"));
//		this.managerRepository.save(m);
	}
}
