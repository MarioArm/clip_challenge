package com.example.clip.controller;


import com.example.clip.repository.PaymentRepository;
import com.example.clip.response.GetAllUsersResponse;
import com.example.clip.response.UserReportResponse;
import com.example.clip.service.UserService;
import com.example.clip.service.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/clip/user")
public class UserController {

    private final UserService userService;
    PaymentRepository paymentRepository;

    public UserController(UserService userServiceImpl, PaymentRepository paymentRepository) {
        this.userService = userServiceImpl;
        this.paymentRepository = paymentRepository;
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<GetAllUsersResponse> getAllUsers() {
        log.debug("Request to get all users received.");

        List<String> uniqueUserIdList = paymentRepository.findAllUniqueUserId();
        log.info("Successfully retrieved all unique User IDs.");
        return ResponseEntity.ok().body(GetAllUsersResponse.builder().userIdList(uniqueUserIdList).build());
    }

    @GetMapping(value = "{userId}/report")
    public ResponseEntity<UserReportResponse> getUserReport(@PathVariable @NotBlank String userId) {
        log.debug(String.format("Request to get user report for user: [%s]", userId));

        UserReportResponse userReport = userService.getUserReport(userId);

        log.info(String.format("User report generated successfully for user [%s]", userId));
        return ResponseEntity.ok().body(userReport);
    }
}
