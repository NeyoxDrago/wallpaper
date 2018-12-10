package app.sample.app.wallpaper;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import dmax.dialog.SpotsDialog;

import static android.support.v4.app.ActivityCompat.requestPermissions;

public class adapterforspecial extends PagerAdapter {
    private Context c;
    private List<String> urls = new ArrayList<>();
    private LayoutInflater inflater;

    public adapterforspecial(Context c, List<String> urls) {
        this.c = c;
        this.inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.urls = urls;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        View v= inflater.inflate(R.layout.viewpageritem , container , false);

        final ImageView image = v.findViewById(R.id.viewpagerimage);

        Picasso.with(c).load(urls.get(position)).into(image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog a = new Dialog(c
                        , android.R.style.Theme_Black_NoTitleBar_Fullscreen);

                a.setContentView(R.layout.fulldialog2);
                a.getWindow().getAttributes().windowAnimations = R.style.Dilaoganimation;
                a.show();
                final int[] h = {0};
                ImageView image = a.findViewById(R.id.fulldialog_image);
                final AppBarLayout barLayout = a.findViewById(R.id.fullapp_bar_layout);
                final Button b = a.findViewById(R.id.fulldialogbutton2);
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (h[0] == 0) {
                            barLayout.setExpanded(false, false);
                            barLayout.setVisibility(View.GONE);
                            b.setVisibility(View.GONE);
                            h[0] = 1;
                        } else {
                            barLayout.setExpanded(true, true);
                            barLayout.setVisibility(View.VISIBLE);
                            b.setVisibility(View.VISIBLE);
                            h[0] = 0;
                        }

                    }
                });
                Toolbar t = a.findViewById(R.id.fulltoolbar);
                t.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        a.dismiss();
                    }
                });

                Glide.with(a.getContext())
                        .load(urls.get(position))
                        .into(image);


                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        {

                            AlertDialog alert = new SpotsDialog(c);
                            alert.show();
                            alert.setMessage("Please Wait...");

                            String name = UUID.randomUUID().toString()+".png";

                            Picasso.with(c)
                                    .load(urls.get(position)).into(
                                    new downloadHelperClass(
                                            c,
                                            alert ,
                                            c.getApplicationContext().getContentResolver() ,
                                            name ,
                                            "Wallpaper App Output")
                            );



                        }
                    }
                });
                    }
                });
        container.addView(v);



        return v;
    }

    @Override
    public void restoreState(@Nullable Parcelable state, @Nullable ClassLoader loader) {
        super.restoreState(state, loader);
    }

    @Nullable
    @Override
    public Parcelable saveState() {
        return super.saveState();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((CardView) object);
    }
}
