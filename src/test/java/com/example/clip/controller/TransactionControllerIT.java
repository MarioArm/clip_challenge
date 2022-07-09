package com.example.clip.controller;

import com.example.clip.model.Payment;
import com.example.clip.repository.PaymentRepository;
import com.example.clip.request.PaymentRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionControllerIT {

    @LocalServerPort
    private int port;
    private URI createNewPaymentUri;
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        createNewPaymentUri = URI.create("http://localhost:" + port + "/api/clip/createPayload");
    }

    @ParameterizedTest
    @CsvSource({
            "User-1,10.10",
            "User-1,100.22",
            "User-123123123123, 333833.62"
    })
    void shouldSuccessfullyCreateANewPayment(String userId, BigDecimal amount) {
        PaymentRequest paymentRequest = buildPaymentRequest(userId, amount);

        ResponseEntity<String> stringResponseEntity = this.restTemplate.postForEntity(createNewPaymentUri, paymentRequest, String.class);

        assertTrue(stringResponseEntity.getStatusCode().is2xxSuccessful());
        assertTrue(StringUtils.isNotBlank(stringResponseEntity.getBody()));
        Long paymentId = Long.parseLong(stringResponseEntity.getBody());
        Payment payment = paymentRepository.getById(paymentId);
        assertEquals(userId, payment.getUserId());
        assertEquals(amount, payment.getAmount());
    }

    private PaymentRequest buildPaymentRequest(String userId, BigDecimal amount) {
        return PaymentRequest.builder()
                .userId(userId)
                .amount(amount)
                .build();
    }
}