package com.miku.lab.dao;/*
 *@author miku
 *@data 2021/7/20 14:41
 *@version:1.1
 */


import com.miku.lab.entity.Article;
import com.miku.lab.entity.ArticleSort;
import com.miku.lab.entity.Config;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
* @Description:    配置dao
* @Author:         涛
* @CreateDate:     2021/7/20 15:06
* @UpdateUser:     涛
* @UpdateDate:     2021/7/20 15:06
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
@Mapper
@Repository
public interface ConfigDao {

    //获取所有的配置信息
    List<Config> getAllConfig();

    /**
     * 分页获取所有配置信息
     * @return
     */
    List<Config>getPageConfig(Map<String,Object> map);

    //获取配置详细信息
    Config getDetailConfiglById(String id);

    //更新配置信息
    int updateConfig(Map<String,Object>map);
}