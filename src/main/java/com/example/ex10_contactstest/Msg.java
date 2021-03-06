package com.example.ex10_contactstest;

public class Msg {
    public static final int TYPE_RECEIVER = 0;//收到
    public static final int TYPE_SENT = 1;//发出
    private String content;//消息内容
    private int type;//消息类型

    public Msg(String content, int type) {
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }
}
