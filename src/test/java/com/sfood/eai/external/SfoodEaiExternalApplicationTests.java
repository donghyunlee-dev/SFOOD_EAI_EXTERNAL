package com.sfood.eai.external;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(initializers = ConfigDataApplicationContextInitializer.class)
@ActiveProfiles("test")
class SfoodEaiExternalApplicationTests {

	@Autowired
	Environment env;

	void contextLoads() {
	}

	@Test
	void printActiveProfiles() {
		System.out.println("Active Profiles: " + Arrays.toString(env.getActiveProfiles()));
		System.out.println("Port : " + env.getProperty("server.port"));
	}
}
