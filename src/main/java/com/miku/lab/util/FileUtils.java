package com.miku.lab.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    /**
     * @Author 庞海
     * @Description 文件上传
     * @Date 2021/8/5
     * @Param [file, filePath, fileName]
     * @return void
     **/
    public static Boolean uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        FileOutputStream out = null;
        try {
            File targetFile = new File(filePath);
            //如果目录不存在，创建目录
            if(!targetFile.exists()){
                targetFile.mkdirs();
            }
            out = new FileOutputStream(filePath+fileName);
            out.write(file);
            out.flush();
            //写入成功
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            //写入失败
            return false;
        } finally {
            out.close();
        }
    }

}

