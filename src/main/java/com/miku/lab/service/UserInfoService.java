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

    int isValiToken(String user_id,String token);

    List<Map> getAllUser();

    Object getOneUser(String user_id);

    int verifyUserPassword(String user_id, String password);

    int updateUserInfo(Map<String, Object> param);

    Map<String, Object> getPageUser(String page, String limit);

    int addUser(Map<String, Object> param);

    int deleteUser(Map<String, Object> param);

    int updatePersonPassword(String user_id, String password,String updater);

    Object searchUser(int page,int limit,String searchKey,String searchValue);

    int updatePersonDisable(Map<String, Object> param);

    int updateWxUserBookingStatus(Map<String, Object> param);

    int resetUser(Map<String, Object> param);
}
