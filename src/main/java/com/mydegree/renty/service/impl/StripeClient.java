package com.mydegree.renty.service.impl;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class StripeClient {
    public void chargeCreditCard(final String token, final double amount) throws StripeException {
        final Map<String, Object> params = new HashMap<>();
        params.put("amount", (int)(amount * 100));
        params.put("currency", "RON");
        params.put("source", token);
        final Charge charge = Charge.create(params);
    }
}
