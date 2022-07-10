package com.example.clip.controller;

import com.example.clip.model.Payment;
import com.example.clip.repository.PaymentRepository;
import com.example.clip.request.PaymentRequest;
import com.example.clip.response.DisbursementDetails;
import com.example.clip.response.PaymentResponse;
import com.example.clip.response.ProcessDisbursementResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionControllerTest {

    @LocalServerPort
    private int port;
    private static final String URL_FORMAT = "http://localhost:%s/api/clip/%s";
    private URI createNewPaymentUri;
    private URI processDisbursementUri;
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        createNewPaymentUri = URI.create(String.format(URL_FORMAT, port, "createPayload"));
        processDisbursementUri = URI.create(String.format(URL_FORMAT, port, "/disbursement/process"));
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

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
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

    @Test
    @Sql(scripts = "classpath:sql/transaction_controller-process_disbursement-success.sql")
    void shouldSuccessToProcessDisbursement() {
        Map<String, DisbursementDetails> expectedResponse = getExpectedProcessDisbursementResponse();

        ResponseEntity<ProcessDisbursementResponse> responseEntity =
                this.restTemplate.postForEntity(processDisbursementUri, null, ProcessDisbursementResponse.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ProcessDisbursementResponse response = responseEntity.getBody();
        assertNotNull(response);
        response.getDisbursementDetailsMap().forEach(
                (userId, details) -> {
                    assertNotNull(expectedResponse.get(userId));
                    assertEquals(expectedResponse.get(userId), details);
                }
        );
        expectedResponse.forEach(
                (userId, details) -> {
                    assertNotNull(response.getDisbursementDetailsMap().get(userId));
                    assertEquals(response.getDisbursementDetailsMap().get(userId), details);
                }
        );
    }

    @Test
    @Sql(scripts = "classpath:sql/transaction_controller-process_disbursement-success.sql")
    void shouldSuccessToProcessDisbursementButItMustBeEmpty() {
        this.restTemplate.postForEntity(processDisbursementUri, null, ProcessDisbursementResponse.class);
        ResponseEntity<ProcessDisbursementResponse> responseEntity =
                this.restTemplate.postForEntity(processDisbursementUri, null, ProcessDisbursementResponse.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ProcessDisbursementResponse response = responseEntity.getBody();
        assertNotNull(response);
        assertNotNull(response.getDisbursementDetailsMap());
        assertTrue(response.getDisbursementDetailsMap().isEmpty());
    }

    private Map<String, DisbursementDetails> getExpectedProcessDisbursementResponse() {
        return Map.of(
                "user-id-14", DisbursementDetails.builder()
                        .payment(BigDecimal.valueOf(10).setScale(2, RoundingMode.DOWN))
                        .disbursement(BigDecimal.valueOf(9.65).setScale(2, RoundingMode.DOWN))
                        .build(),
                "user-id-15", DisbursementDetails.builder()
                        .payment(BigDecimal.valueOf(20).setScale(2, RoundingMode.DOWN))
                        .disbursement(BigDecimal.valueOf(20 - (20 * 0.035)).setScale(2, RoundingMode.DOWN))
                        .build()
        );
    }
}