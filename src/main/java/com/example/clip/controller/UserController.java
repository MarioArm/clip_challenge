package com.example.clip.controller;


import com.example.clip.response.GetAllUsersResponse;
import com.example.clip.response.UserReportResponse;
import com.example.clip.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;


@Slf4j
@RestController
@RequestMapping("/api/clip/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userServiceImpl) {
        this.userService = userServiceImpl;
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<GetAllUsersResponse> getAllUsers() {
        log.debug("Request to get all users received.");
        GetAllUsersResponse getAllUsersResponse = userService.getAllUsers();
        log.info("Successfully retrieved all unique User IDs.");
        return ResponseEntity.ok().body(getAllUsersResponse);
    }

    @GetMapping(value = "{userId}/report")
    public ResponseEntity<UserReportResponse> getUserReport(@PathVariable @NotBlank String userId) {
        log.debug(String.format("Request to get user report for user: [%s]", userId));

        UserReportResponse userReport = userService.getUserReport(userId);

        log.info(String.format("User report generated successfully for user [%s]", userId));
        return ResponseEntity.ok().body(userReport);
    }
}
