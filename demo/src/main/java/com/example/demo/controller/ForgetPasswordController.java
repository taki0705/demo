    package com.example.demo.controller;

    import com.example.demo.auth.dao.ChangePassword;
    import com.example.demo.entity.ForgotPassword;
    import com.example.demo.entity.User;
    import com.example.demo.reposiroty.ForgotPassWordRepository;
    import com.example.demo.reposiroty.UserRepository;
    import com.example.demo.dto.MailBody;
    import com.example.demo.service.EmailService;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.web.bind.annotation.*;

    import java.time.Instant;
    import java.util.Date;
    import java.util.Objects;
    import java.util.Random;

    @RestController
    @RequestMapping("/forgotPassword")
    public class ForgetPasswordController {
        private final UserRepository userRepository;
        private final EmailService emailService;
        private final ForgotPassWordRepository forgotPassWordRepository;
        private final PasswordEncoder passwordEncoder;
        public ForgetPasswordController(UserRepository userRepository, EmailService emailService, ForgotPassWordRepository forgotPassWordRepository, PasswordEncoder passwordEncoder) {
            this.userRepository = userRepository;
            this.emailService = emailService;
            this.forgotPassWordRepository = forgotPassWordRepository;
            this.passwordEncoder = passwordEncoder;
        }

        @PostMapping("/verifyMail/{email}")
        public ResponseEntity<String> verifyEmail(@PathVariable String email){
            User user=userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Please provide an valid email"));
            int otp= otpGenerator();
            MailBody mailBody= MailBody.builder().
                    to(email).
                    text("this is the OTP for your Forget PassWord request: " +otp)
                    .subject("OTP for Forgot Password Request")
                    .build();
            ForgotPassword fp= ForgotPassword.builder()
                    .otp(otp)
                    .expirationTime(new Date(System.currentTimeMillis()+70*1000))
                    .user(user)
                    .build();
            emailService.sendSimpleMessage(mailBody);
            forgotPassWordRepository.save(fp);
            return ResponseEntity.ok("Email Sent for verification");

        }
        @PostMapping("/verifyOtp/{otp}/{email}")
        public ResponseEntity<String> verifyOtp(@PathVariable Integer otp,@PathVariable String email){
            User user=userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Please provide an valid email"));
           ForgotPassword fp= forgotPassWordRepository.findByOtpAndUser(otp,user).orElseThrow(() ->new RuntimeException("Invalid OTP for email"));
//            if(fp.getExpirationTime().before(Date.from(Instant.now()))){
//                forgotPassWordRepository.deleteById(fp.getFpid());
//                return new ResponseEntity<>("OTP has expired", HttpStatus.EXPECTATION_FAILED);
//            }
            forgotPassWordRepository.deleteById(fp.getFpid());  
            return ResponseEntity.ok("OTP verified");
        }

        @PostMapping("/changePassword/{email}")
        public ResponseEntity<String> changePassWordHandler(@RequestBody ChangePassword changePassword,
                                                            @PathVariable String email){
            if(!Objects.equals(changePassword.password(),changePassword.repeatPassword())){
                return new ResponseEntity<>("Please enter the password again!",HttpStatus.EXPECTATION_FAILED);
            }
                String encodedPassword=passwordEncoder.encode(changePassword.password());
                userRepository.updatePassword(email,encodedPassword);
            return ResponseEntity.ok("Password has been changed");
        }
        private int otpGenerator() {
            Random random=new Random();
            return random.nextInt(100_000,999_999);
        }


    }
