package org.springboot.validationUtil;

import org.springboot.model.UserInfoDto;
import org.springframework.stereotype.Service;

@Service
public class ValidationUtil {

    public boolean validateAllFields(UserInfoDto user) {
        if (!isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Invalid email");
        }
        if (!isValidPassword(user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }
        if (!isValidUsername(user.getUsername())) {
            throw new IllegalArgumentException("Invalid username");
        }
        if (!isValidLastname(user.getLastname())) {
            throw new IllegalArgumentException("Invalid last name");
        }
        if (!isValidPhoneNumber(user.getPhoneNumber())) {
            throw new IllegalArgumentException("Invalid phone number");
        }

        return true;
    }

    public boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    // Validate Password (at least 8 characters, 1 digit, 1 special character, 1 uppercase)
    public boolean isValidPassword(String password) {
        return password != null && password.matches(
                "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$"
        );
    }

    // Validate Username (3–20 chars, alphanumeric + underscores)
    public boolean isValidUsername(String username) {
        return username != null && username.matches("^[A-Za-z0-9_]{3,20}$");
    }

    // Validate Name (letters and spaces only, 2–50 characters)
    public boolean isValidLastname(String name) {
        return name != null && name.matches("^[A-Za-z ]{2,50}$");
    }

    // Validate Phone Number (10 digits)
    public boolean isValidPhoneNumber(Long phone) {
        return phone != null && phone.toString().matches("^[0-9]{10}$");
    }

    // Validate Boolean flags (null check)
    public boolean isValidBoolean(Boolean value) {
        return value != null;
    }

    // Generic non-empty string check
    public boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
