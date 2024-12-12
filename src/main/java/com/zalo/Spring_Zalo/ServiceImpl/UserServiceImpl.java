package com.zalo.Spring_Zalo.ServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.zalo.Spring_Zalo.Entities.User;
import com.zalo.Spring_Zalo.JWT.CustomUserDetailsService;
import com.zalo.Spring_Zalo.JWT.JWTGenerator;
import com.zalo.Spring_Zalo.Password_User_Web.PasswordUtils;
import com.zalo.Spring_Zalo.Repo.UserMongoRepo;
import com.zalo.Spring_Zalo.Response.AdminLoginResponse;
import com.zalo.Spring_Zalo.Response.LoginResponse;
import com.zalo.Spring_Zalo.Response.UserInfoResponse;
import com.zalo.Spring_Zalo.Service.UserService;
import com.zalo.Spring_Zalo.request.UserRequestLogin;

@Service
public class UserServiceImpl implements UserService {

    private final UserMongoRepo userRepo;
    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    
    @Autowired
    public UserServiceImpl(UserMongoRepo userRepo, CustomUserDetailsService customUserDetailsService, JWTGenerator jwtGenerator) {
        this.userRepo = userRepo;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtGenerator = jwtGenerator;
    }

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    private final JWTGenerator jwtGenerator;

    @Override
    public ResponseEntity<?> AuthorizeUser(UserRequestLogin user) {
        User userAuthorize = userRepo.findByUserName(user.getUsername());
        if (userAuthorize!= null) {
            logger.info(">>check login");
            User authenticatedUser = userAuthorize;
             
//            if (authenticatedUser.getStatus().equals(EnumManager.UserStatus.WAITING_FOR_APPROVAL) || authenticatedUser.getStatus().equals(EnumManager.UserStatus.DECLINED)) {
//                return ResponseEntity.badRequest().body(new AdminLoginResponse("Tài khoản của bạn đã bị khóa hoặc chưa kích hoạt", false, 401, 0));
//            }
            // checking password
            String passwordCheck = PasswordUtils.decryptPassword(authenticatedUser.getPassword());
            if (!passwordCheck.equals(user.getPassword())) {
                return ResponseEntity.ok().body(new AdminLoginResponse("Sai thông tin tài khoản hoặc mật khẩu ! ", false, 401, 0));
            }
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(authenticatedUser.getUsername());
           
            String accesstoken = jwtGenerator.generateAccessToken(userDetails,authenticatedUser);
            String refeshtoken = jwtGenerator.generateRefreshToken(userDetails,authenticatedUser);
            logger.info(">> Access : " + accesstoken);
            logger.info(">> Refesh : " + refeshtoken);
            UserInfoResponse userReady = new UserInfoResponse(authenticatedUser.getId(), authenticatedUser.getUsername(), authenticatedUser.getEmail(),authenticatedUser
            .getAvatar(),
             authenticatedUser.getStatus(), authenticatedUser.getRole().getName(),authenticatedUser.getFullname());
            return ResponseEntity.ok(new LoginResponse("Đăng nhập thành công", true, 200,userReady, accesstoken, refeshtoken));
        }
        return ResponseEntity.ok().body(new AdminLoginResponse("Tên đăng nhập hoặc mật khẩu không đúng", false, 401, 0));
    }

    @Override
    public User authenticateUser(String username, String password) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'authenticateUser'");
    }
}
