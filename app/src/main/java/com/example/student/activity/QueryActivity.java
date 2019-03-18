package com.example.student.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.student.R;
import com.example.student.bean.Student;
import com.example.student.util.DatabaseHelper;

import java.util.ArrayList;

public class QueryActivity extends AppCompatActivity {

    static String BTN_QUERY="query";
    EditText queryInput;
    Button btnQuery;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query_student_info);

        queryInput=findViewById(R.id.editQueryStu);
        btnQuery=findViewById(R.id.buttonQuery);
    }

    public void turnToQueryResult(View view) {
        String query=queryInput.getText().toString();
        ArrayList<Student> quertList=dbHelper.getQueryData(query);
        Intent intent = new Intent(this, StudentActivity.class);
        intent.putExtra(BTN_QUERY, "add");
        startActivity(intent);
    }
}
