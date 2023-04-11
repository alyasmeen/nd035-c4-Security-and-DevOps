package com.example.demo;

import com.example.demo.model.requests.CreateUserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class UserControllerTest {
    MockMvc mvc;
    @Autowired
    WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        mvc=MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void createUser() throws Exception {
        CreateUserRequest user = new CreateUserRequest();
        user.setUsername("username");
        user.setPassword("password");
        user.setConfirmPassword("password");
        MvcResult result=mvc.perform(post("/api/user/create")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson(user)))
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    public void invalidPasswordLength() throws Exception {
        CreateUserRequest user=new CreateUserRequest();
        user.setUsername("username2");
        user.setPassword("123456");
        user.setConfirmPassword("123456");
        MvcResult result=mvc.perform(post("/api/user/create")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(toJson(user)))
                .andReturn();
        assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    public void passwordNotMatch() throws Exception {
        CreateUserRequest user=new CreateUserRequest();
        user.setUsername("username3");
        user.setPassword("password");
        user.setConfirmPassword("password2");
        MvcResult result=mvc.perform(post("/api/user/create")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(toJson(user)))
                .andReturn();assertEquals(400, result.getResponse().getStatus());
    }

    @Test
    public void findById() throws Exception {
        CreateUserRequest user = new CreateUserRequest();
        user.setUsername("username5");
        user.setPassword("password");
        user.setConfirmPassword("password");
        mvc.perform(post("/api/user/create")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(toJson(user)))
                .andReturn();

        MvcResult result = mvc.perform(get("/api/user/id/1")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    public void findByUsername() throws Exception {
        CreateUserRequest user = new CreateUserRequest();
        user.setUsername("username6");
        user.setPassword("password");
        user.setConfirmPassword("password");
        mvc.perform(post("/api/user/create")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(toJson(user)))
                .andReturn();

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/user/username6").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    String toJson(Object o) throws Exception {
        return new ObjectMapper().writeValueAsString(o);
    }


}
