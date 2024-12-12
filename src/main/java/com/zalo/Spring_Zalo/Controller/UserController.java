package com.zalo.Spring_Zalo.Controller;

import com.zalo.Spring_Zalo.DTO.UserDto;
import com.zalo.Spring_Zalo.Entities.User;
import com.zalo.Spring_Zalo.Repo.UserMongoRepo;
import com.zalo.Spring_Zalo.Service.CloudinaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@RestController
    @RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserMongoRepo userRepo;

    @Autowired
    private CloudinaryService cloudinaryService;
    Logger logger = LoggerFactory.getLogger(UserController.class);

//    @GetMapping("")
//    public ResponseEntity<?> getAllAccount(@RequestParam(value = "pageSize" ,defaultValue = "5") int pageSize,
//                                           @RequestParam(value = "pageNumber" ,defaultValue = "1") int pageNumber) {
//        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
//
//        Page<User> usersWithRole = userRepo.findAll(pageable);
//        Page<UserDto> map = usersWithRole.map(this::mapToDto);
//        return ResponseEntity.ok(map);
//    }

    @GetMapping("")
    public ResponseEntity<?> getAllAccountByTime(@RequestParam(value = "key",required = false ) String key,
                                           @RequestParam(value = "startTime",required = false ) String startTime,
                                           @RequestParam(value = "endTime",required = false ) String endTime,
                                           @RequestParam(value = "sort",required = false, defaultValue ="descend") String sort,
                                           @RequestParam(value = "pageSize" ,defaultValue = "5") int pageSize,
                                           @RequestParam(value = "pageNumber" ,defaultValue = "1") int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort.equals("ascend") ? Sort.by("createAt").ascending() : Sort.by("createAt").descending());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        LocalDateTime time1 = LocalDateTime.parse("0000-01-01T00:00:00");
        LocalDateTime time2 = LocalDateTime.now();
        if(!startTime.isEmpty() && !endTime.isEmpty()){
            time1 = LocalDateTime.parse(startTime);
            time2 = LocalDateTime.parse(endTime);
        }

        logger.info("key = {}, startTime = {}, endTime = {}",key,startTime,endTime);
        Page<User> usersWithRole = userRepo.findByUserNameOrEmail(key,time1,time2,pageable);
        Page<UserDto> map = usersWithRole.map(this::mapToDto);
        return new ResponseEntity<>(map, HttpStatus.OK);
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
                .createAt(user.getCreateAt())

                .build();
    }
}
