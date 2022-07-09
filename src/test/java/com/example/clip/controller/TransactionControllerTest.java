package com.example.clip.controller;

import com.example.clip.model.Payment;
import com.example.clip.repository.PaymentRepository;
import com.example.clip.request.PaymentRequest;
import com.example.clip.response.PaymentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionControllerTest {

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
    @Sql(statements = {"DELETE FROM payment"})
    void shouldSuccessfullyCreateANewPayment(String userId, BigDecimal amount) {
        PaymentRequest paymentRequest = buildPaymentRequest(userId, amount);

        ResponseEntity<PaymentResponse> responseEntity =
                this.restTemplate.postForEntity(createNewPaymentUri, paymentRequest, PaymentResponse.class);

        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertNotNull(responseEntity.getBody());
        Long paymentId = responseEntity.getBody().getPaymentId();
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