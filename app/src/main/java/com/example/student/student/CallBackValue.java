package com.example.student.student;

import com.example.student.bean.Student;

/**
 * @author somelou
 * @description
 * @date 2019/3/24
 */
public interface CallBackValue {
    void SendValueFromStudentMessage(Student student, int pageNum);
}
