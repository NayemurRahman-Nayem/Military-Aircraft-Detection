package com.example.militaryaircraft ;


public class ReadWriteUserDetails {

    public String fullName,email,doB,gender,mobile ;

    public ReadWriteUserDetails() {

    };
    public ReadWriteUserDetails(String texFullname,String email , String textDoB, String textGender, String textMobile) {
        this.fullName = texFullname ;
        this.doB = textDoB ;
        this.gender = textGender;
        this.mobile = textMobile;
        this.email = email ;
    }

    public String getDoB() {
        return doB;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getGender() {
        return gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setDoB(String doB) {
        this.doB = doB;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
