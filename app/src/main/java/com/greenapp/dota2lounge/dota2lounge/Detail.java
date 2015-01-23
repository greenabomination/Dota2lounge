package com.greenapp.dota2lounge.dota2lounge;
        import java.io.IOException;
        import org.jsoup.Jsoup;
        import org.jsoup.nodes.Document;
        import org.jsoup.select.Elements;
        import android.os.AsyncTask;
        import android.widget.TextView;



public class Detail extends AsyncTask<String, Void, String> {

    String _matchtime;
    String _matchtype;
    String _matchcesttime;
    TextView t_v;
    public String[] res;

    @Override
    protected String doInBackground(String... arg) {

        Document doc_detail;
        Elements html_detail;



        _matchtype="";
        for (String _link: arg)
        {

            try {
                doc_detail=Jsoup.connect(_link).get();

                html_detail=doc_detail.select("div.gradient > div.half");
                _matchtime=html_detail.get(0).html();
                _matchtype=html_detail.get(1).html();
                _matchcesttime=html_detail.get(2).html();





            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return _matchtype;

    }

    protected void onPostExecute(String  results) {

    }
}