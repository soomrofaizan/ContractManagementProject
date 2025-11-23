package com.contractor.ContractorApplication.controller;

import com.contractor.ContractorApplication.dtos.*;
import com.contractor.ContractorApplication.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest request) {
        try {
            AuthResponse response = authService.registerUser(
                    request.getMobileNumber(),
                    request.getFullName(),
                    request.getPassword()
            );

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", response.getMessage());
            responseBody.put("token", response.getToken());
            responseBody.put("user", createUserResponse(response.getUser()));

            return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);

        } catch (Exception e) {
            return createErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.loginUser(
                    request.getMobileNumber(),
                    request.getPassword()
            );

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", response.getMessage());
            responseBody.put("token", response.getToken());
            responseBody.put("user", createUserResponse(response.getUser()));

            return ResponseEntity.ok(responseBody);

        } catch (Exception e) {
            return createErrorResponse(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        try {
            authService.initiatePasswordReset(request.getMobileNumber());

            // Always return success for security (don't reveal if user exists)
            Map<String, String> response = new HashMap<>();
            response.put("message", "If the mobile number exists, a reset token has been generated");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return createErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/validate-reset-token")
    public ResponseEntity<?> validateResetToken(@RequestBody ValidateTokenRequest request) {
        try {
            boolean isValid = authService.validateResetToken(request.getToken());

            Map<String, Object> response = new HashMap<>();
            response.put("valid", isValid);

            if (!isValid) {
                response.put("message", "Invalid or expired reset token");
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return createErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            authService.resetPassword(request.getToken(), request.getNewPassword());

            Map<String, String> response = new HashMap<>();
            response.put("message", "Password reset successfully");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return createErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request,
                                            @RequestHeader("Authorization") String authHeader) {
        try {
            // Extract user ID from token
            String token = authHeader.substring(7);
            // You would need to inject JwtUtil and extract userId from token
            // For now, we'll assume the service handles this

            authService.changePassword(request.getUserId(), request.getCurrentPassword(), request.getNewPassword());

            Map<String, String> response = new HashMap<>();
            response.put("message", "Password changed successfully");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return createErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Helper methods
    private Map<String, Object> createUserResponse(com.contractor.ContractorApplication.entities.User user) {
        Map<String, Object> userResponse = new HashMap<>();
        userResponse.put("id", user.getId());
        userResponse.put("mobileNumber", user.getMobileNumber());
        userResponse.put("fullName", user.getFullName());
        return userResponse;
    }

    private ResponseEntity<Map<String, String>> createErrorResponse(String message, HttpStatus status) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", message);
        return ResponseEntity.status(status).body(errorResponse);
    }
}