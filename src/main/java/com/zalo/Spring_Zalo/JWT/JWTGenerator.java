package com.zalo.Spring_Zalo.JWT;
import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zalo.Spring_Zalo.Entities.RefeshToken;
import com.zalo.Spring_Zalo.Entities.User;
import com.zalo.Spring_Zalo.Repo.RefeshTokenMongoRepo;
import com.zalo.Spring_Zalo.Response.ApiResponse;
import com.zalo.Spring_Zalo.Service.SequenceGeneratorService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTGenerator {

    @Autowired
    private RefeshTokenMongoRepo refeshTokenRepo;
    private static final Logger log = LoggerFactory.getLogger(JWTGenerator.class);

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;
    

    // tạo chuỗi JWT
    /**
     *
     * @param authentication
     * @return
     */
    public String generateAccessToken(UserDetails  authentication , User user) {
       // String username = authentication.getName();
        Date currentDate = new Date(); // set thời điểm tạo token
        Date expireDate = new Date(currentDate.getTime() + SecurityContaints.JWT_ACCESS_TOKEN_EXPIRATON);// set thời
                                                                                                         // điểm hết hạn
                                                                                                         // token
        String accesstoken = Jwts.builder() // sử dụng jwts để xây dựng chuỗi
                .setSubject(user.getRole().toString())
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .claim("userName", user.getUsername())
                .claim("uid", user.getId())
                .claim("email", user.getEmail())
                .claim("rid", user.getRole().getId())
                .signWith(SignatureAlgorithm.HS512, SecurityContaints.JWT_SECRET)
                .compact();

        return accesstoken;
    }

    /**
     *
     * @param authentication
     * @return
     */
    public String generateRefreshToken(UserDetails  authentication,User user) {
        // String username = authentication.getName();

        // Tạo chuỗi RefreshToken với thời gian hết hạn lâu hơn AccessToken
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + SecurityContaints.JWT_REFRESH_TOKEN_EXPIRATION);

        String refreshToken = Jwts.builder()
                .setSubject(String.valueOf(user.getRole().getName()))
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .claim("uid", user.getId())
                .claim("userName", user.getUsername())
                .claim("rid", user.getRole().getId())
                .signWith(SignatureAlgorithm.HS512, SecurityContaints.JWT_SECRET)
                .compact();

        RefeshToken refeshToken = RefeshToken.builder()
                .id(sequenceGeneratorService.generateSequence(RefeshToken.SEQUENCE_NAME))
                .user(user)
                .refreshToken(refreshToken)
                .expiryDate(expireDate.toInstant())
                .build();
        refeshTokenRepo.save(refeshToken);
        return refreshToken;
    }

    /**
     *
     * @param token
     * @return
     */
    public String getUsernameFromJWT(String token) {
        // Giải mã JWT và lấy thông tin chủ thể
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityContaints.JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        // Trả về tên người dùng từ thông tin chủ thể
        return claims.getSubject();
    }

    /**
     *
     * @param token
     * @return
     */
    public boolean validateToken(String token) throws IOException {
        try {
            Jwts.parser().setSigningKey(SecurityContaints.JWT_SECRET).parseClaimsJws(token);
            log.info("JWT successfully validated");
            return true;
        } catch (ExpiredJwtException ex){
            log.info("Token expired", ex.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ApiResponse apiResponse = new ApiResponse("Token expired", false, 401);
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(apiResponse);
            response.setContentType("application/json");
            response.getWriter().write(jsonResponse);
//            throw new MalformedJwtException("MalformedJwtException", ex);
            return false;
        }catch (Exception e) {
            log.info("JWT validation failed: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ApiResponse apiResponse = new ApiResponse("JWT was incorrect",false, 401);
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(apiResponse);
            response.setContentType("application/json");
            response.getWriter().write(jsonResponse);
//            throw new AuthenticationCredentialsNotFoundException("JWT was incorrect");
            return false;
        }


    }

    /**
     *
     * @param token
     * @return
     */
    public Date getExpirationDateFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityContaints.JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration();
    }
}
