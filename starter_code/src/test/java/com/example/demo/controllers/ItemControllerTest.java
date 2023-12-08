package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;

    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp () {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void get_items () {
        ResponseEntity<List<Item>> items = itemController.getItems();
        assertNotNull(items);
        assertEquals(200, items.getStatusCodeValue());
    }

    @Test
    public void get_items_by_name () {
        List<Item> i = new ArrayList<Item>(); i.add(new Item());
        when(itemRepository.findByName("Round Widget")).thenReturn(i);
        ResponseEntity<List<Item>> items = itemController.getItemsByName("Round Widget");
        assertNotNull(items);
        assertEquals(200, items.getStatusCodeValue());

        assertEquals(1, Objects.requireNonNull(items.getBody()).size());
    }

    @Test
    public void get_items_by_invalid_name () {
        List<Item> i = new ArrayList<Item>();
        when(itemRepository.findByName("missing Widget")).thenReturn(i);
        ResponseEntity<List<Item>> items = itemController.getItemsByName("missing Widget");
        assertNotNull(items);
        assertEquals(404, items.getStatusCodeValue());
    }

    @Test
    public void get_items_by_id () {
        Item i = new Item();
        i.setId(1L);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(i));
        ResponseEntity<Item> item = itemController.getItemById(1L);
        assertNotNull(item);
        assertEquals(200, item.getStatusCodeValue());
    }

    @Test
    public void get_items_by_invalid_id () {
        when(itemRepository.findById(2L)).thenReturn(Optional.ofNullable(null));
        ResponseEntity<Item> item = itemController.getItemById(2L);
        assertNotNull(item);
        assertEquals(404, item.getStatusCodeValue());
    }
}
