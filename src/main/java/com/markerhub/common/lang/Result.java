package com.markerhub.common.lang;

import lombok.Data;

import java.io.Serializable;

/**
 *   什么情况下需要序列化：
 *
 *         1.     当你想把的内存中的对象写入到硬盘的时候。
 *
 *         2.     当你想用套接字在网络上传送对象的时候。
 *
 *         3.     当你想通过RMI传输对象的时候。
 */
@Data
public class Result implements Serializable {
    private int code;
    private String msg;
    private Object data;

    public static Result succ(Object data){

        return succ(200,"操作成功",data);

    }

    public static Result succ(int code,String msg,Object data){
        Result result=new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return  result;

    }

    public static Result fail(String msg){

        return  fail(400,msg,null);

    }

    public static Result fail(String msg,Object data){

        return  fail(400,msg,data);

    }

    public static Result fail(int code,String msg,Object data){
        Result result=new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;

    }
}
