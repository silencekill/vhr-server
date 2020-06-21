package org.javaboy.vhr.utils;

import java.io.Serializable;

public class ServerResponse implements Serializable {

    private Integer code;
    private String message;
    private Object data;

    public static ServerResponse success(String message,Object data){
        return new ServerResponse(200,message,data);
    }
    public static ServerResponse success(String message){
        return new ServerResponse(200,message);
    }

    public static ServerResponse error(Integer code,String message){
        return new ServerResponse(code,message);
    }

    private ServerResponse(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    private ServerResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    private ServerResponse() {

    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
