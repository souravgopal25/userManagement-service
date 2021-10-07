package com.chirp.userService.service;

import com.chirp.userService.models.User;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class UIDGenerator {
    private  String uid;


    public String getUid(User user) {
        String str=user.getFirstName()+user.getLastName()+user.getEmail();
        UUID uuid=UUID.nameUUIDFromBytes(str.getBytes());
        uid=uuid.toString();
        return uid;
    }


}
