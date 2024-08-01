package com.pratham.order.kafka;

import com.pratham.order.customer.CustomerResponse;
import com.pratham.order.order.PaymentMethod;
import com.pratham.order.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products
) {
}
