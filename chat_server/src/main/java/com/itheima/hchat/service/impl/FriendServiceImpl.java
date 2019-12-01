package com.itheima.hchat.service.impl;

import com.itheima.hchat.mapper.TbFriendMapper;
import com.itheima.hchat.mapper.TbFriendReqMapper;
import com.itheima.hchat.mapper.TbUserMapper;
import com.itheima.hchat.pojo.*;
import com.itheima.hchat.pojo.vo.FriendReq;
import com.itheima.hchat.pojo.vo.Result;
import com.itheima.hchat.pojo.vo.User;
import com.itheima.hchat.service.FriendService;
import com.itheima.hchat.utils.IdWorker;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@Transactional
public class FriendServiceImpl implements FriendService {


    @Autowired
    private TbUserMapper tbUserMapper;

    @Autowired
    private TbFriendMapper tbFriendMapper;

    @Autowired
    private TbFriendReqMapper tbFriendReqMapper;

    @Autowired
    private IdWorker idWorker;

    @Override
    public void sendRequest(String fromUserid, String toUserid) {

        //判断是否可以发送请求
        TbUser friend = tbUserMapper.selectByPrimaryKey(toUserid);
        checkAllowToAddFriend(fromUserid,friend);


        //写入请求到数据库
        TbFriendReq tbFriendReq = new TbFriendReq();
        tbFriendReq.setFromUserid(fromUserid);
        tbFriendReq.setToUserid(toUserid);
        tbFriendReq.setId(idWorker.nextId());
        tbFriendReq.setCreatetime(new Date());
        tbFriendReq.setStatus(0);

        tbFriendReqMapper.insert(tbFriendReq);

    }

    @Override
    public List<FriendReq> findFriendReqByUserid(String userid) {
        try {
            TbFriendReqExample tbFriendReqExample = new TbFriendReqExample();
            TbFriendReqExample.Criteria criteria = tbFriendReqExample.createCriteria();
            criteria.andToUseridEqualTo(userid).andStatusEqualTo(0);
            List<TbFriendReq> tbfriendReqList = tbFriendReqMapper.selectByExample(tbFriendReqExample);
            List<FriendReq> friendReqList = new ArrayList<>();
            for (TbFriendReq tbFriendReq : tbfriendReqList) {
                TbUser tbUser = tbUserMapper.selectByPrimaryKey(tbFriendReq.getFromUserid());
                FriendReq friendReq = new FriendReq();
                BeanUtils.copyProperties(tbUser, friendReq);
                friendReq.setId(tbFriendReq.getId());
                friendReqList.add(friendReq);
            }
            return friendReqList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void acceptFriendReq(String reqid) {
        System.out.println(reqid);
         TbFriendReq tbFriendReq = tbFriendReqMapper.selectByPrimaryKey(reqid);
         tbFriendReq.setStatus(1);
         tbFriendReqMapper.updateByPrimaryKey(tbFriendReq);

         TbFriend tbFriend1 = new TbFriend();
         tbFriend1.setUserid(tbFriendReq.getFromUserid());
         tbFriend1.setFriendsId(tbFriendReq.getToUserid());
         tbFriend1.setId(idWorker.nextId());
         tbFriend1.setCreatetime(new Date());

         TbFriend tbFriend2 = new TbFriend();
         tbFriend2.setUserid(tbFriendReq.getToUserid());
         tbFriend2.setFriendsId((tbFriendReq.getFromUserid()));
         tbFriend2.setId(idWorker.nextId());
         tbFriend2.setCreatetime(new Date());

         tbFriendMapper.insert(tbFriend1);
         tbFriendMapper.insert(tbFriend2);
    }

    @Override
    public void ignoreFriendReq(String reqid) {
        TbFriendReq friendReq = tbFriendReqMapper.selectByPrimaryKey(reqid);
        friendReq.setStatus(1);
        tbFriendReqMapper.updateByPrimaryKey(friendReq);
    }

    @Override
    public List<User> findFriendByUserid(String userid) {
        TbFriendExample friendExample = new TbFriendExample();
        TbFriendExample.Criteria friendCriteria = friendExample.createCriteria();
        friendCriteria.andUseridEqualTo(userid);
        List<TbFriend> tbFriendList = tbFriendMapper.selectByExample(friendExample);
        List<User> friendList = new ArrayList<>();

        for(TbFriend tbFriend :tbFriendList){
            TbUser tbUser = tbUserMapper.selectByPrimaryKey(tbFriend.getFriendsId());
            User user = new User();
            BeanUtils.copyProperties(tbUser,user);
            friendList.add(user);
        }

        return friendList;

    }


    private void checkAllowToAddFriend(String userid, TbUser friend){
        //判断是否添加自己为好友
        if(friend.getId().equals(userid)){
            throw new RuntimeException("不能添加自己为好友!");
        }
        //判断是否已经是好友
        TbFriendExample friendExample = new TbFriendExample();
        TbFriendExample.Criteria friendCriteria = friendExample.createCriteria();
        friendCriteria.andUseridEqualTo(userid).andFriendsIdEqualTo(friend.getId());
        List<TbFriend> friendList = tbFriendMapper.selectByExample(friendExample);
        if(friendList != null && friendList.size() > 0){
            throw new RuntimeException(friend.getUsername()+" 已经是您的好友了!");
        }

        //判断是否已经发送好友申请了
        TbFriendReqExample friendReqExample = new TbFriendReqExample();
        TbFriendReqExample.Criteria friendReqCriteria = friendReqExample.createCriteria();
        friendReqCriteria.andFromUseridEqualTo(userid).andToUseridEqualTo(friend.getId()).andStatusEqualTo(0);
        List<TbFriendReq> tbFriendReqList = tbFriendReqMapper.selectByExample(friendReqExample);
        if(tbFriendReqList != null && tbFriendReqList.size() > 0){
            throw new RuntimeException("已经发送好友申请了！");
        }
    }



}
