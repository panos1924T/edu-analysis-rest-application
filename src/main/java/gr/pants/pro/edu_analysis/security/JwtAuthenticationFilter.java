package gr.pants.pro.edu_analysis.security;

import gr.pants.pro.edu_analysis.authentication.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        String username = "";

        // If the request has no JWT token, continue to the next filter.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7).trim();
        try {
            username = jwtService.extractSubject(jwt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (!jwtService.isTokenValid(jwt, userDetails)) {
                    throw new BadCredentialsException("Invalid Token");
                }

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                System.out.println("principal class = " + authentication.getPrincipal().getClass());
                System.out.println("authorities = " + authentication.getAuthorities());
            }

        } catch (ExpiredJwtException e) {
        // triggers το AuthenticationEntryPoint 401
        throw new AuthenticationCredentialsNotFoundException("Expired token", e);

        } catch (JwtException | IllegalArgumentException e) {
        // triggers το AuthenticationEntryPoint 401
        throw new BadCredentialsException("Invalid token");

        } catch (BadCredentialsException e) {
        // Just leave it to move to the next filter
        // Otherwise will be caught by generic exception handler
        throw e;

        } catch (Exception e) {
        log.error("Unexpected error during token validation", e);
        throw new AuthenticationCredentialsNotFoundException("Token validation failed", e);
        }

        /*
         / UsernameNotFoundException hits catch (Exception e) →
         / AuthenticationCredentialsNotFoundException → 401.
         / Clean and no duplication
        */

        filterChain.doFilter(request, response);
    }
}
