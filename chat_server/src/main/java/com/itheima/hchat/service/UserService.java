package com.itheima.hchat.service;

import com.itheima.hchat.pojo.TbUser;
import com.itheima.hchat.pojo.vo.User;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface UserService {
    List<TbUser> findAll();

    User login(String username, String password);

    void register(TbUser tbUser);

    User upload(MultipartFile file, String userid);

    void updateNickname(String userid, String nickName);

    User findById(String userid);

    User findByUsername(String userid, String friendUsername);
}
