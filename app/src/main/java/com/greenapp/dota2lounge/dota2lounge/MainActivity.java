package com.greenapp.dota2lounge.dota2lounge;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    // наш список матчей
    public ListView lv;
    // переменная для лога
    final String LOG_TAG = "myLogs";
    NewThread mt;
    // массив для складирования наших данных
    public ArrayList<matches> mat = new ArrayList<matches>();
    // мой адаптер для списка
    public MyAdapter adapter;
    // прогресс бар
    ProgressBar pb;
    public RotateAnimation ra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOG_TAG, "First message");
        // находим прогресс бар
        pb = (ProgressBar) findViewById(R.id.progressBar1);
        // Создаем анимацию для нашего рефреш орба
        ra = new RotateAnimation(0.0f, 1080.0f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setRepeatCount(Animation.INFINITE);
        ra.setRepeatMode(Animation.REVERSE);
        ra.setDuration(3000);
        pb.setAnimation(ra);
        ra.start();



        // логируем
        Log.d(LOG_TAG, "Run");
        // получаем список
        lv = (ListView) findViewById(R.id.listView1);
        // получаем адаптер из конструктора с нашими данными
        adapter = new MyAdapter(this, mat);
        // запускаем новый поток в котором делаем наш запрос
        mt = (NewThread) getLastNonConfigurationInstance();
        if (mt == null) {      mt = new NewThread();      mt.execute();    }
        mt.link(this);
        // назначаем на наш список обработчик долгого клика
        lv.setOnItemLongClickListener(new OnItemLongClickListener() {

            // в котором переходим на другую активити, где я планирую показывать
            // детали матча
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                // TODO Auto-generated method stub

                Toast.makeText(
                        MainActivity.this,
                        ((TextView) view.findViewById(R.id.tvteam)).getText()
                                + " vs "
                                + ((TextView) view.findViewById(R.id.textView5))
                                .getText(), Toast.LENGTH_LONG).show();
                return false;
            }

        });

        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                // интент с нужным нам классом
                Intent intent = new Intent(MainActivity.this,
                        DatailActivity.class);
                // передаем ссылку
                intent.putExtra("link", ((TextView) view
                        .findViewById(R.id.textView8)).getText().toString());
                // запускаем активити
                Log.d(LOG_TAG, "start new activity");
                startActivity(intent);
            }

        });

    }

    public Object onRetainNonConfigurationInstance() {
        return mt;
    }

    // процедура по загрузги изображений. комментирую потому что использую
    // другую библиотеку написанную людьми, понимающими чуть-чуть больше чем я

	/*
	 * public Bitmap getBitmapfromurl (String urlstring) throws IOException {
	 * Bitmap pic; URL url = new URL(urlstring); URLConnection conn =
	 * url.openConnection(); pic =
	 * BitmapFactory.decodeStream(conn.getInputStream()); return pic; }
	 */

}