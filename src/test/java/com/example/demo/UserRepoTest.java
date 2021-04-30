package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.bookstore.domain.User;
import com.bookstore.repository.UserRepo;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepoTest {

	
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private UserRepo repo;
	
	@Test
	public void testCreateUser() {
		User user = new User();
		user.setEmailaddress("janeodum41@gmail.com");
		user.setPassword("odum2021");
		user.setFirstname("Jane");
		user.setLastname("Odum");
		user.setUsername("janeodum");
		user.setPhonenumber("234566890");
		User savedUser = new User();
		savedUser = repo.save(user);
		
		User existUser = entityManager.find(User.class, savedUser.getId());
		
		assertThat(user.getEmailaddress()).isEqualTo(existUser.getEmailaddress());
		
	}
	
	@Test
	public void testFindByEmail() {
		String email = "janeodum41@gmail.com";
		User user = repo.findByEmail(email);
		
		assertThat(user.getEmailaddress()).isEqualTo(email);
	}
}
