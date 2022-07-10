package com.example.clip.repository;

import com.example.clip.model.Payment;
import com.example.clip.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query(value = "SELECT DISTINCT user_id FROM payment", nativeQuery = true)
    List<String> findAllUniqueUserId();

    List<Payment> findAllByStatus(PaymentStatus paymentStatus);
}
