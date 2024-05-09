package com.azizo.book.auth;


import com.azizo.book.email.EmailService;
import com.azizo.book.email.EmailTemplateName;
import com.azizo.book.role.RoleRepository;
import com.azizo.book.user.Token;
import com.azizo.book.user.TokenRepository;
import com.azizo.book.user.UserRepository;
import com.azizo.book.user.Users;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    public void register(RegistrationRequest request) throws MessagingException {
        // Try to get the user role and if we don't find it we throw an exception

        var userRole = roleRepository.findByName("USER")
                 // todo - better exception handling
                 .orElseThrow(() -> new IllegalStateException("ROLE USER was not intialized"));
        // create the user object
         var user = Users.builder()
                 .firstname(request.getFirstname())
                         .lastname(request.getLastname())
                 .email(request.getEmail())
                 //encode the password
                 .password(passwordEncoder.encode(request.getPassword()))
                 .accountLocked(false)
                 // set enabled to false
                 // because by default the account should not be enabled
                 // unless the user enable it using the activation code
                  // that we will send using the sentValidationEmail()
                 .enabled(false)
                 .roles(List.of(userRole))
                 .build();
         //we persist the user in the database
         userRepository.save(user);
         sentValidationEmail(user);


    }

    private void sentValidationEmail(Users user) throws MessagingException {
       //generate a token
        var newToken = generateAndSaveActivationToken(user);

        emailService.sendEmail(
                user.getEmail(),
                user.fullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"
                );



    }

    private String generateAndSaveActivationToken(Users user) {
        // generate a token
        String generatedToken = generateActivationCode(6);
        //create a token object
        var token = Token.builder()
                .token(generatedToken)
                .createAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                // the user who the token is assigned
                .user(user)
                .build();
        // save the token to the database
        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        // String builder to store the result
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i =0; i< length; i++){
            // secureRandom to generate a secure integer using the length of characters
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }
}
