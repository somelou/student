package com.example.student.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.widget.Toast;

import com.example.student.bean.Student;

import java.util.ArrayList;


public class DatabaseHelper extends SQLiteOpenHelper {

    /**
     * 声明一个AndroidSDK自带的数据库变量db
     */
    private SQLiteDatabase db;
    private Context parent;

    private static String name = "student.db";
    private static int dbDefaultVersion = 1;

    //学生信息表
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
            + "pswd TEXT);";

    /**
     * 写一个这个类的构造函数，参数为上下文context，所谓上下文就是这个类所在包的路径
     * 指明上下文，数据库名，工厂默认空值，版本号默认从1开始
     * super(context,"db_test",null,1);
     * 把数据库设置成可写入状态，除非内存已满，那时候会自动设置为只读模式
     * db = getReadableDatabase();
     */
    public DatabaseHelper(Context context) {
        super(context, name, null, dbDefaultVersion);
        //db=getWritableDatabase();
        parent = context;
    }

    /**
     * 只在创建的时候使用一次
     * 重写两个必须要重写的方法，因为class DBOpenHelper extends SQLiteOpenHelper
     * 而这两个方法是 abstract 类 SQLiteOpenHelper 中声明的 abstract 方法
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL(CREATE_USER_TABLE);
        //db = getWritableDatabase();
        db.execSQL(CREATE_STUDENT_TABLE);
        Toast.makeText(parent, "成功创建Database", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("drop table if exists student");
        //onCreate(db);
    }

    /*
     * 接下来写自定义的增删改查方法
     * add()
     * delete()
     * update()
     */
    public void insertStudentInfo(long id,String name, String fender,String collage,String speciality,String hobby,String birthday) {
        db=getWritableDatabase();
        String sql="INSERT INTO STU VALUES (?,?,?,?,?,?,?,null)";

        SQLiteStatement statement=db.compileStatement(sql);
        statement.clearBindings();
        statement.bindLong(1,id);
        statement.bindString(2,name);
        statement.bindString(3,fender);
        statement.bindString(4,collage);
        statement.bindString(5,speciality);
        statement.bindString(6,hobby);
        statement.bindString(7,birthday);
        //statement.bindBlob(7, DefaultAvatarBuilder.generateImg(name));

        statement.executeInsert();
        db.close();
    }

    public Cursor getData(String sql){
        db= getReadableDatabase();
        return db.rawQuery(sql, null);
    }


    public void deleteStudentInfo(long id) {
        db=getWritableDatabase();

        String sql = "DELETE FROM STU WHERE stuid = ?";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, id);

        statement.execute();
        db.close();
    }

    public void updateStudentInfo(long id, String name, String fender, String collage, String speciality, String hobby,String birthday) {
        db = getWritableDatabase();

        String sql = "UPDATE STU SET name = ?, fender = ?, collage = ?, speciality=?,hobby=? ,birthday=? WHERE stuid = ?";
        SQLiteStatement statement = db.compileStatement(sql);

        statement.clearBindings();
        statement.bindString(1,name);
        statement.bindString(2,fender);
        statement.bindString(3,collage);
        statement.bindString(4,speciality);
        statement.bindString(5,hobby);
        statement.bindString(6,birthday);
        statement.bindLong(7,id);

        statement.execute();
        db.close();
    }


    public Student getOneData(long id){
        String sql = "SELECT * FROM STU WHERE stuid = "+id;
        Cursor cursor=getData(sql);
        Student student=new Student();
        while(cursor.moveToNext()){
            long stuID = cursor.getLong(cursor.getColumnIndex("stuid"));
            String stuName = cursor.getString(cursor.getColumnIndex("name"));
            String stuFender= cursor.getString(cursor.getColumnIndex("fender"));
            String stuCollage = cursor.getString(cursor.getColumnIndex("collage"));
            String stuSpeciality = cursor.getString(cursor.getColumnIndex("speciality"));
            String stuHobby= cursor.getString(cursor.getColumnIndex("hobby"));
            String stuBirthday= cursor.getString((cursor.getColumnIndex("birthday")));
            //byte[] stuPic = cursor.getBlob(cursor.getColumnIndex("pic"));
            student.setStuName(stuName);
            student.setStuID(stuID);
            student.setStuCollage(stuCollage);
            student.setStuSpeciality(stuSpeciality);
            student.setStuFender(stuFender);
            student.setStuHobby(stuHobby);
            student.setStuBirthday(stuBirthday);
        }
        System.out.println("get stu success,stu id="+student.getStuID());

        return student;
    }

    /*模糊搜索
    * @param query
    * @return list
     */
    public ArrayList<Student> getQueryData(String query){
        db=getReadableDatabase();
        ArrayList<Student> list = new ArrayList<>();
        Cursor cursor = getData("select * from stu where name like '%"+query+"%' or "+"name like '%"+query+"%' or "+"name like '%"+query+"%'");
        //让游标从表头游到表尾,并把数据存放到list中
        while (cursor.moveToNext()) {
            long stuID = cursor.getLong(cursor.getColumnIndex("stuid"));
            String stuName = cursor.getString(cursor.getColumnIndex("name"));
            String stuCollage = cursor.getString(cursor.getColumnIndex("collage"));
            String stuSpeciality = cursor.getString(cursor.getColumnIndex("speciality"));
            System.out.println("after query get name:"+stuName+",stuid:"+stuID);
            list.add(new Student(stuID, stuName, stuCollage, stuSpeciality));
        }
        cursor.close();
        return list;
    }

    /**
     * getAllData()
     * 查询表user全部内容,需要有个容器存放，以供使用，定义了一个ArrayList类的list
     * 使用游标Cursor从表中查询数据,第一个参数："表名"，中间5个：null，查询出来内容的排序方式："stuid DESC"
     *
     * @return list
     */
/*    public ArrayList<Student> getAllData() {
        //可以改成HashMap
        db=getReadableDatabase();
        ArrayList<Student> list = new ArrayList<Student>();
        Cursor cursor = getData("SELECT * FROM STU");
        //让游标从表头游到表尾,并把数据存放到list中
        while (cursor.moveToNext()) {
            long stuID = cursor.getLong(cursor.getColumnIndex("stuid"));
            String stuName = cursor.getString(cursor.getColumnIndex("name"));
            String stuCollage = cursor.getString(cursor.getColumnIndex("collage"));
            String stuSpeciality = cursor.getString(cursor.getColumnIndex("speciality"));
            System.out.println("get name:"+stuName+",stuid:"+stuID);
            //byte[] stuPic = cursor.getBlob(cursor.getColumnIndex("pic"));
            list.add(new Student(stuID, stuName, stuCollage, stuSpeciality, null));
        }
        cursor.close();
        return list;
    }*/

}
