package com.zalo.Spring_Zalo.JWT; 

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.zalo.Spring_Zalo.Entities.Roles;
import com.zalo.Spring_Zalo.Entities.User;
import com.zalo.Spring_Zalo.Repo.RolesMongoRepo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTGenerator tokenGenerator;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private RolesMongoRepo rolesRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getJWTFromRequest(request);
            logger.info("Received token: {}"+ token);
            if (StringUtils.hasText(token) && tokenGenerator.validateToken(token)) {
                User user = getUserFromJWT(token);
                // String username = tokenGenerator.getUsernameFromJWT(token);
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getUsername());
                logger.info("User authorities: {}"+ userDetails.getAuthorities());
                if (tokenExpired(token)) {
//                    // Generate new tokens only if the existing one is expired
//                    String newAccessToken = tokenGenerator.generateAccessToken(userDetails,user);
//                    String newRefreshToken = tokenGenerator.generateRefreshToken(userDetails,user);
//                    response.addHeader("Authorization", "Bearer " + newAccessToken);
//                    response.addHeader("Refresh-Token", newRefreshToken);
                    throw new RuntimeException("Token expired");
                }
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
            filterChain.doFilter(request, response);
        }catch (Exception e) {
            logger.error("Cannot set user authentication: {}"+ e.getMessage());
        }

    }

    public User getUserFromJWT(String token) {
    Claims claims = Jwts.parser()
            .setSigningKey(SecurityContaints.JWT_SECRET)
            .parseClaimsJws(token)
            .getBody();

    // Lấy thông tin từ claim "userDetails"
        logger.info("JWT claims: " + claims.toString());
       Roles role = rolesRepo.findById(claims.get("rid", Integer.class)).get();
       User user = new User();
       user.setId(claims.get("uid", Integer.class));
           user.setUsername(claims.get("userName", String.class));
       if(claims.get("email") != null){
            user.setEmail(claims.get("email", String.class));
       }
       user.setRole(role);
    return user;
}


    private static final long EXPIRATION_THRESHOLD = 1000; // 1 giây

    private boolean tokenExpired(String token) {
        Date expirationDate = tokenGenerator.getExpirationDateFromJWT(token);
        return expirationDate != null && expirationDate.getTime() - System.currentTimeMillis() < EXPIRATION_THRESHOLD;
    }

    public String getJWTFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
