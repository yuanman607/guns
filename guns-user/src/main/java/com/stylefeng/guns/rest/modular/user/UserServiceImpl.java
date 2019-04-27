package com.stylefeng.guns.rest.modular.user;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.api.user.UserAPI;
import com.stylefeng.guns.api.user.vo.UserInfoModel;
import com.stylefeng.guns.api.user.vo.UserModel;
import com.stylefeng.guns.core.util.MD5Util;
import com.stylefeng.guns.rest.common.persistence.dao.MoocUserTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MoocUserT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Service(interfaceClass = UserAPI.class,loadbalance = "roundrobin")
public class UserServiceImpl implements UserAPI {
    @Autowired
    private MoocUserTMapper moocUserTMapper;

    @Override
    public boolean register(UserModel userModel) {
        //将注册信息实体转换成数据实体
        MoocUserT moocUserT=new MoocUserT();
        moocUserT.setUserName(userModel.getUsername());
        moocUserT.setEmail(userModel.getEmail());
        moocUserT.setAddress(userModel.getAddress());
        moocUserT.setUserPhone(userModel.getPhone());
        //数据加密【MD5混淆加密】或者 【MD5混淆加密 + 盐值（随机生成）--》shiro加密的方式】
        //写入数据库
        moocUserT.setUserPwd( MD5Util.encrypt(userModel.getPassword()));
        Integer insert=moocUserTMapper.insert(moocUserT);
        return insert > 0;
    }

    @Override
    public int login(String username, String password) {
        //根据登录账号获取数据库信息
        MoocUserT moocUserT=new MoocUserT();
        moocUserT.setUserName(username);
        MoocUserT result=moocUserTMapper.selectOne(moocUserT);
        if(result!=null&&result.getUuid()>0){
            String md5Password=MD5Util.encrypt(password);
            if(md5Password.equals(result.getUserPwd())){
                return result.getUuid();
            }
        }
        //获取得到的结果，然后与加密字符串匹配
        System.out.println("这个是user service"+username+", "+password);
        return 0;
    }



    @Override
    public boolean checkUsername(String userName) {
        EntityWrapper<MoocUserT> entityWrapper=new EntityWrapper<>();
        entityWrapper.eq("user_name",userName);
        Integer result=moocUserTMapper.selectCount(entityWrapper);
        return result == null || result <= 0;
    }

    private UserInfoModel do2UserInfo(MoocUserT moocUserT){

        UserInfoModel userInfoModel= new UserInfoModel();
        userInfoModel.setUuid(moocUserT.getUuid());
        userInfoModel.setUpdateTime(moocUserT.getUpdateTime()==null?0:moocUserT.getUpdateTime().getTime());
        userInfoModel.setSex(moocUserT.getUserSex());
        userInfoModel.setPhone(moocUserT.getUserPhone());
        userInfoModel.setNickname(moocUserT.getNickName());
        userInfoModel.setHeadAddress(moocUserT.getHeadUrl());
        userInfoModel.setEmail(moocUserT.getEmail());
        userInfoModel.setBirthday(moocUserT.getBirthday());
        userInfoModel.setBiography(moocUserT.getBiography());
        userInfoModel.setBeginTime(moocUserT.getBeginTime()==null?0:moocUserT.getBeginTime().getTime());
        userInfoModel.setAddress(moocUserT.getAddress());
        userInfoModel.setUsername(moocUserT.getUserName());

        return userInfoModel;
    }

    @Override
    public UserInfoModel getUserInfo(int uuid) {
        //根据主键查询用户信息
        //将MoocUserT转换成UserInfoModel
        MoocUserT moocUserT =moocUserTMapper.selectById(uuid);
        UserInfoModel userInfoModel=do2UserInfo(moocUserT);
        return userInfoModel;
    }

    private Date timeToDate(long time){
        return new Date(time);
    }

    @Override
    public UserInfoModel updateUserInfo(UserInfoModel userInfoModel) {
        //将前段数据转成MoocUserT
        MoocUserT moocUserT=new MoocUserT();
        moocUserT.setUuid(userInfoModel.getUuid());
        moocUserT.setUserSex(userInfoModel.getSex());
        moocUserT.setUpdateTime(timeToDate(userInfoModel.getUpdateTime()));
        moocUserT.setNickName(userInfoModel.getNickname());
        moocUserT.setHeadUrl(userInfoModel.getHeadAddress());
        moocUserT.setBirthday(userInfoModel.getBirthday());
        moocUserT.setBiography(userInfoModel.getBiography());
        moocUserT.setBeginTime(timeToDate(userInfoModel.getBeginTime()));
        moocUserT.setEmail(userInfoModel.getEmail());
        moocUserT.setAddress(userInfoModel.getAddress());
        moocUserT.setUserPhone(userInfoModel.getPhone());
        moocUserT.setUserName(userInfoModel.getUsername());

        //数据入库
        Integer isSuccess=moocUserTMapper.updateById(moocUserT);
        if(isSuccess>0){
            UserInfoModel userInfo=getUserInfo(moocUserT.getUuid());
            return userInfo;
        }else{
            return userInfoModel;
        }
    }
}
