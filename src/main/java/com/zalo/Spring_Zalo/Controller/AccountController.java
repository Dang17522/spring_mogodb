package com.zalo.Spring_Zalo.Controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.zalo.Spring_Zalo.DTO.UserDto;
import com.zalo.Spring_Zalo.Entities.Roles;
import com.zalo.Spring_Zalo.Entities.User;
import com.zalo.Spring_Zalo.Exception.ResourceNotFoundException;
import com.zalo.Spring_Zalo.JWT.JWTAuthenticationFilter;
import com.zalo.Spring_Zalo.Repo.RolesMongoRepo;
import com.zalo.Spring_Zalo.Repo.UserMongoRepo;
import com.zalo.Spring_Zalo.Response.ApiDataResponse;
import com.zalo.Spring_Zalo.Response.ApiResponse;
import com.zalo.Spring_Zalo.Service.CloudinaryService;
import com.zalo.Spring_Zalo.Service.UserService;
import com.zalo.Spring_Zalo.utils.ExcelHelper;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/admin/accounts")
public class AccountController {
    @Autowired
    private  UserMongoRepo userRepo;

    @Autowired
    private RolesMongoRepo roleRepo;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private UserService userService;

    @Autowired
    private ExcelHelper excelHelper;

    // @GetMapping("/")
    // public ResponseEntity<List<User>> getListAccount() {
    //     List<User> list = @Entity.findAll();
    //     if(list.isEmpty() ){
    //         throw new ResponseStatusException(404, "No account found", null);
    //     }
    //     return ResponseEntity.ok(list);
    // }


    @PutMapping("/update-account")
    public ResponseEntity<?> postMethodName(@RequestPart("id") String id,
                                            @RequestPart("fullname") String fullName,
                                            @RequestPart("email") String email,
                                            @RequestPart("companyId") String companyId,
                                            @RequestPart("roleId") String roleId,
                                            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
           User existingUser = userRepo.findById(Integer.parseInt(id)).orElseThrow(() -> new ResourceNotFoundException("user", "id", Integer.parseInt(id)));
            Roles role = roleRepo.findById(Integer.parseInt(roleId)).orElseThrow(() -> new ResourceNotFoundException("role", "id", Integer.parseInt(roleId)));
            existingUser.setFullname(fullName);
            existingUser.setEmail(email);
            existingUser.setUpdatedAt(LocalDateTime.now());

            role.setName(role.getName());
            roleRepo.save(role);

            if(file != null){
                if(existingUser.getPublicId() != null){
                    cloudinaryService.deleteImageUpload(existingUser.getPublicId());
                }
                Map data = cloudinaryService.upload(file);
                existingUser.setAvatar(String.valueOf(data.get("secure_url")));
                existingUser.setPublicId(String.valueOf(data.get("public_id")));
            }
            userRepo.save(existingUser);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request body", e);
        }
        ApiResponse apiResponse = new ApiResponse("Update account success !!!", true, 200);
        return new ResponseEntity(apiResponse, HttpStatus.OK);
    }
    private UserDto mapToDto(User user){
        return  new UserDto().builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullname(user.getFullname())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .status(user.getStatus())
                .status(user.getStatus())
                .role(user.getRole().getName())
                .roleId(user.getRole().getId())
                .build();
    }

    @GetMapping("/token")
    public ResponseEntity<?> getUserByToken(HttpServletRequest request) {
     String token = jwtAuthenticationFilter.getJWTFromRequest(request);
     User user = jwtAuthenticationFilter.getUserFromJWT(token);
     User u = userRepo.findById(user.getId()).orElseThrow(() -> new ResourceNotFoundException("user", "id", user.getId()));
     UserDto dto = mapToDto(u);
     ApiDataResponse apiResponse = new ApiDataResponse("Get user success !!!", true, 200, dto);
     return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/importData")
    public ResponseEntity<?> importData(@RequestPart("file") MultipartFile file) {
        boolean importData = userService.importData(file);
        if(!importData){
            return ResponseEntity.ok(new ApiDataResponse("Import data failed duplicate username or email !!!", false, 400, null));
        }
        ApiDataResponse apiResponse = new ApiDataResponse("Import data success !!!", true, 200,null);
        return ResponseEntity.ok(apiResponse);
    }
    @GetMapping("/exportFileExcel")
    public ResponseEntity<?> dowloadFileExcelApi() throws IOException {
        String fileName = "users"+".xlsx";
        ByteArrayInputStream actualData = userService.getExportDataExcel();
        InputStreamResource file = new InputStreamResource(actualData);
        ResponseEntity<?> body = ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename="+fileName)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
        return body;
    }
}
