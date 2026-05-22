package com.devotee.resume.init.service;

import com.devotee.resume.init.Repository.UserRepository;
import com.devotee.resume.init.document.User;
import com.devotee.resume.init.dto.AuthResponse;
import com.devotee.resume.init.dto.LoginRequest;
import com.devotee.resume.init.dto.RegisterRequest;
import com.devotee.resume.init.exception.ResourceExistsException;
import com.devotee.resume.init.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    @Value("${app.base.url}")
    private String appUrl;

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest request) {

        log.info("Inside AuthService : register() {}", request);

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceExistsException(
                    "User already exists with this email"
            );
        }

        User newUser = convertToUser(request);

        userRepository.save(newUser);

        // TODO : Send verification email
        sendVerificationEmail(newUser);

        return convertToResponse(newUser);
    }

    private void sendVerificationEmail(User newUser) {
log.info("Inside auth service- send verification email method :{}", newUser);
        try {

            String link = appUrl +
                    "/api/auth/verify-email?token=" +
                    newUser.getVerificationToken();

            String html =
                    "<div style='font-family:sans-serif'>" +
                            "<h2>Verify your email</h2>" +
                            "<p>Hi " + newUser.getName() +
                            ", please confirm your email to activate your account.</p>" +

                            "<p>" +
                            "<a href='" + link + "' " +
                            "style='display:inline-block;padding:10px 16px;" +
                            "background:#6366f1;color:#fff;text-decoration:none;" +
                            "border-radius:5px;'>Verify Email</a>" +
                            "</p>" +

                            "<p>Or copy this link: " + link + "</p>" +
                            "<p>This link expires in 24 hours.</p>" +
                            "</div>";

            emailService.sendHtmlEmail(
                    newUser.getEmail(),
                    "Verify your email address",
                    html
            );

        } catch (Exception e) {
            log.error("Exception occured in the sendVerificationEmail():{}",e.getMessage());

            throw new RuntimeException(
                    "Failed to send verification email : " +
                            e.getMessage()
            );
        }
    }

    private User convertToUser(RegisterRequest request) {

        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .profileImageUrl(request.getProfileImageUrl())
                .subscriptionPlan("Basic")
                .emailVerified(false)
                .verificationToken(UUID.randomUUID().toString())
                .verificationExpires(LocalDateTime.now().plusHours(24))
                .build();
    }

    private AuthResponse convertToResponse(User newUser) {

        return AuthResponse.builder()
                .id(newUser.getId())
                .name(newUser.getName())
                .email(newUser.getEmail()) // FIXED
                .profileImageUrl(newUser.getProfileImageUrl())
                .subscriptionPlan(newUser.getSubscriptionPlan())
                .emailVerified(newUser.isEmailVerified())
                .createdAt(newUser.getCreatedAt())
                .updatedAt(newUser.getUpdatedAt())

                .build();
    }
    public void verifyEmail(String token){
        log.info("inside AuthService :verifyEmail():{}",token);
        User user= userRepository.findByVerificationToken(token)
                .orElseThrow(()->new RuntimeException("Invalid or expired token"));
        if (user.getVerificationExpires()!=null&&user.getVerificationExpires().isBefore(LocalDateTime.now())){
            throw new RuntimeException("verification toke  has expired please request a new one");

        }
        user.setEmailVerified(true);
        user.setVerificationToken(null);
        user.setVerificationExpires(null);
        userRepository.save(user);

    }

    public AuthResponse login(LoginRequest loginRequest){
       User existingUser= userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(()->new UsernameNotFoundException("invalid email or password"));
       if (!passwordEncoder.matches(loginRequest.getPassword(),existingUser.getPassword()))
           throw new UsernameNotFoundException("invalid email or password");
       if (!existingUser.isEmailVerified())throw new RuntimeException("please verify your email before logging in again ");


    String token=jwtUtil.generateToken(existingUser.getId());
   AuthResponse returnValue= convertToResponse( existingUser);
   returnValue.setToken(token);
   return returnValue;
    }

    public void resendVerification(String email) {
      User user=  userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found "));
      if(user.isEmailVerified()){
          throw new RuntimeException("email is already verified ");
      }
      user.setVerificationToken(UUID.randomUUID().toString());
      user.setVerificationExpires(LocalDateTime.now().plusHours(24));
      userRepository.save(user);
      sendVerificationEmail(user);
    }

    public AuthResponse getProfile(Object principleObject) {
       User existingUser= (User)principleObject;
       return convertToResponse(existingUser);


    }
}