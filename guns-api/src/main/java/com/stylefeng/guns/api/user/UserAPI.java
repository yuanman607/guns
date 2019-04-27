package com.stylefeng.guns.api.user;

import com.stylefeng.guns.api.user.vo.UserInfoModel;
import com.stylefeng.guns.api.user.vo.UserModel;

public interface UserAPI {
   int login(String username,String password);
   //注册
   boolean register(UserModel userModel);
   //检查用户是否存在
   boolean checkUsername(String userName);
   //
   UserInfoModel getUserInfo(int uuid);

   UserInfoModel updateUserInfo(UserInfoModel userInfoModel);
}
