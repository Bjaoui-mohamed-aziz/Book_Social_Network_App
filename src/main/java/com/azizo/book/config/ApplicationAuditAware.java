package com.azizo.book.config;

import com.azizo.book.user.Users;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;


//The ApplicationAuditAware class aims to provide current user information
// for auditing mechanisms in a Spring application.
public class ApplicationAuditAware implements AuditorAware<Integer> {

    @Override
    public Optional<Integer> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken){
            return Optional.empty();
        }
        Users userPrincipal = (Users) authentication.getPrincipal();
        return Optional.ofNullable(userPrincipal.getId());
    }
}