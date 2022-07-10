package com.example.clip.service;

import com.example.clip.response.GetAllUsersResponse;
import com.example.clip.response.UserReportResponse;

public interface UserService {
    UserReportResponse getUserReport(String userId);
    GetAllUsersResponse getAllUsers();
}
