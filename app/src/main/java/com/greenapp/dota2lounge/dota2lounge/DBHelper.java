package com.greenapp.dota2lounge.dota2lounge;

        import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, "myDB", null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("myLogs", "------ onCreate database ------");
        // создаем таблицу с полями
        db.execSQL("create table matches ("
                + "id integer primary key autoincrement,"
                + "matchtime text,"
                + "matchtournament text,"
                + "team text,"
                + "against text,"
                + "teampersent text,"
                + "againstpersent text,"
                + "link text,"
                + "matchstatus text,"
                + "matchwinner text,"
                + "timeupdate text"
                + ");");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}