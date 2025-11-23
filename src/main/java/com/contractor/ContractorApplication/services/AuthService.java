package com.contractor.ContractorApplication.services;

import com.contractor.ContractorApplication.dtos.AuthResponse;
import com.contractor.ContractorApplication.entities.PasswordResetToken;
import com.contractor.ContractorApplication.entities.User;
import com.contractor.ContractorApplication.repositories.PasswordResetTokenRepository;
import com.contractor.ContractorApplication.repositories.UserRepository;
import com.contractor.ContractorApplication.util.JwtUtil;
import com.contractor.ContractorApplication.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private PasswordUtil passwordUtil;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${password.reset.token.expiry.hours:24}")
    private int passwordResetTokenExpiryHours;

    public AuthResponse registerUser(String mobileNumber, String fullName, String password) {
        if (userRepository.existsByMobileNumber(mobileNumber)) {
            throw new RuntimeException("User with this mobile number already exists");
        }

        if (!isValidMobileNumber(mobileNumber)) {
            throw new RuntimeException("Invalid mobile number format");
        }

        String hashedPassword = passwordUtil.hashPassword(password);
        User user = new User(mobileNumber, fullName, hashedPassword);
        User savedUser = userRepository.save(user);

        String token = jwtUtil.generateToken(savedUser.getMobileNumber(), savedUser.getId());

        return new AuthResponse("User registered successfully", token, savedUser);
    }

    public AuthResponse loginUser(String mobileNumber, String password) {
        Optional<User> userOptional = userRepository.findByMobileNumberAndIsActiveTrue(mobileNumber);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found or inactive");
        }

        User user = userOptional.get();

        if (!passwordUtil.verifyPassword(password, user.getPasswordHash())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getMobileNumber(), user.getId());

        return new AuthResponse("Login successful", token, user);
    }

    public void initiatePasswordReset(String mobileNumber) {
        Optional<User> userOptional = userRepository.findByMobileNumber(mobileNumber);
        if (userOptional.isEmpty()) {
            // Don't reveal whether user exists or not for security
            return;
        }

        User user = userOptional.get();

        // Invalidate any existing tokens for this user
        passwordResetTokenRepository.deleteByUser(user);

        // Generate new token
        String resetToken = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusHours(passwordResetTokenExpiryHours);

        PasswordResetToken passwordResetToken = new PasswordResetToken(resetToken, user, expiryDate);
        passwordResetTokenRepository.save(passwordResetToken);

        // In a real application, you would send this token via SMS/Email
        // For now, we'll just return it in the response for testing
        System.out.println("Password reset token for " + mobileNumber + ": " + resetToken);
    }

    public boolean validateResetToken(String token) {
        Optional<PasswordResetToken> tokenOptional = passwordResetTokenRepository.findByToken(token);

        if (tokenOptional.isEmpty()) {
            return false;
        }

        PasswordResetToken resetToken = tokenOptional.get();

        if (resetToken.getIsUsed() || resetToken.isExpired()) {
            return false;
        }

        return true;
    }

    public void resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> tokenOptional = passwordResetTokenRepository.findByToken(token);

        if (tokenOptional.isEmpty()) {
            throw new RuntimeException("Invalid reset token");
        }

        PasswordResetToken resetToken = tokenOptional.get();

        if (resetToken.getIsUsed()) {
            throw new RuntimeException("Reset token already used");
        }

        if (resetToken.isExpired()) {
            throw new RuntimeException("Reset token expired");
        }

        User user = resetToken.getUser();
        String hashedPassword = passwordUtil.hashPassword(newPassword);
        user.setPasswordHash(hashedPassword);
        userRepository.save(user);

        // Mark token as used
        resetToken.setIsUsed(true);
        passwordResetTokenRepository.save(resetToken);
    }

    public void changePassword(Long userId, String currentPassword, String newPassword) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOptional.get();

        if (!passwordUtil.verifyPassword(currentPassword, user.getPasswordHash())) {
            throw new RuntimeException("Current password is incorrect");
        }

        String hashedPassword = passwordUtil.hashPassword(newPassword);
        user.setPasswordHash(hashedPassword);
        userRepository.save(user);
    }

    private boolean isValidMobileNumber(String mobileNumber) {
        return mobileNumber != null && mobileNumber.matches("\\d{10,15}");
    }

}