package mx.edu.utez.mexprotec.segurity.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.mexprotec.segurity.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenFilter.class);
    final JwtProvider provider;
    final AuthService service;
    

    @Autowired
    private JwtTokenFilter(JwtProvider provider, AuthService service) {
        this.provider = provider;
        this.service = service;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        try{
            String token = getToken(request);
            if(token != null && provider.validateToken(token)){
                String email = provider.getUsernameFromToken(token);
                UserDetails userDetails = service.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                SecurityContextHolder.getContext()
                        .setAuthentication(auth);
            }
        }catch (Exception e){
            LOGGER.error("Error filterInternal -> " ,e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    public String getToken(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        if(token != null && token.startsWith("Bearer"))
            return token.replace("Bearer", "");
        return null;
    }
}
