package org.office.config;


import org.office.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                       HttpServletResponse response,
                                       Authentication authentication) throws IOException, ServletException {
        
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        
        
        userService.findByEmail(email).ifPresent(user -> {
            userService.setActive(user);
            userService.loginRecord(user);
        });
        
        
        String redirectUrl = "/";
        java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> authorities = authentication.getAuthorities();
        System.out.println("LOGIN DEBUG: User " + email + " has authorities: " + authorities);

        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_STAFF"))) {
            redirectUrl = "/staff/dashboard";
        } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRATOR"))) {
            redirectUrl = "/admin/dashboard";
        }
        
        System.out.println("LOGIN DEBUG: Redirecting to " + redirectUrl);
        response.sendRedirect(redirectUrl);
    }
}

