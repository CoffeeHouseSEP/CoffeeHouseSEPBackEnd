package com.sep.coffeemanagement.controller;

import com.sep.coffeemanagement.service.order_detail.OrderDetailService;
import com.sep.coffeemanagement.service.orders.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "orders")
public class OrdersController extends AbstractController<OrdersService> {
  @Autowired
  private OrderDetailService orderDetailService;
}
