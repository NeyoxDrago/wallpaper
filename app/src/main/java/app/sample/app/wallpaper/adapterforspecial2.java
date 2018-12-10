package app.sample.app.wallpaper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import dmax.dialog.SpotsDialog;

public class adapterforspecial2 extends BaseAdapter {

    private Context c;
    private List<String> Urls = new ArrayList<>();

    public adapterforspecial2(Context c, List<String> Urls) {
        this.c = c;
        this.Urls = Urls;
    }

    @Override
    public int getCount() {
        if(Urls.size()==0)
            return 1;

        return Urls.size();
    }

    @Override
    public Object getItem(int position) {
        return Urls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
       View rootView = convertView;

       if(rootView==null)
       {
           LayoutInflater inflater = LayoutInflater.from(c);
           View v = inflater.inflate(R.layout.special2,parent ,false);

           ImageView image = v.findViewById(R.id.imagespecial2);
           Picasso.with(c).load(Urls.get(position)).placeholder(R.drawable.load).into(image);
           image.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   final Dialog d = new Dialog(c);
                   d.setContentView(R.layout.downloadrequest);
                   Button b = d.findViewById(R.id.yes);
                   Button n = d.findViewById(R.id.no);
                   n.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           d.dismiss();
                       }
                   });

                   b.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {

                           AlertDialog alert = new SpotsDialog(c);
                           alert.show();
                           alert.setMessage("Please Wait...");


                           String name = UUID.randomUUID().toString()+".png";
                           Picasso.with(c)
                                   .load(Urls.get(position))
                                   .into(
                                           new downloadHelperClass(
                                                   c,
                                                   alert ,
                                                   c.getContentResolver() ,
                                                   name ,
                                                   "Wallpaper App Output")
                                   );
                           d.dismiss();
                       }
                   });

                   d.show();

               }
           });

           return v;
       }
       return rootView;
    }
}
