package com.example.student.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.student.R;

public class QueryActivity extends AppCompatActivity {

    //static String BTN_QUERY="query";
    EditText queryInput;
    Button btnQuery;

    //private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query_student_info);

        queryInput=findViewById(R.id.editQueryStu);
        btnQuery=findViewById(R.id.buttonQuery);
    }
}
