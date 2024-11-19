package com.maveric.bank.service;

import org.springframework.stereotype.Service;
import java.util.*;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.maveric.bank.JWT.CustomerUserDetailsService;
import com.maveric.bank.JWT.JwtFilter;
import com.maveric.bank.JWT.JwtUtils;
import com.maveric.bank.entity.User;
import com.maveric.bank.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService implements IUserService {

    private static final String PHONE_REGEX = "^\\+[1-9]\\d{1,14}$"; // E.164 format for phone numbers with country code
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);

    private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"; // Email format
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private static final List<String> ALLOWED_ROLES = Arrays.asList("superadmin", "admin", "customer");

    private static final String PASSWORD_REGEX = 
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,20}$"; // Strong password regex
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomerUserDetailsService customerUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    JwtUtils jwtUtils;

    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        try {
            // Validate signup data
            String validationMessage = validateSignUp(requestMap);
            if (!validationMessage.isEmpty()) {
                return new ResponseEntity<>(validationMessage, HttpStatus.BAD_REQUEST);
            }

            User user = userRepository.findByEmail(requestMap.get("email"));
            if (Objects.isNull(user)) {
                userRepository.save(getUserFromMap(requestMap));
                return new ResponseEntity<>("Successfully Registered", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.error("Error during sign-up", ex);
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String validateSignUp(Map<String, String> requestMap) {
        if (!requestMap.containsKey("name") ||
            !requestMap.containsKey("contactNumber") ||
            !requestMap.containsKey("email") ||
            !requestMap.containsKey("password") ||
            !requestMap.containsKey("role")) {
            return "Missing required fields";
        }

        // Validate email format
        if (!EMAIL_PATTERN.matcher(requestMap.get("email")).matches()) {
            return "Invalid email format";
        }

        // Validate contact number format with country code
        if (!PHONE_PATTERN.matcher(requestMap.get("contactNumber")).matches()) {
            return "Invalid contact number. Must include country code";
        }

        // Validate password strength
        if (!PASSWORD_PATTERN.matcher(requestMap.get("password")).matches()) {
            return "Password not strong enough. It must contain at least one digit, one lowercase letter, one uppercase letter, one special character, and be between 8-20 characters";
        }

        // Validate role
        if (!ALLOWED_ROLES.contains(requestMap.get("role").toLowerCase())) {
            return "Invalid role. Allowed roles are superadmin, admin, or customer";
        }

        return "";
    }

    private User getUserFromMap(Map<String, String> requestMap) {
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(passwordEncoder.encode(requestMap.get("password")));
        user.setStatus("false"); // User must wait for superadmin approval
        List<String> roles = Arrays.asList(requestMap.get("role").toUpperCase());
        user.setRoles(roles);
      //  user.setRoles(Arrays.asList(requestMap.get("role")));
        return user;
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login");

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestMap.get("email"),
                            requestMap.get("password"))
            );

            if (auth.isAuthenticated()) {
                if (customerUserDetailsService.getuserDetails().getStatus().equalsIgnoreCase("true")) {
                    String token = jwtUtils.generateToken(
                            customerUserDetailsService.getuserDetails().getEmail(),
                            customerUserDetailsService.getuserDetails().getRoles()
                    );
                    return new ResponseEntity<>("{ \"token\": \"" + token + "\" }", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("{ \"message\": \"Wait for Superadmin approval.\" }", HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception e) {
            log.error("Login failed: ", e);
        }
        return new ResponseEntity<>("{ \"message\": \"Bad Credentials.\" }", HttpStatus.BAD_REQUEST);
    }
}
