package com.example.clip.controller;


import com.example.clip.repository.PaymentRepository;
import com.example.clip.response.GetAllUsersResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/clip/user")
public class UserController {

    PaymentRepository paymentRepository;

    public UserController(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<GetAllUsersResponse> getAllUsers() {
        log.info("Request to get all users received.");

        List<String> uniqueUserIdList = paymentRepository.findAllUniqueUserId();
        log.info("Successfully retrieved all unique User IDs.");
        return ResponseEntity.ok().body(GetAllUsersResponse.builder().userIdList(uniqueUserIdList).build());
    }
}
