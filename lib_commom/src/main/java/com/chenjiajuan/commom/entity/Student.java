package com.chenjiajuan.commom.entity;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Student {
    @Id(autoincrement = true)
    private Long id;
    @Index(unique = true)
    private String stuNo;
    private String name;
    private int age;
    private String sex;
    private String score;
    @Generated(hash = 1363676828)
    public Student(Long id, String stuNo, String name, int age, String sex,
            String score) {
        this.id = id;
        this.stuNo = stuNo;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.score = score;
    }
    @Generated(hash = 1556870573)
    public Student() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getStuNo() {
        return this.stuNo;
    }
    public void setStuNo(String stuNo) {
        this.stuNo = stuNo;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return this.age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getSex() {
        return this.sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getScore() {
        return this.score;
    }
    public void setScore(String score) {
        this.score = score;
    }
}
