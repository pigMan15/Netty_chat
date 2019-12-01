package com.itheima.hchat.service.impl;

import com.itheima.hchat.mapper.TbChatRecordMapper;
import com.itheima.hchat.pojo.TbChatRecord;
import com.itheima.hchat.pojo.TbChatRecordExample;
import com.itheima.hchat.service.ChatRecordService;
import com.itheima.hchat.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Service
@Transactional
public class ChatRecordServiceImpl implements ChatRecordService {

    @Autowired
    private TbChatRecordMapper tbChatRecordMapper;

    @Autowired
    private IdWorker idWorker;

    @Override
    public void insert(TbChatRecord chatRecord) {
        chatRecord.setId(idWorker.nextId());
        chatRecord.setCreatetime(new Date());
        chatRecord.setHasRead(0);
        chatRecord.setHasDelete(0);
        tbChatRecordMapper.insert(chatRecord);
    }

    @Override
    public List<TbChatRecord> findByUserIdAndFriendId(String userid, String friendid) {
        TbChatRecordExample example = new TbChatRecordExample();
        TbChatRecordExample.Criteria criteria1 = example.createCriteria();
        TbChatRecordExample.Criteria criteria2 = example.createCriteria();

        criteria1.andUseridEqualTo(userid);
        criteria1.andFriendidEqualTo(friendid);
        criteria1.andHasDeleteEqualTo(0);

        criteria2.andUseridEqualTo(friendid);
        criteria2.andFriendidEqualTo(userid);
        criteria2.andHasDeleteEqualTo(0);

        example.or(criteria1);
        example.or(criteria2);

        //将发给userid的所有消息记录设置为已读
        TbChatRecordExample exampleQuerySendToMe = new TbChatRecordExample();
        TbChatRecordExample.Criteria criteriaQuerySendToMe = exampleQuerySendToMe.createCriteria();

        criteriaQuerySendToMe.andFriendidEqualTo(userid).andHasReadEqualTo(0);

        List<TbChatRecord> tbChatRecords = tbChatRecordMapper.selectByExample(exampleQuerySendToMe);

        for(TbChatRecord tbChatRecord : tbChatRecords){
            tbChatRecord.setHasRead(1);
            tbChatRecordMapper.updateByPrimaryKey(tbChatRecord);
        }


        return tbChatRecordMapper.selectByExample(example);
    }

    @Override
    public List<TbChatRecord> findUnreadByUserid(String userid) {
        TbChatRecordExample example = new TbChatRecordExample();
        TbChatRecordExample.Criteria criteria = example.createCriteria();
        criteria.andFriendidEqualTo(userid).andHasReadEqualTo(0);
        return tbChatRecordMapper.selectByExample(example);
    }

    @Override
    public void updateStatusHasRead(String id) {
        TbChatRecord tbChatRecord = tbChatRecordMapper.selectByPrimaryKey(id);
        tbChatRecord.setHasRead(1);
        tbChatRecordMapper.updateByPrimaryKey(tbChatRecord);
    }
}
