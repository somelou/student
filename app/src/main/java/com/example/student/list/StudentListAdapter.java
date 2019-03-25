package com.example.student.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.student.R;
import com.example.student.bean.Student;

import java.util.ArrayList;

/**
 * @description StudentItem单项适配器
 * @author somelou
 * @date 2019/3/20
 */

public class StudentListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Student> studentsList;

    /**
     *
     * @param context Context
     * @param layout int
     * @param studentsList ArrayList<Student>
     */
    public StudentListAdapter(Context context, int layout, ArrayList<Student> studentsList) {
        this.context = context;
        this.layout = layout;
        this.studentsList = studentsList;
    }

    @Override
    public int getCount() {
        return studentsList.size();
    }

    @Override
    public Object getItem(int position) {
        return studentsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 私有类
     */
    private class ViewHolder{
        CharAvatarView imageAvatarView;
        TextView txtName, txtId,txtCollage,txtSpeciality;
    }

    /**
     * 将得到的信息显示在Adapter中
     * @param position int
     * @param view View
     * @param viewGroup ViewGroup
     * @return View
     */
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.txtName =  row.findViewById(R.id.textStuName);
            holder.txtId = row.findViewById(R.id.textStuID);
            holder.txtCollage = row.findViewById(R.id.textStuCollage);
            holder.txtSpeciality = row.findViewById(R.id.textStuSpeciality);
            holder.imageAvatarView =  row.findViewById(R.id.imageStudentAvatar);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }

        Student student = studentsList.get(position);

        holder.txtName.setText(student.getStuName());
        holder.txtId.setText(String.valueOf(student.getStuID()));
        holder.txtCollage.setText(student.getStuCollage());
        holder.txtSpeciality.setText(student.getStuSpeciality());

        //根据用户名生成默认头像
        //byte[] avatar = student.getStuPic();
        //Bitmap bitmap = BitmapFactory.decodeByteArray(avatar, 0, avatar.length);
        //holder.imageAvatarView.setImageBitmap(bitmap);
        holder.imageAvatarView.setText(student.getStuName());

        return row;
    }
}
