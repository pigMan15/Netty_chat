package com.itheima.hchat.controller;

import com.itheima.hchat.pojo.TbUser;
import com.itheima.hchat.pojo.vo.Result;
import com.itheima.hchat.pojo.vo.User;
import com.itheima.hchat.service.UserService;
import com.itheima.hchat.utils.OSSClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;



    @RequestMapping(value = "/findAll")
    public List<TbUser> findAll(){
        return userService.findAll();
    }

    @RequestMapping(value = "/login")
    public Result login(@RequestBody TbUser tbUser){
        User user = null;

            user = userService.login(tbUser.getUsername(),tbUser.getPassword());
            if(user != null){
                return new Result(true,"登陆成功",user);
            }else{
                return new Result(false,"登录失败，请检查用户名或密码");
            }

    }

    @RequestMapping(value = "/register")
    public Result register(@RequestBody TbUser tbUser){
        try{
            userService.register(tbUser);
            return new Result(true,"注册成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,e.getMessage());
        }
    }


    @RequestMapping(value = "/upload")
    public Result upload(MultipartFile file, String userid){
        try{
               User user = userService.upload(file,userid);
               if(user != null){
                   System.out.println(user);
                   return new Result(true,"上传成功",user);
               }else{
                   return new Result(false,"上传失败");
               }
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"上传失败");
        }
    }


    @RequestMapping(value="/updateNickname")
    public Result updateNickname(@RequestBody User user){
        try{
             userService.updateNickname(user.getId(),user.getNickname());
             return new Result(true,"更新成功");
         }catch (Exception e){
             return new Result(false,e.getMessage());
         }
    }

    @RequestMapping(value = "/findById")
    public User findById(String userid){
        return userService.findById(userid);
    }



    @RequestMapping(value = "/findByUsername")
    public Result findByUsername(String userid,String friendUsername){
        try {
            User user = userService.findByUsername(userid, friendUsername);
            return new Result(true,"查询成功",user);
        }catch(Exception e){
            e.printStackTrace();
            return new Result(false,e.getMessage());
        }
    }

    @RequestMapping(value = "/search")
    public Result search(String userid,String friendUsername){
        try {
            User user = userService.findByUsername(userid, friendUsername);
            return new Result(true,"查询成功",user);
        }catch(Exception e){
            e.printStackTrace();
            return new Result(false,e.getMessage());
        }
    }
}
