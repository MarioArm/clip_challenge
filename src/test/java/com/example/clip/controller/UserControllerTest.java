package com.example.clip.controller;

import com.example.clip.repository.PaymentRepository;
import com.example.clip.response.GetAllUsersResponse;
import com.example.clip.response.UserReportResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @LocalServerPort
    private int port;
    private static final String URI_FORMAT = "http://localhost:%s/api/clip/user/%s";
    private URI getAllUsersUri;
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        getAllUsersUri = URI.create(String.format(URI_FORMAT, port, "getAll"));
    }


    @Test
    @Sql(scripts = "classpath:sql/user_controller-get_all_users-success.sql")
    void shouldSuccessfullyGetAllUsersForPaymentTable() {
        GetAllUsersResponse expectedResponse = getExpectedGetAllUsersResponse();

        ResponseEntity<GetAllUsersResponse> responseEntity =
                this.restTemplate.getForEntity(getAllUsersUri, GetAllUsersResponse.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        List<String> responseUserIdList = responseEntity.getBody().getUserIdList();
        assertEquals(expectedResponse.getUserIdList().size(), responseUserIdList.size());
        assertTrue(expectedResponse.getUserIdList().containsAll(responseUserIdList));
        assertTrue(responseUserIdList.containsAll(expectedResponse.getUserIdList()));
    }

    private GetAllUsersResponse getExpectedGetAllUsersResponse() {
        return GetAllUsersResponse.builder()
                .userIdList(List.of(
                        "user-id-1",
                        "user-id-2",
                        "user-id-3",
                        "user-id-4",
                        "user-id-5",
                        "user-id-6",
                        "user-id-7",
                        "user-id-8",
                        "user-id-10",
                        "user-id-13",
                        "user-id-14",
                        "user-id-15"
                )).build();
    }

    @ParameterizedTest
    @ValueSource(strings = {"user-id-1", "user-id-2", "user-id-14", "user-id-15",})
    @Sql(scripts = "classpath:sql/user_controller-get_user_report-success.sql")
    void shouldSuccessToGenerateUserReport(String userId) {
        UserReportResponse expectedResponse = getExpectedUserReportResponse(userId);

        ResponseEntity<UserReportResponse> responseEntity =
                this.restTemplate.getForEntity(
                        URI.create(String.format(URI_FORMAT, port, userId + "/report")), UserReportResponse.class);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        UserReportResponse response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals(expectedResponse, response);
    }

    private UserReportResponse getExpectedUserReportResponse(String userId) {
        switch (userId) {
            case "user-id-1":
                return UserReportResponse.builder()
                        .userName("user-id-1")
                        .paymentsSum(BigDecimal.valueOf(10).setScale(2, RoundingMode.DOWN))
                        .newPayments(0)
                        .newPaymentsAmount(BigDecimal.valueOf(0).setScale(2, RoundingMode.DOWN))
                        .build();
            case "user-id-2":
                return UserReportResponse.builder()
                        .userName("user-id-2")
                        .paymentsSum(BigDecimal.valueOf(10).setScale(2, RoundingMode.DOWN))
                        .newPayments(0)
                        .newPaymentsAmount(BigDecimal.valueOf(0).setScale(2, RoundingMode.DOWN))
                        .build();
            case "user-id-14":
                return UserReportResponse.builder()
                        .userName("user-id-14")
                        .paymentsSum(BigDecimal.valueOf(10).setScale(2, RoundingMode.DOWN))
                        .newPayments(1)
                        .newPaymentsAmount(BigDecimal.valueOf(10).setScale(2, RoundingMode.DOWN))
                        .build();
            case "user-id-15":
                return UserReportResponse.builder()
                        .userName("user-id-15")
                        .paymentsSum(BigDecimal.valueOf(30).setScale(2, RoundingMode.DOWN))
                        .newPayments(1)
                        .newPaymentsAmount(BigDecimal.valueOf(20).setScale(2, RoundingMode.DOWN))
                        .build();
            default:
                throw new IllegalArgumentException("This user ID is not allowed");
        }
    }
}
