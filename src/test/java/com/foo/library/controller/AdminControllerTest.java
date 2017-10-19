package com.foo.library.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.HandlerInterceptor;

import com.foo.library.config.AdminInterceptor;
import com.foo.library.model.User;
import com.foo.library.service.LibraryService;

@WebMvcTest(AdminController.class)
@RunWith(SpringRunner.class)
public class AdminControllerTest {
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private LibraryService libraryService;

	@Test
	public void testForNotLoggedInCall() throws Exception{
		MvcResult mvcResult = mockMvc.perform(get("/admin")).andDo(print())
		.andExpect(status().is3xxRedirection())
		.andExpect(flash().attribute("loginError", equalTo("Please enter admin credentials to continue")))
		.andExpect(redirectedUrl("/login"))
		.andReturn();
		assertNotNull(mvcResult);
		List<HandlerInterceptor> interceptors = Arrays.asList(mvcResult
				.getInterceptors());
		assertTrue(
				"Expecting login interceptor in this path for cases where there is no logged in user",
				interceptors.stream().anyMatch(
						i -> i.getClass().equals(AdminInterceptor.class)));
	}
	
	@Test
	public void testForLoggedInCall() throws Exception {
		User user = new User();
		user.setId("admin");
		MvcResult mvcResult = mockMvc.perform(get("/admin").sessionAttr("loggedInUser", user)).andDo(print())
		.andExpect(status().isOk())
		.andExpect(view().name(equalTo("admin")))
		.andReturn();
		assertNotNull(mvcResult);
		List<HandlerInterceptor> interceptors = Arrays.asList(mvcResult
				.getInterceptors());
		assertTrue(
				"Expecting login interceptor in this path for cases where there is no logged in user",
				interceptors.stream().anyMatch(
						i -> i.getClass().equals(AdminInterceptor.class)));
	
	}
}