package com.miku.lab.service;/*
 *@author miku
 *@data 2021/6/1 20:12
 *@version:1.1
 */

import com.miku.lab.entity.UserInfo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface UserInfoService {
    UserInfo login(String token, UserInfo user, String captcha);
    int isValiToken(String user_id,String token);
    List<UserInfo> getAllUser();

    Map<String,Object> getOneUser(String user_id);
}
