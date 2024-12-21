package org.example.onlineshop.service;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.stereotype.Service;

@Service
public class StripeService {
    public StripeService() {
        Stripe.apiKey = "sk_test_51QYBdNRr839MP2GDwf2DYRmpin4XlTtEDrN4rk4dcfSljNsshZvn0QTQiHo6CMtZLAMteBslrNUvd3lVYVeBPzuL00RDmZ31HZ"; // Initialize Stripe with the API key
    }

    public PaymentIntent createPaymentIntent(Long amount, String currency, String description) throws Exception {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount)
                .setCurrency(currency)
                .setDescription(description)
                .build();

        return PaymentIntent.create(params);
    }
}

