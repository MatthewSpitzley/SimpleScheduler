package com.example.simpleschedulerproject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


public class AddCategoryActivity extends AppCompatActivity {
    private EditText categoryName;
    private Button backBtn;
    private Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        categoryName = findViewById(R.id.category_name);
        backBtn = findViewById(R.id.back);
        addBtn = findViewById(R.id.add);
    }
}