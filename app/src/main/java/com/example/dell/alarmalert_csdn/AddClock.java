package com.example.dell.alarmalert_csdn;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import javax.security.auth.login.LoginException;

import static com.example.dell.alarmalert_csdn.MainActivity.list;
import static com.example.dell.alarmalert_csdn.MainActivity.timeAdapter;

public class AddClock extends AppCompatActivity implements View.OnClickListener {
    private Calendar calendar;
    private TextView show_hour;
    private TextView show_minute;
    private EditText content;
    private Button set;
    private Button save;
    private ImageView back;
    private TextView title;
    String hourformat;
    String minuteformat;
    Clock clock = new Clock();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clock);
        show_hour = findViewById(R.id.hour);
        show_minute = findViewById(R.id.minute);
        content = findViewById(R.id.content);
        set = findViewById(R.id.set_time);
        set.setOnClickListener(this);
        save = findViewById(R.id.save);
        back = findViewById(R.id.add);
        back.setImageResource(R.drawable.ic_back);
        back.setOnClickListener(this);
        title = findViewById(R.id.title);
        title.setText("添加闹钟");
        save.setOnClickListener(this);
        calendar = Calendar.getInstance();//获得一个Calendar类型的通用对象，
                                         // getInstance()将返回一个Calendar的对象

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_time:
                calendar.setTimeInMillis(System.currentTimeMillis());//返回当前时间
                int mhour = calendar.get(Calendar.HOUR_OF_DAY);//24小时制时
                int mminute = calendar.get(Calendar.MINUTE);    //分
                new TimePickerDialog(AddClock.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //设置时钟时间
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);

                        //返回时间值到textview
                        hourformat = format(hourOfDay);
                        minuteformat = format(minute);
                        Toast.makeText(AddClock.this, "" + hourformat + ":" + minuteformat, Toast.LENGTH_SHORT).show();
                        show_hour.setText(hourformat);
                        show_minute.setText(minuteformat);


                    }
                }, mhour, mminute, true).show();
                break;
            case R.id.save:
                //通过am启动intent,实现定时任务
                Intent intent = new Intent(AddClock.this, CallAlarm.class);
                PendingIntent sender = PendingIntent.getBroadcast(
                        AddClock.this, 0, intent, 0);
                AlarmManager am;
                am = (AlarmManager) getSystemService(ALARM_SERVICE);//获取AlarmManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { //包含新的api
                    if (System.currentTimeMillis()>calendar.getTimeInMillis()+40000){
                        //加24小时，能唤醒系统
                        am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()+86400000, sender);
                    }else {
                        am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
                    }
                }
                clock.setHour(hourformat);
                clock.setMinute(minuteformat);
                clock.setContent("" + content.getText().toString());//设置标签内容
                clock.setClockType(Clock.clock_open);//默认设置为开
                if (clock.getHour()!=null&&clock.getMinute()!=null) {
                    clock.save();
                    list.add(clock);
                    timeAdapter.notifyDataSetChanged();
                    //Log.e("Listnumber======",list.size()+"");
                    finish();
                }else {
                    Toast.makeText(this, "请选择闹钟时间", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.add:
                finish();
                break;


        }
    }

    private String format(int x) {
        String s = "" + x;
        if (s.length() == 1) {
            s = "0" + s;
        }
        return s;
    }
}
