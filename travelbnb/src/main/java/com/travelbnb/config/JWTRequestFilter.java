package com.travelbnb.config;

import com.auth0.jwt.exceptions.MissingClaimException;
import com.travelbnb.Entity.AppUser;
import com.travelbnb.Service.JWTService;
import com.travelbnb.repository.AppUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    private JWTService jwtService;
    private AppUserRepository appUserRepository;

    public JWTRequestFilter(JWTService jwtService, AppUserRepository appUserRepository) {
        this.jwtService = jwtService;

        this.appUserRepository = appUserRepository;
    }

    //@Component make your ordinary java class into spring boot class.
    // JwtRequestFilter: This is your custom filter responsible for JWT authentication.
    //It extends GenericFilterBean and overrides doFilter method to implement your JWT authentication logic.
    //this method will start taking incoming http url and that url has the token, present in  authorization header of the url.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String tokenHeader = request.getHeader("Authorization");

        if (tokenHeader != null && tokenHeader.startsWith("Bearer")) {
            System.out.println(tokenHeader);
            String token = tokenHeader.substring(8,tokenHeader.length()-1);
            System.out.println(token);
            String userName = jwtService.getUsername(token);
//            System.out.println(userName);
            Optional<AppUser> opUsername = appUserRepository.findByUsername(userName);
            if (opUsername.isPresent()){
                AppUser appUser = opUsername.get();

                UsernamePasswordAuthenticationToken authentication
                        = new UsernamePasswordAuthenticationToken(appUser,null,
                        Collections.singleton(new SimpleGrantedAuthority(appUser.getRole())));
                authentication.setDetails(new WebAuthenticationDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

//                A new UsernamePasswordAuthenticationToken is created using the AppUser object. It includes the user's authorities (roles).
//                authentication.setDetails(new WebAuthenticationDetails(request)); sets additional details about the request.
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//                sets the authentication object in the security context, effectively logging in the user for this request.
                /*
                Collections.singleton(new SimpleGrantedAuthority(appUser.getRole())):
                appUser.getRole(): This method retrieves the role of the appUser. Let's assume the role is a string like "ROLE_USER".
                new SimpleGrantedAuthority(appUser.getRole()): This creates a SimpleGrantedAuthority object with the role retrieved.
                SimpleGrantedAuthority is a class provided by Spring Security to represent an authority granted to an Authentication object.
                Collections.singleton(...): This wraps the SimpleGrantedAuthority object in an immutable set.
                This set will contain only one element, which is the SimpleGrantedAuthority object representing the user's role.
                 */
            }
        }
            filterChain.doFilter(request, response);
    }

}

