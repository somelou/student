package com.example.student.util;

import com.example.student.bean.Student;

import java.util.Map;

/**
 * @author somelou
 * @description
 * @date 2019/3/24
 */
public interface CallBackValue {
    void SendValueFromStudentMessage(Student student, int pageNum);

    void SendQueryValue(Map<String,String> map,int pageNum);
}
