package com.example.student.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * @author somelou
 * @description
 * @date 2019-04-22
 */
class VersionThird extends UpgradeDB {

    private final static String STUDENT="stu";

    private final static String CREATE_STUDENT_TABLE = "create table if not exists "+STUDENT+"("
            + "stuid long primary key,"
            + "name TEXT,"
            + "fender TEXT,"
            + "collage TEXT,"
            + "speciality TEXT,"
            + "hobby TEXT,"
            + "birthday DATE,"
            + "pic BLOB," +
            "second TEXT,"+
            "third TEXT);";


    @Override
    public void update(SQLiteDatabase db) {
        System.out.println("begin to update version");
        //数据库版本升级时会执行这个方法
        //事务
        db.beginTransaction();
        try {
            //第一步将表A重命名为temp
            db.execSQL("alter table " + STUDENT + " rename to _temp;");
            //第二步创建新表A,此时表结构已加了列
            db.execSQL(CREATE_STUDENT_TABLE);
            //第三步讲temp表中的数据插入到表A
            db.execSQL("INSERT INTO " + STUDENT + " SELECT *,\"3\" FROM _temp;");
            //第四步删除临时表temp
            db.execSQL("drop table _temp;");
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
            System.out.println("end to update version");
        }    }
}
