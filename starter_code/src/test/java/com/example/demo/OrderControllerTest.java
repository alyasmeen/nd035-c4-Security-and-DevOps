package com.example.demo;

import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class OrderControllerTest {
    MockMvc mvc;
    @Autowired
    WebApplicationContext webApplicationContext;
    OrderRepository orderRepo=mock(OrderRepository.class);
    UserRepository userRepo=mock(UserRepository.class);
    OrderController orderController;

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepo);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepo);
        mvc= MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void submit() throws Exception {
        User user = getUser();
        when(userRepo.findByUsername(user.getUsername())).thenReturn(user);
        UserOrder userOrder = new UserOrder();
        userOrder.setId(1L);
        userOrder.setUser(user);
        user.setCart(user.getCart());
        when(orderRepo.findById(userOrder.getId())).thenReturn(Optional.of(userOrder));
        when(orderRepo.save(userOrder)).thenReturn(userOrder);

        ResponseEntity<UserOrder> response = orderController.submit(user.getUsername());
        assertEquals(200, response.getStatusCodeValue());
        UserOrder userOrderResponse = response.getBody();
        assertNotNull(userOrderResponse);
    }

    @Test
    public void getUserOrders() throws Exception {
        User user = getUser();
        when(userRepo.findByUsername(user.getUsername())).thenReturn(user);
        UserOrder userOrder = new UserOrder();
        userOrder.setId(1L);
        userOrder.setUser(user);
        user.setCart(user.getCart());
        List<UserOrder> userOrders = new ArrayList<>();
        userOrders.add(userOrder);
        when(orderRepo.findByUser(user)).thenReturn(userOrders);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(user.getUsername());
        assertEquals(200, response.getStatusCodeValue());
        List<UserOrder> userOrdersResponse = response.getBody();
        assertEquals(userOrdersResponse, userOrders);
    }

    @Test
    public void ordersForInvalidUser() throws Exception {
        MvcResult result=mvc.perform(get("/api/order/history/username4")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertNotNull(result.getResponse().getContentAsString());
    }

    @Test
    public void submitInvalidUser() throws Exception {
        MvcResult result=mvc.perform(post("/api/order/submit/username5")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(404, result.getResponse().getStatus());
    }

    private User getUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("username11");
        user.setPassword("password");
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        List<Item> items = new ArrayList<>();
        items.add(getItem());
        cart.setItems(items);
        user.setCart(cart);
        return user;
    }

    private Item getItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setPrice(new BigDecimal(1.5));
        item.setDescription("description");
        return item;
    }


}
