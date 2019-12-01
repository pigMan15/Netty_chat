package com.itheima.hchat.controller;


import com.itheima.hchat.pojo.TbFriendReq;
import com.itheima.hchat.pojo.vo.FriendReq;
import com.itheima.hchat.pojo.vo.Result;
import com.itheima.hchat.pojo.vo.User;
import com.itheima.hchat.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/friend")
public class FriendController {

    @Autowired
    private FriendService friendService;


    @RequestMapping(value = "/sendRequest")
    public Result sendRequest(@RequestBody TbFriendReq tbFriendReq){
        try{

            friendService.sendRequest(tbFriendReq.getFromUserid(),tbFriendReq.getToUserid());
            return new Result(true,"已申请!");
        }catch (RuntimeException e){
            e.printStackTrace();
            return new Result(false,e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"好友申请失败!");
        }
    }

    @RequestMapping(value = "/findFriendReqByUserid")
    public List<FriendReq> findFriendReqByUserid(String userid){
        return friendService.findFriendReqByUserid(userid);
    }

    @RequestMapping(value = "/acceptFriendReq")
    public Result acceptFriendReq(String reqid){
        try{
            friendService.acceptFriendReq(reqid);
            return new Result(true,"添加好友请求成功!");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(true,"添加好友请求失败!");
        }
    }

    @RequestMapping(value = "/ignoreFriendReq")
    public Result ignoreFriendReq(String reqid){
        try{
            friendService.ignoreFriendReq(reqid);
            return new Result(true,"忽略添加好友请求成功!");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(true,"忽略添加好友请求失败!");
        }
    }

    @RequestMapping(value = "/findFriendByUserid")
    public List<User> findFriendByUserid(String userid){
        try{
           return  friendService.findFriendByUserid(userid);
        }catch (Exception e){
            e.printStackTrace();
            return new ArrayList<User>();
        }
    }
}
