package com.miku.lab.service;/*
 *@author miku
 *@data 2021/7/9 14:27
 *@version:1.1
 */

import com.miku.lab.entity.OrderCheck;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookLogService {
    Object getAllBookLog();

    Object getAllBookMachine();

    String addBookMachineLog(String openId,String machine_id,String book_number);

    String addLabLog(OrderCheck orderCheck);

    //添加一个仪器数目
    String addBookingNumber(String openId,String machine_id);
    //减少一个仪器数目
    String subBookingNumber(String openId,String machine_id);


}
