package com.example.demo.reposiroty;

import com.example.demo.entity.ForgotPassword;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ForgotPassWordRepository extends JpaRepository<ForgotPassword,Integer> {
    @Query(value = "SELECT fp FROM ForgotPassword fp WHERE fp.otp = ?1 AND fp.user = ?2")
    Optional<ForgotPassword> findByOtpAndUser(Integer otp, User userId);


}
