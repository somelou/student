package com.example.student.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @description student bean
 * @author somelou
 * @date 2019/3/20
 */
public class Student implements Parcelable {

    private long stuID;
    private String stuName;
    private String stuFender;
    private String stuCollage;
    private String stuSpeciality;
    private String stuBirthday;
    private String stuHobby;

    public Student(){}

    private Student(Parcel in) {
        this.stuID=in.readLong();
        this.stuName=in.readString();
        this.stuFender=in.readString();
        this.stuCollage=in.readString();
        this.stuSpeciality = in.readString();
        this.stuHobby=in.readString();
        this.stuBirthday=in.readString();
    }

    public Student(long stuID, String stuName, String stuFender, String stuCollage, String stuSpeciality, String stuHobby,String stuBirthday){
        this.stuID=stuID;
        this.stuName=stuName;
        this.stuFender=stuFender;
        this.stuCollage=stuCollage;
        this.stuSpeciality = stuSpeciality;
        this.stuHobby=stuHobby;
        this.stuBirthday=stuBirthday;
    }

    public Student(long stuID, String stuName, String stuCollage, String stuSpeciality){
        this.stuID=stuID;
        this.stuName=stuName;
        this.stuCollage=stuCollage;
        this.stuSpeciality = stuSpeciality;
    }

    public long getStuID() {
        return stuID;
    }

    public void setStuID(long stuID) {
        this.stuID = stuID;
    }

    public String getStuFender() {
        return stuFender;
    }

    public void setStuFender(String stuFender) {
        this.stuFender = stuFender;
    }

    public String getStuHobby() {
        return stuHobby;
    }

    public void setStuHobby(String stuHobby) {
        this.stuHobby = stuHobby;
    }


    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getStuCollage() {
        return stuCollage;
    }

    public void setStuCollage(String stuCollage) {
        this.stuCollage = stuCollage;
    }

    public String getStuSpeciality() {
        return stuSpeciality;
    }

    public void setStuSpeciality(String stuSpeciality) {
        this.stuSpeciality = stuSpeciality;
    }

    public String getStuBirthday() {
        return stuBirthday;
    }

    public void setStuBirthday(String stuBirthday) {
        this.stuBirthday = stuBirthday;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * writeToParcel()方法中需要调用Parcel的writeXxx()方法 , 将Student类中的字段一一写出
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(stuID);
        dest.writeString(stuName);
        dest.writeString(stuFender);
        dest.writeString(stuCollage);
        dest.writeString(stuSpeciality);
        dest.writeString(stuHobby);
        dest.writeString(stuBirthday);
        //dest.writeByteArray(stuPic);
    }

    /**
     * 我们还必须在Student类中提供一个名为CREATOR的常量
     * 这里创建了Parcelable.Creator接口的一个实现 , 并将泛型指定为Student .
     * 接着需要重写createFromParcel()和newArray()这两个方法 .
     * 在createFromParcel()方法中我们要去创建一个Student对象 .
     * 而newArray()方法只需要new出一个Student数组 , 并传入size()作为数据的大小就可以了 .
     */
    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };
}
