package com.itheima.hchat.netty;

import com.itheima.hchat.pojo.TbChatRecord;

public class Message {

    private Integer type;
    private TbChatRecord chatRecord;
    private Object ext;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public TbChatRecord getChatRecord() {
        return chatRecord;
    }

    public void setChatRecord(TbChatRecord chatRecord) {
        this.chatRecord = chatRecord;
    }

    public Object getExt() {
        return ext;
    }

    public void setExt(Object ext) {
        this.ext = ext;
    }

    @Override
    public String toString() {
        return "Message{" +
                "type=" + type +
                ", chatRecord=" + chatRecord +
                ", ext=" + ext +
                '}';
    }
}
