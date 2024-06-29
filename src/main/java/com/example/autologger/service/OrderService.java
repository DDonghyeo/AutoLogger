package com.example.autologger.service;

import com.example.autologger.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public String saveOrder(String orderId) {
        return orderRepository.saveOrder(orderId);
    }
}
