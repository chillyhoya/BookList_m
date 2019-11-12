package com.example.booklist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditBookActivity extends AppCompatActivity {

    private EditText editTextBookTitle;
    private Button buttonOK,buttonCancel;
    private int insertPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        editTextBookTitle=(EditText)findViewById(R.id.edit_text_book_title);
        buttonOK=(Button)findViewById(R.id.button_ok);
        buttonCancel=(Button)findViewById(R.id.button_cancel);

        editTextBookTitle.setText(getIntent().getStringExtra("title"));//在图书编辑页面接收main传来的内容
        insertPosition=getIntent().getIntExtra("position",0);

        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("title",editTextBookTitle.getText().toString());//把数据传回去
                intent.putExtra("position",insertPosition);
                setResult(RESULT_OK,intent);
                EditBookActivity.this.finish();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditBookActivity.this.finish();
            }
        });
    }
}
