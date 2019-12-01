package com.itheima.hchat.service;

import com.itheima.hchat.pojo.vo.FriendReq;
import com.itheima.hchat.pojo.vo.User;

import java.util.List;

public interface FriendService {
    void sendRequest(String fromUserid, String toUserid);

    List<FriendReq> findFriendReqByUserid(String userid);

    void acceptFriendReq(String reqid);

    void ignoreFriendReq(String reqid);

    List<User> findFriendByUserid(String userid);
}
