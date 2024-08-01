package com.pratham.order.order;


import com.pratham.order.customer.CustomerClient;
import com.pratham.order.exception.BusinessException;
import com.pratham.order.kafka.OrderConfirmation;
import com.pratham.order.kafka.OrderProducer;
import com.pratham.order.orderline.OrderLineRequest;
import com.pratham.order.orderline.OrderLineService;
import com.pratham.order.payment.PaymentClient;
import com.pratham.order.payment.PaymentRequest;
import com.pratham.order.product.PurchaseRequest;
import com.pratham.order.product.ProductClient;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final CustomerClient customerClient;
    private final PaymentClient paymentClient;
    private final ProductClient productClient;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;

    @Transactional
    public Integer createOrder(OrderRequest request) {
        //check customer  -->openFeign
        var customer = this.customerClient.findCustomerById(request.customerId())
                .orElseThrow(() -> new BusinessException("Cannot create order::No Customer found"));

        //purchase the products -->product-microservice
        var purchaseProducts= this.productClient.purchaseProduct(request.products());

        //persist order
        var order = this.repository.save(mapper.toOrder(request));

        //persist order lines
        for(PurchaseRequest purchaseRequest : request.products()) {
                          orderLineService.saveOrderLine(
                                  new OrderLineRequest(
                                          null,
                                          order.getId(),
                                          purchaseRequest.productId(),
                                          purchaseRequest.quantity()
                    ));
        }
        //start payment process
        var paymentRequest = new PaymentRequest(
                request.amount(),
                request.paymentMethod(),
                order.getId(),
                order.getReference(),
                customer
        );

           paymentClient.requestOrderPayment(paymentRequest);


        //send the order confirmation -->notification-ms (kafka)
        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        request.reference(),
                        request.amount(),
                        request.paymentMethod(),
                        customer,
                        purchaseProducts
                )

        );
        return order.getId();
    }

    public List<OrderResponse> findAllOrders() {
        return this.repository.findAll()
                .stream()
                .map(this.mapper::fromOrder)
                .collect(Collectors.toList());
    }

    public OrderResponse findById(Integer id) {
        return this.repository.findById(id)
                .map(this.mapper::fromOrder)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No Order found of this ID: %d", id)));
    }

}
