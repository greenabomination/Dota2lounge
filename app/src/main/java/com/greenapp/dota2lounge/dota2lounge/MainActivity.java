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
        new NewThread().execute();
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

    public class NewThread extends AsyncTask<String, Void, String> {

        public List<String> listurl = new ArrayList<String>();


        @Override
        protected String doInBackground(String... arg) {
            // создаем объект для создания и управления версиями БД
            DBHelper dbHelper = new DBHelper(MainActivity.this);
            Log.d(LOG_TAG, "get dbHelper");
            ContentValues cv = new ContentValues();
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            int clearCount = db.delete("matches", null, null);
            Log.d(LOG_TAG, "START");
            Document doc;
            Elements htmltext;
            String _matchtime;
            String _matchtournament;
            String _team;
            String _against;
            String _teampersent;
            String _againstpersent;
            String _link;
            String _matchstatus;
            String _matchwinner;

            try {
                doc = Jsoup.connect("http://dota2lounge.com/")
                        .timeout(15 * 1000).get();
                Log.d(LOG_TAG, "Connect");
                htmltext = doc.select("div.matchmain:has(div.teamtext)");
                Log.d(LOG_TAG, "PARSING");
                // Log.d(LOG_TAG, htmltext.html());
                // int q=0;
                for (Element i : htmltext) {

                    _matchtime = i.select("div.whenm").first().text();
                    _matchtournament = i.select("div.eventm").first().text();
                    _team = i.select("div.teamtext > b").first().text()
                            .replace("\\", "");
                    _against = i.select("div.teamtext > b").last().text()
                            .replace("\\", "");
                    _teampersent = i.select("div.teamtext > i").first().text();
                    _againstpersent = i.select("div.teamtext > i").last()
                            .text();
                    _link = i.select("a").attr("abs:href");
                    _matchstatus = i.select("div.matchleft").parents()
                            .attr("class");
                    _matchwinner = "";

                    if (_matchstatus.equals("match notavailable")) {
                        if (i.select("div.team").first().children().size() == 1) {
                            // Log.d(LOG_TAG, "TEAM!!!");
                            _matchwinner = "team";
                        } else {
                            _matchwinner = "against";
                        }

                    }
                    /*
					 * if (q==4){ Log.d(LOG_TAG, i.html()); Log.d(LOG_TAG,
					 * _matchstatus); Log.d(LOG_TAG,
					 * ""+i.select("div.team").first().children().size());} q++;
					 */

					/*
					 * doc_detail=Jsoup.connect(_link).get();
					 * html_detail=doc_detail.select("div.gradient > div.half");
					 * _matchtime=html_detail.get(0).html();
					 * _matchtype=html_detail.get(1).html();
					 * _matchcesttime=html_detail.get(2).html();
					 *
					 * _matchtime=_matchtime+", "+_matchcesttime;
					 */

                    matches m = new matches(_matchtime, _matchtournament,
                            _team, _against, _teampersent, _againstpersent, i
                            .select("div.team")
                            .first()
                            .attr("style")
                            .replace("float: right; background: url('",
                                    "").replace("')", "")
                            .replace("\\", ""), i
                            .select("div.team")
                            .last()
                            .attr("style")
                            .replace("float: left; background: url('",
                                    "").replace("')", "")
                            .replace("\\", ""), null, _matchstatus,
                            _matchwinner, _link);

                    mat.add(m);

                    cv.put("matchtime", _matchtime);
                    cv.put("matchtournament", _matchtournament);
                    cv.put("team", _team);
                    cv.put("against", _against);
                    cv.put("teampersent", _teampersent);
                    cv.put("againstpersent", _againstpersent);
                    cv.put("link", _link);
                    cv.put("matchstatus", _matchstatus);
                    cv.put("matchwinner", _matchwinner);

                    // вставляем запись и получаем ее ID
                    long rowID = db.insert("matches", null, cv);
                    Log.d(LOG_TAG, "row inserted, ID = " + rowID);
                }

                Log.d(LOG_TAG, "FINISH");
                Cursor c = db.query("matches", null, null, null, null, null, null);
                if (c.moveToFirst()) {        // определяем номера столбцов по имени в выборке
                    int idColIndex = c.getColumnIndex("id");
                    int teamColIndex = c.getColumnIndex("team");
                    int againstColIndex = c.getColumnIndex("against");
                    do {
                        // получаем значения по номерам столбцов и пишем все в лог
                        Log.d(LOG_TAG,
                                "ID = " + c.getInt(idColIndex) +
                                        ", ::: " + c.getString(teamColIndex) +
                                        ", VS  " + c.getString(againstColIndex));
                        // переход на следующую строку
                        // а если следующей нет (текущая - последняя), то false - выходим из цикла
                    } while (c.moveToNext());
                } else Log.d(LOG_TAG, "0 rows");
                c.close();


                // удаляем все записи
                clearCount = db.delete("matches", null, null);
                Log.d(LOG_TAG, "deleted rows count = " + clearCount);
                db.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.d(LOG_TAG, "setadapter");
            lv.setAdapter(adapter);
            ra.cancel();
            pb.setAnimation(null);
            pb.setVisibility(View.GONE);
        }
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
