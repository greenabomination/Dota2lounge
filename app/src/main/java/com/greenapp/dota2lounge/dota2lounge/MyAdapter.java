package com.greenapp.dota2lounge.dota2lounge;


        import java.util.ArrayList;

        import android.content.Context;
        import android.graphics.drawable.Drawable;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.ProgressBar;
        import android.widget.TextView;

        import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class MyAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater linflater;
    ArrayList<matches> objects;
    Drawable bck;
    String res;

    MyAdapter(Context context, ArrayList<matches> _matches) {
        ctx=context;
        objects = _matches;
        linflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View view = convertView;
        int pcnt=0;
        if (view==null)
        {
            view=linflater.inflate(R.layout.item, parent, false);
        }
        matches m = getmatch(position);

        //	bck=new BitmapDrawable(m.backgrnd);
        ((TextView)view.findViewById(R.id.textView1)).setText(m.matchtime);
        //Log.d("myLogs","111");
        ((TextView)view.findViewById(R.id.textView2)).setText(m.matchtournament);
        ((TextView)view.findViewById(R.id.tvteam)).setText(m.team);
        //((TextView)view.findViewById(R.id.textView4)).setText("VS");

        //((ImageView)view.findViewById(R.id.imageView2)).setImageDrawable(R.drawable.vs);
        ((TextView)view.findViewById(R.id.textView5)).setText(m.against);
        ((TextView)view.findViewById(R.id.textView6)).setText(m.teampersent);
        ((TextView)view.findViewById(R.id.textView7)).setText(m.againstpersent);
        ((TextView)view.findViewById(R.id.textView8)).setText(m.matchlink);
        pcnt=Integer.parseInt((m.teampersent).replace("%", ""));
        ((ProgressBar)view.findViewById(R.id.progressBar1)).setProgress(pcnt);
        if(m.matchstatus.equals("match notavailable"))
        {
            ((LinearLayout)view.findViewById(R.id.lay1)).setBackgroundResource(R.color.Red);
            if (m.matchwinner.equals("team"))
            {
                ((ImageView)view.findViewById(R.id.imageView3)).setVisibility(ImageView.VISIBLE);
                ((ImageView)view.findViewById(R.id.imageView4)).setVisibility(ImageView.INVISIBLE);
            }
            else
            {
                ((ImageView)view.findViewById(R.id.imageView4)).setVisibility(ImageView.VISIBLE);
                ((ImageView)view.findViewById(R.id.imageView3)).setVisibility(ImageView.INVISIBLE);
            }
        }
        else
        {
            ((LinearLayout)view.findViewById(R.id.lay1)).setBackgroundResource(R.color.Green);
            ((ImageView)view.findViewById(R.id.imageView3)).setVisibility(ImageView.INVISIBLE);
            ((ImageView)view.findViewById(R.id.imageView4)).setVisibility(ImageView.INVISIBLE);
        }

		/*((ImageView)view.findViewById(R.id.imageView1)).setImageBitmap(m.teampic);
		((ImageView)view.findViewById(R.id.imageView2)).setImageBitmap(m.againstpic);*/
        //((LinearLayout)view.findViewById(R.id.lay2)).setBackgroundDrawable(bck);
        UrlImageViewHelper.setUrlDrawable((ImageView)view.findViewById(R.id.imageView1), m.teampic);
        UrlImageViewHelper.setUrlDrawable((ImageView)view.findViewById(R.id.imageView2), m.againstpic);


		/*d=new Detail();
		d.execute(m.matchlink);
		try {
			res=d.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		((TextView)view.findViewById(R.id.textView8)).setText(res);*/

	/*	int h=0, w =0;
		float x= 0, y =0;
		h=(int)view.findViewById(R.id.imageView1).getHeight();
		w=(int)view.findViewById(R.id.imageView1).getWidth();
		x=view.findViewById(R.id.imageView1).getPaddingTop();
		y=view.findViewById(R.id.imageView1).getPaddingLeft();
		((TextView)view.findViewById(R.id.textView4)).setText(h+"x"+w+"\n"+x+"x"+y);*/

        return view;
    }

    matches getmatch(int position)
    {
        return ((matches) getItem(position));
    }

}

