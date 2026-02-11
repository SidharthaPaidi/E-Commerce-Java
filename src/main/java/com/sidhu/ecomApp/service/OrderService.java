package com.sidhu.ecomApp.service;

import com.sidhu.ecomApp.model.Order;
import com.sidhu.ecomApp.model.OrderItem;
import com.sidhu.ecomApp.model.Product;
import com.sidhu.ecomApp.model.dto.OrderItemRequest;
import com.sidhu.ecomApp.model.dto.OrderItemResponse;
import com.sidhu.ecomApp.model.dto.OrderRequest;
import com.sidhu.ecomApp.model.dto.OrderResponse;
import com.sidhu.ecomApp.repo.OrderRepo;
import com.sidhu.ecomApp.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private OrderRepo orderRepo;


    public OrderResponse placeOrder(OrderRequest orderRequest) {
        Order order = new Order();

        order.setOrderId("ORD" + UUID.randomUUID().toString().substring(0,8).toUpperCase());
        order.setCustomerName(orderRequest.customerName());
        order.setEmail(orderRequest.email());
        order.setStatus("PLACED");
        order.setOrderDate(LocalDate.now());

        List<OrderItem> orderItems = new ArrayList<>();

        for(OrderItemRequest itemRequest : orderRequest.items()) {
            Product product = productRepo.findById(itemRequest.productId())
                    .orElseThrow(() -> new RuntimeException("Product Not Found"));

            product.setStockQuantity(product.getStockQuantity()-itemRequest.quantity());
            productRepo.save(product);

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemRequest.quantity())
                    .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(itemRequest.quantity())))
                    .order(order)
                    .build();

            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);

        Order savedOrder = orderRepo.save(order);

        List<OrderItemResponse> itemResponses = new ArrayList<>();

        for(OrderItem item : order.getOrderItems()) {
            OrderItemResponse orderItemResponse = new OrderItemResponse(
                    item.getProduct().getName(),
                    item.getQuantity(),
                    item.getTotalPrice()
            );

            itemResponses.add(orderItemResponse);
        }

        return new OrderResponse(
                savedOrder.getOrderId(),
                savedOrder.getCustomerName(),
                savedOrder.getEmail(),
                savedOrder.getOrderId(),
                savedOrder.getOrderDate(),
                itemResponses
        );
    }

    public List<OrderResponse> getAllOrdersResponses() {
        List<Order> orders = orderRepo.findAll();
        List<OrderResponse> orderResponses = new ArrayList<>();



        for(Order order : orders) {

            List<OrderItemResponse> itemResponses = new ArrayList<>();

            for(OrderItem item : order.getOrderItems()) {
                OrderItemResponse orderItemResponse = new OrderItemResponse(
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getTotalPrice()
                );

                itemResponses.add(orderItemResponse);
            }

            OrderResponse orderResponse = new OrderResponse(
                    order.getOrderId(),
                    order.getCustomerName(),
                    order.getEmail(),
                    order.getOrderId(),
                    order.getOrderDate(),
                    itemResponses
            );

            orderResponses.add(orderResponse);
        }


        return orderResponses;
    }
}
