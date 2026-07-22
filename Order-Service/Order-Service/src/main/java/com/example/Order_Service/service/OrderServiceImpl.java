package com.example.Order_Service.service;
import com.example.Order_Service.dto.request.PlaceOrderRequest;
import com.example.Order_Service.dto.response.*;
import com.example.Order_Service.exception.OrderNotFoundException;
import com.example.Order_Service.exception.UnauthorizedException;
import com.example.Order_Service.feign.CartServiceClient;
import com.example.Order_Service.feign.ProductServiceClient;
import com.example.Order_Service.model.Order;
import com.example.Order_Service.model.OrderItem;
import com.example.Order_Service.model.OrderStatus;
import com.example.Order_Service.repository.OrderItemRepository;
import com.example.Order_Service.repository.OrderRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderItemRepository orderItemRepository;
    @Autowired private CartServiceClient cartServiceClient;
    @Autowired private ProductServiceClient productServiceClient;

    @Override @Transactional
    public OrderResponse placeOrder(Long customerId, PlaceOrderRequest request) {
        if (request.getShippingAddress() == null || request.getShippingAddress().isBlank()) {
            throw new IllegalArgumentException("Shipping address is required");
        }
        
        Order order = new Order();
        order.setCustomerId(customerId);
        order.setShippingAddress(request.getShippingAddress());
        order.setStatus(OrderStatus.PENDING);
        BigDecimal totalAmount = BigDecimal.ZERO;
        
        boolean isDirectOrder = request.getProductId() != null && request.getQuantity() != null;

        if (isDirectOrder) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(request.getProductId());
            orderItem.setQuantity(request.getQuantity());
            try {
                ProductResponse product = productServiceClient.getProductById(request.getProductId());
                orderItem.setProductName(product.getName());
                orderItem.setThumbnailUrl(product.getImageUrls() != null && !product.getImageUrls().isEmpty() ? product.getImageUrls().get(0) : null);
                orderItem.setProductPrice(product.getPrice());
                orderItem.setSellerId(product.getSellerId() != null ? product.getSellerId() : 0L);
            } catch (FeignException e) {
                log.error("Failed to fetch product for direct order: {}", e.getMessage());
                throw new IllegalArgumentException("Product not found or unavailable.");
            }
            BigDecimal itemTotal = orderItem.getProductPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()));
            totalAmount = itemTotal;
            order.addItem(orderItem);
        } else {
            CartResponse cart;
            try { cart = cartServiceClient.getCart(); }
            catch (FeignException e) { log.error("Failed to fetch cart: {}", e.getMessage()); throw new IllegalArgumentException("Unable to retrieve your cart. Please try again."); }
            if (cart.getItems() == null || cart.getItems().isEmpty()) { throw new IllegalArgumentException("Your cart is empty. Add items before placing an order."); }
            
            for (CartItemResponse cartItem : cart.getItems()) {
                OrderItem orderItem = new OrderItem();
                orderItem.setProductId(cartItem.getProductId());
                orderItem.setProductName(cartItem.getName());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setThumbnailUrl(cartItem.getThumbnailUrl());
                BigDecimal price; Long sellerId;
                try {
                    ProductResponse product = productServiceClient.getProductById(cartItem.getProductId());
                    price = product.getPrice() != null ? product.getPrice() : BigDecimal.valueOf(cartItem.getPrice());
                    sellerId = product.getSellerId() != null ? product.getSellerId() : 0L;
                } catch (FeignException e) {
                    log.warn("Could not fetch product {}, using cart data", cartItem.getProductId());
                    price = BigDecimal.valueOf(cartItem.getPrice());
                    sellerId = 0L;
                }
                orderItem.setProductPrice(price);
                orderItem.setSellerId(sellerId);
                BigDecimal itemTotal = price.multiply(BigDecimal.valueOf(cartItem.getQuantity()));
                totalAmount = totalAmount.add(itemTotal);
                order.addItem(orderItem);
            }
        }
        
        order.setTotalAmount(totalAmount);
        Order savedOrder = orderRepository.save(order);
        
        if (!isDirectOrder) {
            try { cartServiceClient.clearCart(); }
            catch (FeignException e) { log.warn("Failed to clear cart after order: {}", e.getMessage()); }
        }
        return mapToOrderResponse(savedOrder);
    }

    @Override
    public List<OrderResponse> getCustomerOrders(Long customerId) {
        return orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId).stream().map(this::mapToOrderResponse).collect(Collectors.toList());
    }

    @Override
    public OrderResponse getOrderById(Long customerId, Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));
        if (!order.getCustomerId().equals(customerId)) { throw new UnauthorizedException("You do not have permission to view this order"); }
        return mapToOrderResponse(order);
    }

    @Override
    public List<SellerOrderItemResponse> getSellerOrders(Long sellerId) {
        return orderItemRepository.findBySellerIdOrderByIdDesc(sellerId).stream().map(item ->
            SellerOrderItemResponse.builder()
                .orderId(item.getOrder().getId())
                .productId(item.getProductId())
                .productName(item.getProductName())
                .productPrice(item.getProductPrice())
                .quantity(item.getQuantity())
                .itemTotal(item.getProductPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .orderStatus(item.getOrder().getStatus().name())
                .orderDate(item.getOrder().getCreatedAt())
                .build()
        ).collect(Collectors.toList());
    }

    @Override @Transactional
    public OrderResponse cancelOrder(Long customerId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));
        if (!order.getCustomerId().equals(customerId)) {
            throw new UnauthorizedException("You do not have permission to modify this order");
        }
        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.CONFIRMED) {
            throw new IllegalArgumentException("Order cannot be cancelled in its current state: " + order.getStatus());
        }
        order.setStatus(OrderStatus.CANCELLED);
        Order savedOrder = orderRepository.save(order);
        return mapToOrderResponse(savedOrder);
    }

    private OrderResponse mapToOrderResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems().stream().map(item ->
            OrderItemResponse.builder()
                .id(item.getId()).productId(item.getProductId()).sellerId(item.getSellerId())
                .productName(item.getProductName()).productPrice(item.getProductPrice())
                .quantity(item.getQuantity())
                .itemTotal(item.getProductPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .thumbnailUrl(item.getThumbnailUrl()).build()
        ).collect(Collectors.toList());
        return OrderResponse.builder().id(order.getId()).customerId(order.getCustomerId()).totalAmount(order.getTotalAmount())
            .shippingAddress(order.getShippingAddress()).status(order.getStatus().name()).items(itemResponses)
            .createdAt(order.getCreatedAt()).updatedAt(order.getUpdatedAt()).build();
    }
}
