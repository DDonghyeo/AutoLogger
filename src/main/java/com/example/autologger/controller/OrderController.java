package com.example.autologger.controller;

import com.example.autologger.annotaion.AutoLog;
import com.example.autologger.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/order")
@RestController
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/save")
    @AutoLog
    public ResponseEntity<?> save(@RequestParam String orderId) {
        return ResponseEntity.ok(orderService.saveOrder(orderId));
    }
}
