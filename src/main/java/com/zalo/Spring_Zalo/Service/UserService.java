package com.zalo.Spring_Zalo.Service;

import com.zalo.Spring_Zalo.request.UserRequestImport;
import org.springframework.http.ResponseEntity;

import com.zalo.Spring_Zalo.Entities.User;
import com.zalo.Spring_Zalo.request.UserRequestLogin;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    ResponseEntity<?> AuthorizeUser(UserRequestLogin user);
    User authenticateUser(String username, String password);

    boolean importData(MultipartFile file);

    boolean checkData(List<UserRequestImport> list);
}
