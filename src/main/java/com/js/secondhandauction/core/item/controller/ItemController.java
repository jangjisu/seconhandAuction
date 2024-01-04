package com.js.secondhandauction.core.item.controller;

import com.js.secondhandauction.core.item.domain.Item;
import com.js.secondhandauction.core.item.dto.ItemCreateRequest;
import com.js.secondhandauction.core.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/items")
public class ItemController {
    @Autowired
    private ItemService itemService;

    /**
     * 상품 등록
     */
    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody ItemCreateRequest itemCreateRequest) {
        return ResponseEntity.ok(itemService.createItem(itemCreateRequest));
    }

    /**
     * 상품 조회
     */
    @GetMapping("/{itemNo}")
    public ResponseEntity<Item> getItem(@PathVariable long itemNo) {
        return ResponseEntity.ok(itemService.getItem(itemNo));
    }




}
