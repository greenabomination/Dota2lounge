package com.greenapp.dota2lounge.dota2lounge;

        import java.io.IOException;

        import org.jsoup.Jsoup;
        import org.jsoup.nodes.Document;
        import org.jsoup.nodes.Element;
        import org.jsoup.select.Elements;

        import android.app.Activity;
        import android.app.Notification;
        import android.app.NotificationManager;
        import android.content.Intent;
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.provider.AlarmClock;
        import android.util.Log;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.widget.Button;
        import android.widget.ImageButton;
        import android.widget.ImageView;
        import android.widget.ProgressBar;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class DatailActivity extends Activity implements OnClickListener{


    TextView tvtime, tvtype, tvteam1, tvteam2, tvpersent1,tvpersent2, tvheretime;
    ImageView team1image,team2image;
    ImageButton ib;
    final String LOG_TAG="myLogs";
    Button d2lbtn;
    ProgressBar pb;
    //public Bitmap b1, b2;
    public String _link;
    int h;
    String m;
    public String _matchtime;
    public String _matchtype;
    public String _matchesttime;
    public String _team;
    public String _against;
    public String _teampersent;
    public String _againstpersent;
    public String _teamimagelink;
    public String _againstimagelink;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        Log.d("myLogs", "CREATE");
        tvtime = (TextView)findViewById(R.id.textView1);
        tvtype = (TextView)findViewById(R.id.textView2);
        tvteam1 = (TextView)findViewById(R.id.textView3);
        tvteam2 = (TextView)findViewById(R.id.textView5);
        tvpersent1 = (TextView)findViewById(R.id.textView4);
        tvpersent2 = (TextView)findViewById(R.id.textView6);
        tvheretime=(TextView)findViewById(R.id.textView7);
        team1image=(ImageView)findViewById(R.id.imageView1);
        team2image=(ImageView)findViewById(R.id.imageView2);
        pb=(ProgressBar)findViewById(R.id.detailpb);
        d2lbtn=(Button)findViewById(R.id.d2lbtn);
        d2lbtn.setOnClickListener(this);
        tvheretime.setOnClickListener(this);
        //Log.d("myLogs", "GET COMPLETE");

        Intent intent = getIntent();
        _link = intent.getStringExtra("link");
        DetailLoader dl = new DetailLoader();
        dl.execute(_link);


    }



    public class DetailLoader extends AsyncTask<String, Void, String> {

        String vremya;

        @Override
        protected String doInBackground(String... args) {

            //Log.d(LOG_TAG, "START");
            Document doc_detail;
            Elements html_detail;
            Element i;
            doc_detail=null;



            try {
                for (String arg:args){
                    Log.d(LOG_TAG, arg);
                    while(doc_detail==null){
                        doc_detail = Jsoup.connect(arg).timeout(15*1000).get();
                        Log.d(LOG_TAG, "Connect");
                        //Log.d(LOG_TAG, doc_detail.html());
                    }


                }
                html_detail = doc_detail.select("section.box");
                Log.d(LOG_TAG, html_detail.html());
                i=html_detail.get(0);


                _matchtime=i.select("div.half").get(0).html();

                _matchtype=i.select("div.half").get(1).html();

                _matchesttime=i.select("div.half").get(2).html();
                Log.d(LOG_TAG, _matchesttime);
                _team=i.select("b").first().text().replace("\\", "");
                _against=i.select("b").last().text().replace("\\", "");
                _teampersent=i.select("i").first().text();
                _againstpersent=i.select("i").last().text();

                _teamimagelink=i.select("div.team").first().attr("style").replace("float: right; margin-left: 10%; background: url('", "").replace("')", "").replace("\\", "");
                _againstimagelink=i.select("div.team").last().attr("style").replace("float: left; margin-right: 10%; background: url('", "").replace("')", "").replace("\\", "");



            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }
        protected void onPostExecute(String result) {
            //Log.d(LOG_TAG, _matchesttime);
            Log.d(LOG_TAG, "test2");
            vremya=_matchesttime.replace(" CEST", "");
            vremya=_matchesttime.replace(" CET", "");
            h=Integer.parseInt(vremya.substring(0, 2));
            m=vremya.substring(3);
            h=h+4;
            if(h>=24){
                h=h-24;
            }
            tvtime.setText(_matchesttime+"("+_matchtime+")");
            tvtype.setText(_matchtype);
            tvteam1.setText(_team);
            tvteam2.setText(_against);
            tvpersent1.setText(_teampersent);
            tvpersent2.setText(_againstpersent);
            if(h<10)
            {
                tvheretime.setText("0"+h+":"+m);
            }
            else
            {
                tvheretime.setText(h+":"+m);
            }
            UrlImageViewHelper.setUrlDrawable(team1image, _teamimagelink);
            UrlImageViewHelper.setUrlDrawable(team2image, _againstimagelink);
            pb.setVisibility(View.INVISIBLE);
        }

    }



    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Log.d(LOG_TAG,"CLICK!!!");
        Intent intent;
        switch (v.getId()) {
            case R.id.d2lbtn:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(_link));
                startActivity(intent);
                break;
        }
        switch (v.getId()) {
            case R.id.textView7:
                Toast.makeText(this, "click", Toast.LENGTH_SHORT).show();
                createAlarm(_team+" VS "+_against ,h,Integer.parseInt(m));
                break;
        }



    }


    public void createAlarm(String message, int hour, int minutes) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_MESSAGE, message)
                .putExtra(AlarmClock.EXTRA_HOUR, hour)
                .putExtra(AlarmClock.EXTRA_MINUTES, minutes);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


	/*
	public Bitmap getBitmapfromurl (String urlstring) throws IOException
	{
		Bitmap pic;
		URL url = new URL(urlstring);
        URLConnection conn = url.openConnection();
        pic = BitmapFactory.decodeStream(conn.getInputStream());
		return pic;
	}*/
}
