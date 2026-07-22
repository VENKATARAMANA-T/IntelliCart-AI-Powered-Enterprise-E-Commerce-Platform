package com.example.Order_Service.service;
import com.example.Order_Service.dto.request.PlaceOrderRequest;
import com.example.Order_Service.dto.response.OrderResponse;
import com.example.Order_Service.dto.response.SellerOrderItemResponse;
import java.util.List;
public interface OrderService {
    OrderResponse placeOrder(Long customerId, PlaceOrderRequest request);
    List<OrderResponse> getCustomerOrders(Long customerId);
    OrderResponse getOrderById(Long customerId, Long orderId);
    List<SellerOrderItemResponse> getSellerOrders(Long sellerId);
    OrderResponse cancelOrder(Long customerId, Long orderId);
}
