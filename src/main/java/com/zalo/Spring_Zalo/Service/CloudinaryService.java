package com.zalo.Spring_Zalo.Service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    Map upload(MultipartFile  file);

    void deleteImageUpload(String  file);
}
