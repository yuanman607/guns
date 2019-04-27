package com.stylefeng.guns.rest.modular.vo;

import lombok.Data;

@Data
public class ResponseVO<M> {
    //返回状态
    private int status;
    //返回信息
    private String msg;
    //返回实体
    private M data;

    private String imgPre;

    private ResponseVO(){}
    //成功返回
    public static<M> ResponseVO success(String imgPre,M m){
        ResponseVO responseVO =new ResponseVO();
        responseVO.setData(m);
        responseVO.setStatus(0);
        responseVO.setImgPre(imgPre);
        return responseVO;
    }
    public static<M> ResponseVO success(M m){
        ResponseVO responseVO =new ResponseVO();
        responseVO.setData(m);
        responseVO.setStatus(0);
        return responseVO;
    }
    //成功返回
    public static<M> ResponseVO success(String msg){
        ResponseVO responseVO =new ResponseVO();
        responseVO.setStatus(0);
        responseVO.setMsg(msg);
        return responseVO;
    }
    //业务异常返回
    public static<M> ResponseVO serviceFail(String msg){
        ResponseVO responseVO =new ResponseVO();
        responseVO.setMsg(msg);
        responseVO.setStatus(1);
        return responseVO;
    }
    //系统异常返回
    public static<M> ResponseVO appFail(String msg){
        ResponseVO responseVO =new ResponseVO();
        responseVO.setMsg(msg);
        responseVO.setStatus(999);
        return responseVO;
    }
}
