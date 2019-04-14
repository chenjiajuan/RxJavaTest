package com.chenjiajuan.commom.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Course {
    String name;

    @Generated(hash = 1863461973)
    public Course(String name) {
        this.name = name;
    }

    @Generated(hash = 1355838961)
    public Course() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
