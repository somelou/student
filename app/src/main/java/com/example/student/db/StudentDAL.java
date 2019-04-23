package com.example.student.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.example.student.bean.Student;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

/**
 * @author somelou
 * @description
 * @date 2019-04-22
 */
public class StudentDAL {

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;


    public StudentDAL(Context context){
        databaseHelper=new DatabaseHelper(context);
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
    public void insertStudentInfo(long id,String name, String fender,String collage,String speciality,String hobby,String birthday) throws SQLException {
        db=databaseHelper.getWritableDatabase();
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
        return databaseHelper.getReadableDatabase().rawQuery(sql, null);
    }

    /**
     * 删除学生信息
     * @param id long
     */
    public void deleteStudentInfo(long id) throws SQLException{
        db=databaseHelper.getWritableDatabase();

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
        db = databaseHelper.getWritableDatabase();

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
        cursor.close();
        return student;
    }

    /**
     * 模糊搜索
     * @param studentArrayList ArrayList<Student>
     * @param query String
     * @return list
     */
    public ArrayList<Student> getQueryData(ArrayList<Student> studentArrayList, String query){
        db=databaseHelper.getReadableDatabase();
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
        db=databaseHelper.getReadableDatabase();
        System.out.println("db version:"+db.getVersion());
        //db.setVersion(2);
        /*
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
            System.out.println("get name:"+stuName+",stuid:"+stuID+"，version:"+cursor.getString(cursor.getColumnIndex("second")));
            //byte[] stuPic = cursor.getBlob(cursor.getColumnIndex("pic"));
            studentArrayList.add(new Student(stuID, stuName, stuCollage, stuSpeciality));
        }
        cursor.close();
        return studentArrayList;
    }
}
