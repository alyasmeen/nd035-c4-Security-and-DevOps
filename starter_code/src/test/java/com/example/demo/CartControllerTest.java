package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    CartController cartController;
    CartRepository cartRepo=mock(CartRepository.class);
    ItemRepository itemRepo=mock(ItemRepository.class);
    UserRepository userRepo=mock(UserRepository.class);

    @Before
    public void setUp () {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "cartRepository", cartRepo);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepo);
        TestUtils.injectObjects(cartController, "userRepository", userRepo);
    }

    @Test
    public void addToCart(){
        User user=getUser();
        when(userRepo.findByUsername(user.getUsername())).thenReturn(user);
        List<Item> items = user.getCart().getItems();
        when(itemRepo.findById(items.get(0).getId())).thenReturn(Optional.of(items.get(0)));
        Cart cart = user.getCart();
        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setItemId(items.get(0).getId());
        cartRequest.setUsername(user.getUsername());
        cartRequest.setQuantity(1);
        when(cartRepo.save(cart)).thenReturn(cart);

        ResponseEntity<Cart> response=cartController.addTocart(cartRequest);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(cart.getItems(), response.getBody().getItems());
    }

    @Test
    public void removeFromCart(){
        User user=getUser();
        when(userRepo.findByUsername(user.getUsername())).thenReturn(user);
        List<Item> items=user.getCart().getItems();
        when(itemRepo.findById(items.get(0).getId())).thenReturn(Optional.of(items.get(0)));
        Cart cart=user.getCart();

        ModifyCartRequest cartRequest=new ModifyCartRequest();
        cartRequest.setItemId(items.get(0).getId());
        cartRequest.setUsername(user.getUsername());
        cartRequest.setQuantity(1);
        items.remove(items.get(0));
        cart.setItems(items);
        when(cartRepo.save(cart)).thenReturn(cart);

        ResponseEntity<Cart> response=cartController.removeFromcart(cartRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(cart.getItems(), response.getBody().getItems());
    }

    User getUser() {
        User user=new User();
        user.setId(1L);
        user.setUsername("username");
        user.setPassword("password");
        Cart cart=new Cart();
        cart.setId(1L);
        cart.setUser(user);
        List<Item> items=new ArrayList<>();
        items.add(getItem());
        cart.setItems(items);
        user.setCart(cart);
        return user;
    }

    Item getItem() {
        Item item=new Item();
        item.setId(1L);
        item.setName("name");
        item.setPrice(new BigDecimal(1.5));
        item.setDescription("description");
        return item;
    }
}
