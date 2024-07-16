    package com.example.demo.auth.services;

    import com.example.demo.auth.dao.request.SignUpRequest;
    import com.example.demo.auth.dao.request.SigninRequest;
    import com.example.demo.auth.dao.response.JwtAuthenticationResponse;
    import com.example.demo.entity.Role;
    import com.example.demo.entity.User;
    import com.example.demo.reposiroty.UserRepository;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;
    import lombok.RequiredArgsConstructor;
    import java.util.NoSuchElementException;

    @Service
    @RequiredArgsConstructor
        public class AuthenticationServiceImpl implements AuthenticationService {
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;

        @Override
        public JwtAuthenticationResponse signup(SignUpRequest request) {
            var user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.USER)
                    .build();
            userRepository.save(user);

            var jwt = jwtService.generateToken(user);

            return JwtAuthenticationResponse.builder()
                    .token(jwt)
                    .build();
        }
        @Override
        public JwtAuthenticationResponse signin(SigninRequest request) {
            try {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));
                var user = userRepository.findByEmail(request.getEmail())
                        .orElseThrow();
                var jwt = jwtService.generateToken(user);
                return JwtAuthenticationResponse.builder().token(jwt).build();
            } catch (NoSuchElementException ex) {
                // Handle authentication or user retrieval errors here
                throw new RuntimeException("Authentication failed: " + ex.getMessage());
            }

        }

    }
