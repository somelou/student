package com.example.student.util;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.widget.Toast;

import com.example.student.bean.Student;

import java.util.ArrayList;

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
    private SQLiteDatabase db;
    private Context parent;

    private static String name = "student.db";
    private static int dbDefaultVersion = 1;

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
        Toasty.success(parent, "成功创建Database&Table", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("drop table if exists student");
        //onCreate(db);
    }

    /**
     * 接下来写自定义的增删改查方法,add(),delete(),update()
     * @param id long
     * @param name String
     * @param fender String
     * @param collage String
     * @param speciality String
     * @param hobby String
     * @param birthday String
     */
    public void insertStudentInfo(long id,String name, String fender,String collage,String speciality,String hobby,String birthday) throws SQLException{
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

    /**
     * 执行sql，返回Cursor
     * @param sql String
     * @return Cursor
     */
    private Cursor getData(String sql) throws SQLException{
        db= getReadableDatabase();
        return db.rawQuery(sql, null);
    }

    /**
     * 删除学生信息
     * @param id long
     */
    public void deleteStudentInfo(long id) throws SQLException{
        db=getWritableDatabase();

        String sql = "DELETE FROM STU WHERE stuid = ?";
        SQLiteStatement statement = db.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, id);

        statement.execute();
        db.close();
    }

    /**
     * 更新已有的学生的信息
     * @param id long
     * @param name String
     * @param fender String
     * @param collage String
     * @param speciality String
     * @param hobby String
     * @param birthday String
     */
    public void updateStudentInfo(long id, String name, String fender, String collage, String speciality, String hobby,String birthday) throws SQLException{
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


    /**
     * 得到某个学生的信息
     * @param id long
     * @return Student
     */
    public Student getOneData(long id) throws SQLException {
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
        System.out.println("get stu success in helper,stu id="+student.getStuID());

        return student;
    }

    /**
     * 模糊搜索
     * @param studentArrayList ArrayList<Student>
     * @param query String
     * @return list
     */
    public ArrayList<Student> getQueryData(ArrayList<Student> studentArrayList,String query){
        db=getReadableDatabase();
        Cursor cursor = getData("select * from stu where name like '%"+query+"%' or "+"collage like '%"+query+"%' or "+"speciality like '%"+query+"%'");
        //让游标从表头游到表尾,并把数据存放到list中
        while (cursor.moveToNext()) {
            long stuID = cursor.getLong(cursor.getColumnIndex("stuid"));
            String stuName = cursor.getString(cursor.getColumnIndex("name"));
            String stuCollage = cursor.getString(cursor.getColumnIndex("collage"));
            String stuSpeciality = cursor.getString(cursor.getColumnIndex("speciality"));
            System.out.println("after query get name:"+stuName+",stuid:"+stuID);
            studentArrayList.add(new Student(stuID, stuName, stuCollage, stuSpeciality));
        }
        cursor.close();
        return studentArrayList;
    }

    /**
     * getAllData()
     * 查询表user全部内容,需要有个容器存放，以供使用，定义了一个ArrayList类的list
     * 使用游标Cursor从表中查询数据,第一个参数："表名"，中间5个：null，查询出来内容的排序方式："stuid DESC"
     *
     * @param studentArrayList ArrayList<Student>
     * @return list
     */
    public ArrayList<Student> getAllData(ArrayList<Student> studentArrayList) {
        //可以改成HashMap
        db=getReadableDatabase();
        /**
         * 使用 adapter.notifyDataSetChanged() 时，必须保证传进 Adapter 的数据 List 是同一个 List
         * 而不能是其他对象，否则无法更新 listview。
         * 即，你可以调用 List 的 add()， remove()， clear()，addAll() 等方法，
         * 这种情况下，List 指向的始终是你最开始 new 出来的 ArrayList ，
         * 然后调用 adapter.notifyDataSetChanged() 方法，可以更新 ListView；
         * 但是如果你重新 new 了一个 ArrayList（重新申请了堆内存），
         * 那么这时候，List 就指向了另外一个 ArrayLIst，
         * 这时调用 adapter.notifyDataSetChanged() 方法，就无法刷新 listview 了。*/
        //ArrayList<Student> list = new ArrayList<Student>();
        Cursor cursor = getData("SELECT * FROM STU");
        //让游标从表头游到表尾,并把数据存放到list中
        while (cursor.moveToNext()) {
            long stuID = cursor.getLong(cursor.getColumnIndex("stuid"));
            String stuName = cursor.getString(cursor.getColumnIndex("name"));
            String stuCollage = cursor.getString(cursor.getColumnIndex("collage"));
            String stuSpeciality = cursor.getString(cursor.getColumnIndex("speciality"));
            System.out.println("get name:"+stuName+",stuid:"+stuID);
            //byte[] stuPic = cursor.getBlob(cursor.getColumnIndex("pic"));
            studentArrayList.add(new Student(stuID, stuName, stuCollage, stuSpeciality));
        }
        cursor.close();
        return studentArrayList;
    }

}
