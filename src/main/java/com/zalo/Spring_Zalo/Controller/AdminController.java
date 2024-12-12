package com.zalo.Spring_Zalo.Controller;

import com.zalo.Spring_Zalo.DTO.UserDto;
import com.zalo.Spring_Zalo.Exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.zalo.Spring_Zalo.Entities.RefeshToken;
import com.zalo.Spring_Zalo.Entities.Roles;
import com.zalo.Spring_Zalo.Entities.User;
import com.zalo.Spring_Zalo.JWT.CustomUserDetailsService;
import com.zalo.Spring_Zalo.JWT.JWTAuthenticationFilter;
import com.zalo.Spring_Zalo.JWT.JWTGenerator;
import com.zalo.Spring_Zalo.Repo.RolesMongoRepo;
import com.zalo.Spring_Zalo.Repo.UserMongoRepo;
import com.zalo.Spring_Zalo.Response.ApiResponse;
import com.zalo.Spring_Zalo.Response.LoginResponse;
import com.zalo.Spring_Zalo.Service.RefeshTokenService;
import com.zalo.Spring_Zalo.Service.SequenceGeneratorService;
import com.zalo.Spring_Zalo.Service.UserService;
import com.zalo.Spring_Zalo.request.UserRequestLogin;
import com.zalo.Spring_Zalo.request.UserRequestSignUp;
import com.zalo.Spring_Zalo.utils.PasswordUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMongoRepo userRepo;
    @Autowired
    private RolesMongoRepo roleRepo;
    @Autowired
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    private JWTGenerator tokenGenerator;

    @Autowired
    private RefeshTokenService refeshTokenService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> loginAdmin(@RequestBody UserRequestLogin user) {
        System.out.println("////////////////" + user.getUsername());
        return userService.AuthorizeUser(user);

    }

    @PostMapping("/register")
    public ApiResponse RegisterAccount(@RequestBody UserRequestSignUp entity) {
        if (entity == null) {
            return new ApiResponse("Đăng kí thất bại do thiếu thông tin", false, 403);
        }else if (userRepo.findByUserName(entity.getUsername()) != null) {
            return new ApiResponse("Đăng kí thất bại do đã tồn tại tài khoản ", false, 409);
        }else if (userRepo.findByEmail(entity.getEmail()) != null) {
            return new ApiResponse("Đăng kí thất bại do email tồn tại", false, 409);
        }

        User user = new User();
        String encryptedPassword = PasswordUtils.encryptPassword(entity.getPassword()); // move to function encrypt
                                                                                        // password
        user.setId(sequenceGeneratorService.generateSequence(User.SEQUENCE_NAME));
        user.setUsername(entity.getUsername());
        user.setPassword(encryptedPassword);
        user.setEmail(entity.getEmail());
        user.setFullname(entity.getUsername());

        user.setAvatar("https://i.pinimg.com/736x/0c/ce/24/0cce244c8456e9632233b1921450f5af.jpg");// default the avatar

        Roles roles = new Roles();
        roles.setId(sequenceGeneratorService.generateSequence(Roles.SEQUENCE_NAME));
        if(entity.getRole().equals("Admin")){
            roles.setName("Admin");
        }else{
            roles.setName("User");
        }

        roles.setUser(user);
//        roleRepo.save(roles);
        user.setCreateAt(LocalDateTime.now());
        user.setRole(roles);
        userRepo.save(user);
        roleRepo.save(roles);
        return new ApiResponse("Đăng kí thành công", true, 200);
    }

    @PostMapping("/registerAdmin")
    public ApiResponse RegisterAccountAdmin(@RequestBody UserRequestSignUp entity) {
        if (entity == null) {
            return new ApiResponse("Đăng kí thất bại do thiếu thông tin", false, 403);
        }

        if (userRepo.findByUserName(entity.getUsername()) != null) {
            return new ApiResponse("Đăng kí thất bại do đã tồn tại tài khoản ", false, 409);
        }
        User user = new User();
        String encryptedPassword = PasswordUtils.encryptPassword(entity.getPassword()); // move to function encrypt
        // password
        user.setId(sequenceGeneratorService.generateSequence(User.SEQUENCE_NAME));
        user.setUsername(entity.getUsername());
        user.setPassword(encryptedPassword);
        user.setEmail(entity.getEmail());
        user.setFullname(entity.getUsername());
        user.setCreateAt(LocalDateTime.now());
        user.setAvatar("https://i.pinimg.com/736x/0c/ce/24/0cce244c8456e9632233b1921450f5af.jpg");// default the avatar

        Roles roles = new Roles();
        roles.setId(sequenceGeneratorService.generateSequence(Roles.SEQUENCE_NAME));
        roles.setName("User");
        roles.setUser(user);
//        roleRepo.save(roles);

        user.setRole(roles);
        userRepo.save(user);
        roleRepo.save(roles);
        return new ApiResponse("Đăng kí thành công", true, 200);
    }

    @PutMapping("/updateUserData")
    public ResponseEntity<?> updateUserData(@RequestBody UserDto userUpdate) {
        User user = userRepo.findById(userUpdate.getId()).orElseThrow(() -> new ResourceNotFoundException("user", "id", userUpdate.getId()));
        user.setFullname(userUpdate.getFullname());
        if (userUpdate.getEmail() != null && !userUpdate.getEmail().isEmpty() && !userUpdate.getEmail().equals(user.getEmail())) {
            User userByEmail = userRepo.findByEmail(userUpdate.getEmail());
            if (userByEmail != null) {
                return new ResponseEntity<>(new ApiResponse("Đăng kí thất bại do Email đã tồn tại ", false, 408),HttpStatus.BAD_REQUEST);
            }
            user.setEmail(userUpdate.getEmail());
        }
        if(userUpdate.getPassword() != null && !userUpdate.getPassword().isEmpty()){
            user.setPassword(PasswordUtils.encryptPassword(userUpdate.getPassword()));
        }
        if(!userUpdate.getRole().equals(user.getRole())){
            roleRepo.removeUserFromRoleByUserId(user.getId());
            Roles roles = new Roles();
            roles.setId(sequenceGeneratorService.generateSequence(Roles.SEQUENCE_NAME));
            roles.setName(userUpdate.getRole());
            roles.setUser(user);

            user.setRole(roles);
            userRepo.save(user);
            roleRepo.save(roles);

        }else{
            userRepo.save(user);
        }
        ApiResponse userResponse = new ApiResponse("Update success", true, 200);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);

    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> delete(@PathVariable Integer userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user", "id", userId));

        roleRepo.removeUserFromRoleByUserId(userId);
        userRepo.delete(user);

        ApiResponse userResponse = new ApiResponse("Delete success", true, 200);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);

    }
    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String refeshToken = request.getHeader("rereshToken");
        System.out.println("refeshToken: " + refeshToken);
        RefeshToken refeshTokenDB = refeshTokenService.findByRefreshToken(refeshToken);
        System.out.println("refeshTokenDB: "+refeshTokenDB.toString());
        refeshTokenService.deleteToken(refeshTokenDB);
        return new ResponseEntity<>(new ApiResponse("Logout success", true, 200), HttpStatus.OK);
    }

    @PostMapping("/refeshToken")
    public ResponseEntity<?> refeshToken(HttpServletRequest request, HttpServletResponse response) {
        String refeshToken = request.getHeader("Authorization");
        RefeshToken refeshTokenDB = refeshTokenService.findByRefreshToken(refeshToken);
        User user = jwtAuthenticationFilter.getUserFromJWT(refeshToken);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getUsername());
        if (refeshTokenDB.getUser().getId() == user.getId()) {
            if (refeshTokenService.verifyExpiration(refeshTokenDB)) {
                return new ResponseEntity<>(new ApiResponse("Refresh Token expired", true, 400),
                        HttpStatus.BAD_REQUEST);
            }
            refeshTokenService.deleteToken(refeshTokenDB);
            String newAccessToken = tokenGenerator.generateAccessToken(userDetails, user);
            String newRefreshToken = tokenGenerator.generateRefreshToken(userDetails, user);
            response.addHeader("Authorization", "Bearer " + newAccessToken);
            response.addHeader("Refresh-Token", newRefreshToken);

            RefeshToken newRefeshToken = new RefeshToken();
            newRefeshToken.setRefreshToken(newRefreshToken);
            newRefeshToken.setUser(user);
            LoginResponse loginResponse = new LoginResponse(newAccessToken, newRefreshToken);
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse("Error Refresh Token", true, 400), HttpStatus.BAD_REQUEST);
    }
}
