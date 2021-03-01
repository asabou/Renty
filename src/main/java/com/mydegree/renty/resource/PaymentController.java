package com.mydegree.renty.resource;

import com.mydegree.renty.service.impl.StripeClient;
import com.mydegree.renty.utils.ServicesUtils;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Value("${stripe.key.secret}")
    private String stripeKeySecret;

    private final StripeClient stripeClient;

    public PaymentController(StripeClient stripeClient) {
        this.stripeClient = stripeClient;
    }

    @PostMapping("/charge")
    private void chargeCard(@RequestHeader(name = "token") String token, @RequestHeader(name = "amount") String amount) throws StripeException {
        Stripe.apiKey = stripeKeySecret;
        final Double totalAmount = ServicesUtils.convertStringToDouble(amount);
        this.stripeClient.chargeCreditCard(token, totalAmount);
    }
}
