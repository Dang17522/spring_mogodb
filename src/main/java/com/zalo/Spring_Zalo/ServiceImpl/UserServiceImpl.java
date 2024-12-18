package com.zalo.Spring_Zalo.ServiceImpl;

import com.zalo.Spring_Zalo.Entities.Roles;
import com.zalo.Spring_Zalo.Repo.RolesMongoRepo;
import com.zalo.Spring_Zalo.Service.SequenceGeneratorService;
import com.zalo.Spring_Zalo.request.UserRequestImport;
import com.zalo.Spring_Zalo.utils.ExcelHelper;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

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

    @Autowired
    private ExcelHelper excelHelper;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    private RolesMongoRepo roleRepo;

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

    @Override
    public boolean importData(MultipartFile file) {
        try {
            List<UserRequestImport> list = ExcelHelper.excelToTutorials(file.getInputStream());
            if (checkData(list)) {
                for (UserRequestImport user : list) {
                    Roles role = Roles.builder()
                            .id(sequenceGeneratorService.generateSequence(Roles.SEQUENCE_NAME))
                            .name(user.getRole())
                            .build();

                    User u = User.builder()
                            .id(sequenceGeneratorService.generateSequence(User.SEQUENCE_NAME))
                            .username(user.getUsername())
                            .fullname(user.getFullname())
                            .email(user.getEmail())
                            .password(PasswordUtils.encryptPassword(user.getPassword()))
                            .role(role)
                            .createAt(LocalDateTime.now())
                            .avatar("https://i.pinimg.com/736x/0c/ce/24/0cce244c8456e9632233b1921450f5af.jpg")
                            .status(1)
                            .build();

                    userRepo.save(u);
                    roleRepo.save(role);

                }
            }else{
                return false;
            }
            return true;
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }

    @Override
    public boolean checkData(List<UserRequestImport> list) {
        for (UserRequestImport user : list) {
            if (userRepo.findByUserName(user.getUsername()) != null) {
                return false;
            }
            if(userRepo.findByEmail(user.getEmail()) != null) {
                return false;
            }
        }
        return true;
    }
}
