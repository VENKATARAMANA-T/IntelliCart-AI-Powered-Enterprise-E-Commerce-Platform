package com.example.Order_Service.controller;
import com.example.Order_Service.dto.request.PlaceOrderRequest;
import com.example.Order_Service.dto.response.OrderResponse;
import com.example.Order_Service.dto.response.SellerOrderItemResponse;
import com.example.Order_Service.security.AuthenticatedUser;
import com.example.Order_Service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired private OrderService orderService;
    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody PlaceOrderRequest request, Authentication authentication) {
        AuthenticatedUser customer = (AuthenticatedUser) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.placeOrder(customer.getUserId(), request));
    }
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getMyOrders(Authentication authentication) {
        AuthenticatedUser customer = (AuthenticatedUser) authentication.getPrincipal();
        return ResponseEntity.ok(orderService.getCustomerOrders(customer.getUserId()));
    }
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id, Authentication authentication) {
        AuthenticatedUser customer = (AuthenticatedUser) authentication.getPrincipal();
        return ResponseEntity.ok(orderService.getOrderById(customer.getUserId(), id));
    }
    @GetMapping("/seller")
    public ResponseEntity<List<SellerOrderItemResponse>> getSellerOrders(Authentication authentication) {
        AuthenticatedUser seller = (AuthenticatedUser) authentication.getPrincipal();
        return ResponseEntity.ok(orderService.getSellerOrders(seller.getUserId()));
    }
    @PutMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long id, Authentication authentication) {
        AuthenticatedUser customer = (AuthenticatedUser) authentication.getPrincipal();
        return ResponseEntity.ok(orderService.cancelOrder(customer.getUserId(), id));
    }
}
