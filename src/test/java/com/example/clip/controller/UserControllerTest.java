package com.example.clip.controller;

import com.example.clip.repository.PaymentRepository;
import com.example.clip.response.GetAllUsersResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @LocalServerPort
    private int port;
    private URI getAllUsersUri;
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        getAllUsersUri = URI.create("http://localhost:" + port + "/api/clip/user/getAll");
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
}
