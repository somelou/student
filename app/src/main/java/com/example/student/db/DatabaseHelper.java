package com.example.student.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

/**
 * @description 数据库协助
 * @author somelou
 * @date 2019/3/20
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    /**
     * 声明一个AndroidSDK自带的数据库变量db
     */
    //private SQLiteDatabase db;
    private Context parent;

    private static String name = "student.db";
    private static int dbDefaultVersion = 3;

    /**
     * 学生信息表
     */
    private final static String CREATE_STUDENT_TABLE = "create table if not exists stu("
            + "stuid long primary key,"
            + "name TEXT,"
            + "fender TEXT,"
            + "collage TEXT,"
            + "speciality TEXT,"
            + "hobby TEXT,"
            + "birthday DATE,"
            + "pic BLOB);";

    //登录用户表
    private final static String CREATE_USER_TABLE = "create table if not exists user("
            + "userid long primary key,"
            + "name TEXT,"
            + "pswd TEXT,"
            + "token TEXT);";

    /**
     * 写一个这个类的构造函数，参数为上下文context，所谓上下文就是这个类所在包的路径
     * 指明上下文，数据库名，工厂默认空值，版本号默认从1开始
     * super(context,"db_test",null,1);
     * 把数据库设置成可写入状态，除非内存已满，那时候会自动设置为只读模式
     * db = getReadableDatabase();
     * @param context Context
     */
    public DatabaseHelper(Context context) {
        super(context, name, null, dbDefaultVersion);
        //db.setVersion(2);
        System.out.println("have do DatabaseHelper new");
        //Log.d("Database",SQLiteDatabase.)
        //db=getWritableDatabase();
        parent = context;
    }

    /**
     * 只在创建的时候使用一次
     * 重写两个必须要重写的方法，因为class DBOpenHelper extends SQLiteOpenHelper
     * 而这两个方法是 abstract 类 SQLiteOpenHelper 中声明的 abstract 方法
     * @param db SQLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL(CREATE_USER_TABLE);
        //db = getWritableDatabase();

        db.execSQL(CREATE_STUDENT_TABLE);
        db.execSQL(CREATE_USER_TABLE);
        Toasty.success(parent, "成功创建Database&Table,"+db.getVersion(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("old version:"+oldVersion+",new version:"+newVersion);
        update(db,oldVersion,newVersion);
        Toasty.success(parent, "成功升级Database&Table,"+db.getVersion()).show();
        //db.execSQL("drop table if exists student");
        //onCreate(db);
    }


    public void update(SQLiteDatabase db, int oldVersion, int newVersion){
        UpgradeDB upgrade;
        if (oldVersion < newVersion) {
            oldVersion++;
            upgrade = VersionFactory.getUpgrade(oldVersion);
            if (upgrade == null) {
                return;
            }
            upgrade.update(db);
            //update方法是一个递归实现，主要是解决数据库跨版本升级
            update(db, oldVersion, newVersion);
        }
    }


    @Override
    public SQLiteDatabase getWritableDatabase(){
        return super.getWritableDatabase();
    }

    @Override
    public SQLiteDatabase getReadableDatabase(){
        return super.getReadableDatabase();
    }

}
