package com.example.clip.service;

import com.example.clip.model.Payment;
import com.example.clip.model.PaymentStatus;
import com.example.clip.repository.PaymentRepository;
import com.example.clip.response.UserReportResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final PaymentRepository paymentRepository;

    public UserServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public UserReportResponse getUserReport(String userId) {
        List<Payment> paymentList = paymentRepository.findAllByUserId(userId);

        UserReportResponse userReportResponse = initializeUserReportResponse(userId);
        populateUserReportResponseWithPaymentList(userReportResponse, paymentList);

        return userReportResponse;
    }

    private void populateUserReportResponseWithPaymentList(UserReportResponse userReportResponse, List<Payment> paymentList) {
        paymentList.forEach(payment -> {
            userReportResponse.setPaymentsSum(userReportResponse.getPaymentsSum().add(payment.getAmount()));
            if (payment.getStatus().equals(PaymentStatus.NEW)) {
                userReportResponse.setNewPayments(userReportResponse.getNewPayments() + 1);
                userReportResponse.setNewPaymentsAmount(userReportResponse.getNewPaymentsAmount().add(payment.getAmount()));
            }
        });
    }

    private UserReportResponse initializeUserReportResponse(String userId) {
        return UserReportResponse.builder()
                .userName(userId)
                .newPaymentsAmount(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN))
                .paymentsSum(BigDecimal.ZERO.setScale(2, RoundingMode.DOWN))
                .build();
    }
}
