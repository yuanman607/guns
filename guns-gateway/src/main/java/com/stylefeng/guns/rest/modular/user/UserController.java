package com.stylefeng.guns.rest.modular.user;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.api.user.UserAPI;
import com.stylefeng.guns.api.user.vo.UserInfoModel;
import com.stylefeng.guns.api.user.vo.UserModel;
import com.stylefeng.guns.rest.common.CurrentUser;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user/")
@RestController
public class UserController {
    @Reference(interfaceClass = UserAPI.class,check = false)
    private UserAPI userAPI;

    @RequestMapping(value = "register",method = RequestMethod.POST)
    public ResponseVO register(UserModel userModel){
        if(userModel.getUsername()==null||userModel.getUsername().equals("")){
            return  ResponseVO.serviceFail("用户名不能为空！");
        }
        if(userModel.getPassword()==null||userModel.getPassword().equals("")){
            return  ResponseVO.serviceFail("密码不能为空！");
        }
        boolean isSuccess=userAPI.register(userModel);
        if(isSuccess){
            return ResponseVO.success("注册成功");
        }else{
            return  ResponseVO.serviceFail("注册失败");
        }
    }

    @RequestMapping(value = "check",method = RequestMethod.POST)
    public ResponseVO check(String username){
       if(username!=null && username.trim().length()>0){
           boolean notExists = userAPI.checkUsername(username);
           if(notExists){
                return  ResponseVO.success("用户名不存在");
            }else{
                return  ResponseVO.serviceFail("用户名已存在");
            }
       }else {
           return ResponseVO.serviceFail("用户名不能为空");
       }
    }

    @RequestMapping(value = "logout",method = RequestMethod.POST)
    public ResponseVO logout(String username){
        /**
         * 前端jwt存储7天
         * 服务器存储活跃用户信息30分钟
         * jwt里的userid查询活跃用户
         * 退出：
         * 前端删除jwt
         * 服务器删除活跃用户缓存数据
         */

        return ResponseVO.success("退出成功");
    }

    /**
     * 获取用户信息
     * @param username
     * @return
     */
    @RequestMapping(value = "getUserInfo",method = RequestMethod.GET)
    public ResponseVO getUserInfo(String username){
        String userId = CurrentUser.getCurrentUser();
        if(userId!=null && userId.trim().length()>0){
            int uuid =Integer.parseInt(userId);
            UserInfoModel userInfoModel=userAPI.getUserInfo(uuid);
            if(userInfoModel!=null){
                return  ResponseVO.success(userInfoModel);
            }else {
                return  ResponseVO.appFail("用户信息查询失败");
            }
        }else{
            return ResponseVO.serviceFail("用户未登录");
        }
    }

    @RequestMapping(value = "updateUserInfo",method = RequestMethod.POST)
    public ResponseVO updateUserInfo(UserInfoModel userInfoModel){
        String userId = CurrentUser.getCurrentUser();
        if(userId!=null && userId.trim().length()>0){
            int uuid =Integer.parseInt(userId);
            //判断当前登陆人员的id与修改的结果id是否一致
            if(uuid!=userInfoModel.getUuid()){
                return  ResponseVO.serviceFail("仅限于修改个人信息");
            }
        UserInfoModel userInfoModel1 = userAPI.updateUserInfo(userInfoModel);
            if(userInfoModel!=null){
                return  ResponseVO.success(userInfoModel);
            }else {
                return  ResponseVO.appFail("用户修改失败");
            }
        }else{
            return ResponseVO.serviceFail("用户未登录");
        }
    }
}
