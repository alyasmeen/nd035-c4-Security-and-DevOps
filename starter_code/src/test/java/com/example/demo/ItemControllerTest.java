package com.example.demo;

import com.example.demo.controllers.OrderController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class ItemControllerTest {
    MockMvc mvc;
    @Autowired
    WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        mvc= MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void getItems() throws Exception {
        MvcResult result = mvc.perform(get("/api/item").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    public void getItemById() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/api/item/1").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());
    }

    @Test
    public void getItemsByName() throws Exception {
        MvcResult result=mvc.perform(get("/api/item/name/Round Widget").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    public void getItemByInvalidId() throws Exception {
        MvcResult result = mvc.perform(get("/api/item/100").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertEquals(404, result.getResponse().getStatus());
    }

    @Test
    public void getItemsByInvalidName() throws Exception {
        MvcResult result = mvc.perform(get("/api/item/name/invalid").accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertNotNull(result.getResponse().getContentAsString());
    }
}
