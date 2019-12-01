package com.itheima.hchat.service.impl;

import com.itheima.hchat.mapper.TbFriendMapper;
import com.itheima.hchat.mapper.TbFriendReqMapper;
import com.itheima.hchat.mapper.TbUserMapper;
import com.itheima.hchat.pojo.*;
import com.itheima.hchat.pojo.vo.User;
import com.itheima.hchat.service.UserService;

import com.itheima.hchat.utils.*;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;


import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private TbUserMapper tbUserMapper;

    @Autowired
    private TbFriendMapper tbFriendMapper;

    @Autowired
    private TbFriendReqMapper tbFriendReqMapper;

    @Autowired
    private IdWorker idWorker;


    @Autowired
    private FastDFSClient fastDFSClient;

    @Autowired
    private Environment env;

    @Autowired
    private QRCodeUtils qrCodeUtils;

    @Autowired
    private OSSClientUtil ossClientUtil;

    @Autowired
    private FileToMultipartFileUtil fileToMultipartFileUtil;

    @Override
    public List<TbUser> findAll() {
        return tbUserMapper.selectByExample(null);
    }


    @Override
    public User login(String username, String password)  {

        try {
            if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
                TbUserExample example = new TbUserExample();
                TbUserExample.Criteria criteria = example.createCriteria();
                criteria.andUsernameEqualTo(username);
                List<TbUser> userList = tbUserMapper.selectByExample(example);
                if (userList != null && userList.size() == 1) {
                    String encodingUsername = DigestUtils.md5DigestAsHex(password.getBytes());
                    if (encodingUsername.equals(userList.get(0).getPassword())) {
                        User user = new User();
                        BeanUtils.copyProperties(userList.get(0), user);
                        return user;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void register(TbUser tbUser) {


        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(tbUser.getUsername());
        List<TbUser> userList = tbUserMapper.selectByExample(example);
        if(userList != null && userList.size() > 0){
            throw new RuntimeException("用户名已经存在!");
        }

        try {
            tbUser.setId(idWorker.nextId());
            tbUser.setPassword(DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes()));
            tbUser.setPicNormal("");
            tbUser.setPicSmall("");
            tbUser.setNickname(tbUser.getUsername());


            String qrcodeStr = "user_code:" + tbUser.getId();
            String tempdir = env.getProperty("chat.tmpdir");
            String qrCodeFilePath = tempdir + tbUser.getUsername() + ".png";
            qrCodeUtils.createQRCode(qrCodeFilePath, qrcodeStr);
            System.out.println(qrCodeFilePath);
            //String url = env.getProperty("fdfs.httpurl") + fastDFSClient.uploadFile(new File(qrCodeFilePath));

            String url = ossClientUtil.uploadFileByPath(qrCodeFilePath);
            tbUser.setQrcode(url);
            tbUser.setCreatetime(new Date());

            tbUserMapper.insert(tbUser);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("二维码生成失败!");
        }
    }

    @Override
    public User upload(MultipartFile file, String userid) {
        try {
            System.out.println(file);

            //上传到阿里云OSS
            String imageUrl = ossClientUtil.checkImage(file);

//
//            String url = fastDFSClient.uploadFace(file);
//            String[] fileNameList = url.split("\\.");
//            String fileName = fileNameList[0];
//            String ext = fileNameList[1];
//
//
//            String picSmallUrl = fileName + "_150x150."+ext;
//            TbUser tbUser = tbUserMapper.selectByPrimaryKey(userid);
////
////            String prefix = env.getProperty("fdfs.httpurl");
//            tbUser.setPicNormal(prefix+url);
//            tbUser.setPicSmall(prefix+picSmallUrl);

            TbUser tbUser = tbUserMapper.selectByPrimaryKey(userid);
            tbUser.setPicNormal(imageUrl);
            tbUser.setPicSmall(imageUrl);

            tbUserMapper.updateByPrimaryKey(tbUser);
            User user = new User();
            BeanUtils.copyProperties(tbUser,user);


            return user;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void updateNickname(String userid, String nickName) {
        if(StringUtils.isNotBlank(nickName)){
            TbUser tbUser = tbUserMapper.selectByPrimaryKey(userid);
            tbUser.setNickname(nickName);
            tbUserMapper.updateByPrimaryKey(tbUser);
        }else{
            throw new RuntimeException("昵称不能为空!");
        }
    }

    @Override
    public User findById(String userid) {
        TbUser tbUser = tbUserMapper.selectByPrimaryKey(userid);
        User user = new User();
        BeanUtils.copyProperties(tbUser,user);
        return user;
    }

    @Override
    public User findByUsername(String userid, String friendUsername) {
        TbUserExample userExample = new TbUserExample();
        TbUserExample.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andUsernameEqualTo(friendUsername);

        List<TbUser> userList = tbUserMapper.selectByExample(userExample);
        TbUser friend = userList.get(0);



        User friendUser = new User();
        BeanUtils.copyProperties(friend,friendUser);
        return friendUser;
    }


}
