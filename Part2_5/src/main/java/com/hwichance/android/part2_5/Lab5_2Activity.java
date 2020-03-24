package com.hwichance.android.part2_5;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class Lab5_2Activity extends AppCompatActivity implements View.OnClickListener {

    Button btn_alert;
    Button btn_list;
    Button btn_date;
    Button btn_time;
    Button btn_custom;

    AlertDialog alertDialog;
    AlertDialog listDialog;
    AlertDialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab5_2);

        btn_alert = (Button) findViewById(R.id.btn_alert);
        btn_list = (Button) findViewById(R.id.btn_list);
        btn_date = (Button) findViewById(R.id.btn_date);
        btn_time = (Button) findViewById(R.id.btn_time);
        btn_custom = (Button) findViewById(R.id.btn_custom);

        btn_alert.setOnClickListener(this);
        btn_list.setOnClickListener(this);
        btn_date.setOnClickListener(this);
        btn_time.setOnClickListener(this);
        btn_custom.setOnClickListener(this);
    }

    DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if(dialog == alertDialog && which == DialogInterface.BUTTON_POSITIVE) {
                Toast.makeText(Lab5_2Activity.this, "alert dialog OK click.....", Toast.LENGTH_SHORT).show();
            }
            else if(dialog == listDialog) {
                // resource access method of Context class
                String[] data = getResources().getStringArray(R.array.dialog_array);
                Toast.makeText(Lab5_2Activity.this, data[which] + " 선택 하셨습니다.", Toast.LENGTH_SHORT).show();
            }
            else if(dialog == customDialog && which == DialogInterface.BUTTON_POSITIVE) {
                Toast.makeText(Lab5_2Activity.this, "custom dialog 확인 click.....", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onClick(View v) {
        if(v == btn_alert) {
            AlertDialog.Builder builder= new AlertDialog.Builder(this);
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setTitle("알림");
            builder.setMessage("정말 종료 하시겠습니까?");
            builder.setPositiveButton("OK", dialogListener);
            builder.setNegativeButton("NO", null);

            alertDialog = builder.create();
            alertDialog.show();
        }
        else if(v == btn_list) {
            AlertDialog.Builder builder= new AlertDialog.Builder(this);
            builder.setTitle("알람 벨소리");
            builder.setSingleChoiceItems(R.array.dialog_array, 0, dialogListener);
            builder.setPositiveButton("확인", null);
            builder.setNegativeButton("취소", null);

            listDialog = builder.create();
            listDialog.show();
        }
        else if(v == btn_date) {
            // util class
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Toast.makeText(Lab5_2Activity.this, year + ":" + (month + 1) + ":" + dayOfMonth, Toast.LENGTH_SHORT).show();
                }
            }, year, month, day);
            datePickerDialog.show();
        }
        else if(v == btn_time) {
            Calendar c = Calendar.getInstance();
            final int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    Toast.makeText(Lab5_2Activity.this, hourOfDay + ":" + minute, Toast.LENGTH_SHORT).show();
                }
            }, hour, minute, false);
            timePickerDialog.show();
        }
        else if(v == btn_custom) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.dialog_layout, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(view);
            builder.setPositiveButton("확인", dialogListener);
            builder.setNegativeButton("취소", null);

            customDialog = builder.create();
            customDialog.show();
        }
    }
}
