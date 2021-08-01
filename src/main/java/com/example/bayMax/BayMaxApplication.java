package com.example.bayMax;

import com.example.bayMax.Domain.Roles;
import com.example.bayMax.Domain.Users;
import com.example.bayMax.Infrastructure.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Date;

@SpringBootApplication
public class BayMaxApplication implements CommandLineRunner {

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	UserRepository userRepository;

	public static void main(String[] args) {

		SpringApplication.run(BayMaxApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		Date date = new Date(100,10,1);
		Users newUser = new Users("firstname","lastname",date,"Amman, Jordan","A++", 999999L,"admin",bCryptPasswordEncoder.encode("password"));
		newUser.addRole(new Roles("ADMIN"));
		newUser = userRepository.save(newUser);

	}
}
