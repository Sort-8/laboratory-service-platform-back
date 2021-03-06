package com.miku.lab.config;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.miku.lab.util.Constant;

import java.util.Random;

/**
 * @Description: 阿里云短信接口配置类
 * @author: 涛
 * @date: 2019/4/11 15:01
 */
public class AliyunConfig {

    /* 短信API产品名称（短信产品名固定，无需修改） */
    private static final String product = "Dysmsapi";

    /* 短信API产品域名，接口地址固定，无需修改 */
    private static final String domain = "dysmsapi.aliyuncs.com";

    
    private static final String accessKeyId = "LTAI5tS3qUD5zyrfJsdLZmjb";
    private static final String accessKeySecret = "62D2OXDQQDwlvy19IMm787hcvctbqL";

    /* 短信发送 */
    public static SendSmsResponse sendSms(String phone) throws ClientException {

        /* 超时时间，可自主调整 */
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        /* 初始化acsClient,暂不支持region化 */
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        /* 组装请求对象-具体描述见控制台-文档部分内容 */
        SendSmsRequest request = new SendSmsRequest();
        /* 必填:待发送手机号 */
        request.setPhoneNumbers(phone);
        /* 必填:短信签名-可在短信控制台中找到 */
        request.setSignName("图书管理系统"); //TODO: 这里是你短信签名
        /* 必填:短信模板code-可在短信控制台中找到 */
        request.setTemplateCode("SMS_220600985"); //TODO: 这里是你的模板code
        /* 可选:模板中的变量替换JSON串,如模板内容为"亲爱的用户,您的验证码为${code}"时,此处的值为 */
        String smsCode = getMsgCode();

        request.setTemplateParam("{\"code\":\"" + smsCode + "\"}");

        // hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        if(sendSmsResponse.getCode()!= null && sendSmsResponse.getCode().equals("OK")){
            System.out.println("短信验证码为："+ Constant.SMS_CODE);
            return sendSmsResponse;
        }else {
           return null;
        }

    }

    /**
     * @Function: 生成验证码
     * @author:   涛
     * @Date:     2019/4/11 15:30
     */
    private static String getMsgCode() {
        int n = 6;
        StringBuilder code = new StringBuilder();
        Random ran = new Random();
        for (int i = 0; i < n; i++) {
            code.append(Integer.valueOf(ran.nextInt(10)).toString());
        }
        Constant.SMS_CODE = code.toString();
        return code.toString();
    }
}