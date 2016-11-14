package com.example.intel.hospital;

/**
 * Created by Intel on 21/06/2016.
 */
public class MessageModel {

    int textId;
    String Name,UserName,Password,Mobile,UserType;

    public MessageModel(){
    }

    public MessageModel(int textId, String name, String userName, String password, String mobile, String userType) {
        this.textId = textId;
        Name = name;
        UserName = userName;
        Password = password;
        Mobile = mobile;
        UserType = userType;
    }


    public int getTextId() {
        return textId;
    }

    public void setTextId(int textId) {
        this.textId = textId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }
}
