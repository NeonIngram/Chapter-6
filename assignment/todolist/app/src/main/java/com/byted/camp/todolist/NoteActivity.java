package com.byted.camp.todolist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.byted.camp.todolist.db.TodoContract.TodoEntry;
import com.byted.camp.todolist.db.TodoDbHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteActivity extends AppCompatActivity {

    private static final String TAG = "Note";

    private EditText editText;
    private Button addBtn;

    private TodoDbHelper dbhelper = new TodoDbHelper(this);
    private RadioButton btn1;
    private RadioButton btn2;
    private RadioButton btn3;
    private RadioGroup radiogroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        setTitle(R.string.take_a_note);

        editText = findViewById(R.id.edit_text);
        editText.setFocusable(true);
        editText.requestFocus();
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.showSoftInput(editText, 0);
        }

        addBtn = findViewById(R.id.btn_add);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence content = editText.getText();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(NoteActivity.this,
                            "No content to add", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean succeed = saveNote2Database(content.toString().trim());
                if (succeed) {
                    Toast.makeText(NoteActivity.this,
                            "Note added", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK);
                } else {
                    Toast.makeText(NoteActivity.this,
                            "Error", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
        radiogroup=findViewById(R.id.radiogroup);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private boolean saveNote2Database(String content) {
        // TODO 插入一条新数据，返回是否插入成功
        //return false;
       SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TodoEntry.COLUMN_CONTENT,content);
        Date date = new Date();
        String format = "E, dd MMM yyyy HH:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        values.put(TodoEntry.COLUMN_DATE, dateFormat.format(date));
        values.put(TodoEntry.COLUMN_STATE, 0);
        //int count = radiogroup.getChildCount();
       /* for(int i=0;i<count;i++)
        {
            RadioButton rb = (RadioButton) radiogroup.getChildAt(i);
            if (rb.isChecked()) {
                values.put(TodoEntry.COLUMN_PRIORITY, i);
                break;
            }

        }

       if(btn1.isChecked()) {
           values.put(TodoEntry.COLUMN_PRIORITY,1);
       }
       else if(btn2.isChecked()){
           values.put(TodoEntry.COLUMN_PRIORITY,2);
       }
       else{
           values.put(TodoEntry.COLUMN_PRIORITY,3);
       }*/
        int count = radiogroup.getChildCount();
        for (int i = 0; i < count; i++) {
            RadioButton rb = (RadioButton) radiogroup.getChildAt(i);
            if (rb.isChecked()) {
                values.put(TodoEntry.COLUMN_PRIORITY, i);
                break;
            }
        }

        long newRowId = db.insert(TodoEntry.TABLE_NAME, null, values);
        if (newRowId < 0)
            return false;
        else
            return true;
    }


}
