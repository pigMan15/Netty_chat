package com.itheima.hchat.netty;

import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class UserChannelMap {
    private static Map<String, Channel> userChannelMap;

    static{
        userChannelMap = new HashMap<String,Channel>();
    }

    public static void put(String userid, Channel channel){
        userChannelMap.put(userid,channel);
    }

    public static void remove(String userid){
        userChannelMap.remove(userid);
    }


    public static void removeByChannelId(String channelId){
        if(!StringUtils.isNotBlank(channelId)){
            return;
        }
        for(String s : userChannelMap.keySet()){
            if(userChannelMap.get(s).id().asLongText().equals(channelId)){
                System.out.println("客户端断开了，取消用户id: "+s+" 与通道id: "+userChannelMap.get(s).id().asLongText()+"的关联");
                userChannelMap.remove(s);
                break;
            }
        }
    }


    public static void print(){
        for(String s : userChannelMap.keySet()){
            System.out.println("用户id: "+s+" 通道id: "+userChannelMap.get(s).id());
        }
    }


    public static Channel get(String friendId){
        return userChannelMap.get(friendId);
    }
}
