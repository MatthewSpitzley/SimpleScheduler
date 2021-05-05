package com.example.simpleschedulerproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


public class AddCategoryActivity extends AppCompatActivity {
    private EditText categoryName;
    private Button backBtn;
    private Button addBtn;
    DBHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        categoryName = findViewById(R.id.category_name);
        backBtn = findViewById(R.id.back);
        addBtn = findViewById(R.id.add);

        mHelper = new DBHelper(this);

        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Category newCat = new Category(categoryName.getText().toString());
                mHelper.addCategory(newCat);

                Intent i = new Intent(AddCategoryActivity.this, TaskList.class);
                startActivity(i);
                finish();
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(AddCategoryActivity.this, TaskList.class);
                startActivity(i);
                finish();
            }
        });
    }
}