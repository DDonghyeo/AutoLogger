package com.example.autologger.repository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class OrderRepository {
    public String saveOrder(String orderId) {
        if (orderId.isEmpty()) {
            throw new IllegalArgumentException("ID is null");
        }
        return "Success";
    }
}

