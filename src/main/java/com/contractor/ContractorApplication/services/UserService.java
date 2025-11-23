package com.contractor.ContractorApplication.services;

import com.contractor.ContractorApplication.entities.User;
import com.contractor.ContractorApplication.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof String) {
            String mobileNumber = (String) principal;
            return userRepository.findByMobileNumber(mobileNumber);
        }

        return Optional.empty();
    }

    public Long getCurrentUserId() {
        Object details = SecurityContextHolder.getContext().getAuthentication().getDetails();

        if (details instanceof Map) {
            Map<?, ?> detailsMap = (Map<?, ?>) details;
            Object userIdObj = detailsMap.get("userId");
            if (userIdObj instanceof Long) {
                return (Long) userIdObj;
            } else if (userIdObj instanceof Integer) {
                return ((Integer) userIdObj).longValue();
            }
        }

        // Fallback: try to get from user repository
        Optional<User> currentUser = getCurrentUser();
        return currentUser.map(User::getId).orElse(null);
    }
}
