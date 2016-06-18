package io.honeymon.springboot.totp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringSecurity2stepVerificationApplication.class)
@WebAppConfiguration
public class SpringSecurity2stepVerificationApplicationTests {

	@Test
	public void contextLoads() {
	}

}
