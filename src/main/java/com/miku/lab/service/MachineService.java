package com.miku.lab.service;/*
 *@author miku
 *@data 2021/7/8 16:42
 *@version:1.1
 */

import com.miku.lab.entity.Machine;
import com.miku.lab.entity.Machine_sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface MachineService {
    Object getAllMachine();

    Object getPageMachine(String page, String limit);


    Object getPageMachineSort(String page, String limit);

    Object getSortDetail(String sortId);

    Object updateMachineSort(Machine_sort machine_sort);

    List<Machine_sort> getMachineType();

    int addMachineSort(Machine_sort machine_sort);

    int delMachineSort(String sortId);

    //Object searchSort(String searchKey,String searchValue);

    Object searchSort(String searchKey,String searchValue,String page, String limit);

    int addMachine(Map<String, Object> param);

    int updateMachine(Map<String, Object> param);

    int updateMachineCheck(Map<String, Object> param);

    Object searchMachine(int page,int limit,String searchKey,String searchValue);

    int deleteMachine(Map<String, Object> param);
}
