package com.greenapp.dota2lounge.dota2lounge;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kabardinov133238 on 29.01.2015.
 */
public class NewThread extends AsyncTask<String, Void, String> {

    final String LOG_TAG = "myLogs";
    public List<String> listurl = new ArrayList<String>();
    MainActivity activity; // получаем ссылку на MainActivity

    void link(MainActivity act) {
        activity = act;
    } // обнуляем ссылку

    void unLink() {
        activity = null;
    }

    @Override
    protected String doInBackground(String... arg) {
        // создаем объект для создания и управления версиями БД
        DBHelper dbHelper = new DBHelper(activity);
        Log.d(LOG_TAG, "get dbHelper");
        ContentValues cv = new ContentValues();
        Log.d(LOG_TAG, dbHelper.getDatabaseName());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Log.d(LOG_TAG, "get db");
        //  int clearCount = db.delete("matches", null, null);
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
        String _teampic;
        String _againstpic;
        //Для базы данных
        String[] columns = null;
        String selection = null;
        String[] selectionArgs = null;
        String groupBy = null;
        String having = null;
        String orderBy = null;

        long time = System.currentTimeMillis();
        long deletetime = time - 900000;//пятнадцатиминутная давность

        selection = "timeupdate <= ?";
        selectionArgs = new String[]{""+deletetime};
Log.d(LOG_TAG,"pre_delete");
int deleterecord = db.delete("matches", selection, selectionArgs);
Log.d(LOG_TAG,"deleted "+deleterecord+" rows");


        Log.d(LOG_TAG, "current time: " + time);
        try {
            doc = Jsoup.connect("http://dota2lounge.com/").timeout(15 * 1000)
                    .get();
            Log.d(LOG_TAG, "Connect");
            htmltext = doc.select("div.matchmain:has(div.teamtext)");
            Log.d(LOG_TAG, "PARSING");

            for (Element i : htmltext) {

                _matchtime = i.select("div.whenm").first().text();
                _matchtournament = i.select("div.eventm").first().text();
                _team = i.select("div.teamtext > b").first().text()
                        .replace("\\", "");
                _against = i.select("div.teamtext > b").last().text()
                        .replace("\\", "");
                _teampersent = i.select("div.teamtext > i").first().text();
                _againstpersent = i.select("div.teamtext > i").last().text();
                _link = i.select("a").attr("abs:href");
                _matchstatus = i.select("div.matchleft").parents()
                        .attr("class");
                _matchwinner = "";
                Log.d(LOG_TAG, _matchstatus.toString());
                if (_matchstatus.equals("match notavailable")) {
                    if (i.select("div.team").first().children().size() == 1) {
                        // Log.d(LOG_TAG, "TEAM!!!");
                        _matchwinner = "team";
                    } else {
                        _matchwinner = "against";
                    }

                }
                _teampic = i
                        .select("div.team").first().attr("style")
                        .replace("float: right; background: url('", "")
                        .replace("')", "").replace("\\", "");
                _againstpic = i
                        .select("div.team").last().attr("style")
                        .replace("float: left; background: url('", "")
                        .replace("')", "").replace("\\", "");
                //заканчиваем парсинг и сохраняем полученный матч


                selection = "link = ?";
                selectionArgs = new String[]{_link};


                Cursor q = db.query("matches", null, selection, selectionArgs, null, null, null);

                Log.d(LOG_TAG, "" + q.getCount());

                if (0 == q.getCount()) {

                    cv.put("matchtime", _matchtime);
                    cv.put("matchtournament", _matchtournament);
                    cv.put("team", _team);
                    cv.put("against", _against);
                    cv.put("teampersent", _teampersent);
                    cv.put("againstpersent", _againstpersent);
                    cv.put("teampic", _teampic);
                    cv.put("againstpic", _againstpic);
                    cv.put("link", _link);
                    cv.put("matchstatus", _matchstatus);
                    cv.put("matchwinner", _matchwinner);
                    cv.put("timeupdate", time);

                    // вставляем запись и получаем ее ID
                    long rowID = db.insert("matches", null, cv);
                    Log.d(LOG_TAG, "row inserted, ID = " + rowID);
                } else { //иначе пропускаем
                    Log.d(LOG_TAG, "Link " + _link + " already exist...skip");
                }
            }

            Log.d(LOG_TAG, "FINISH");
            Cursor c = db.query("matches", null, null, null, null, null, null);
            if (c.moveToFirst()) { // определяем номера столбцов по имени в
                // выборке
                int idColIndex = c.getColumnIndex("id");
                int teamColIndex = c.getColumnIndex("team");
                int againstColIndex = c.getColumnIndex("against");
                int matchstatusColIndex = c.getColumnIndex("matchstatus");
                int matchtimeColIndex = c.getColumnIndex("matchtime");
                int timerecordColIndex = c.getColumnIndex("timeupdate");
                do {
                    // получаем значения по номерам столбцов и пишем все в лог
                    Log.d(LOG_TAG,
                            "ID = " + c.getInt(idColIndex) + ", ::: "
                                    + c.getString(teamColIndex) + ", VS  "
                                    + c.getString(againstColIndex) + " ("
                                    + c.getString(matchstatusColIndex) + ") "
                                    + c.getString(matchtimeColIndex) + " ---"
                                    + c.getString(timerecordColIndex)
                    );
                    //получаем из базы, создаем объект матч и добавляем в коллекцию матчей
                    matches m = new matches(c.getString(c.getColumnIndex("matchtime")), c.getString(c.getColumnIndex("matchtournament")), c.getString(c.getColumnIndex("team")),
                            c.getString(c.getColumnIndex("against")), c.getString(c.getColumnIndex("teampersent")), c.getString(c.getColumnIndex("againstpersent")), c.getString(c.getColumnIndex("teampic")), c.getString(c.getColumnIndex("againstpic")), null,
                            c.getString(c.getColumnIndex("matchstatus")), c.getString(c.getColumnIndex("matchwinner")), c.getString(c.getColumnIndex("link")));

                    activity.mat.add(m);
                    // переход на следующую строку
                    // а если следующей нет (текущая - последняя), то false -
                    // выходим из цикла
                } while (c.moveToNext());
            } else
                Log.d(LOG_TAG, "0 rows");
            c.close();

            // удаляем все записи
            //     clearCount = db.delete("matches", null, null);
            //      Log.d(LOG_TAG, "deleted rows count = " + clearCount);
            db.close();
            time = System.currentTimeMillis();
            Log.d(LOG_TAG, time + "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onPostExecute(String result) {
        Log.d(LOG_TAG, "setadapter");
        activity.lv.setAdapter(activity.adapter);
        activity.ra.cancel();
        activity.pb.setAnimation(null);
        activity.pb.setVisibility(View.GONE);
    }
}
