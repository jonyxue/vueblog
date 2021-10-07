package com.markerhub.util;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.markerhub.shiro.AccountProfile;
import org.apache.shiro.SecurityUtils;

public class ShiroUtil {

    /**
     *获取用户id
     */
    public static AccountProfile getProfile(){
        return (AccountProfile) SecurityUtils.getSubject().getPrincipal();
//        JSONObject jsonObject= JSONUtil.parseObj(SecurityUtils.getSubject().getPrincipal());
//        return JSONUtil.toBean(jsonObject,AccountProfile.class) ;
    }
}
