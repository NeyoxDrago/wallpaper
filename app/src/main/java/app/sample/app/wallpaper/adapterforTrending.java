package app.sample.app.wallpaper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import dmax.dialog.SpotsDialog;

public class adapterforTrending extends RecyclerView.Adapter<adapterforTrending.ViewHolder> {

    private Context c;
    private List<String> urls = new ArrayList<>();
    private List<Integer> views = new ArrayList<>();
    List<String> orientation = new ArrayList<>();

    public adapterforTrending(Context c, List<String> urls,List<Integer> views , List<String> orientation) {
        this.c = c;
        this.urls = urls;
        this.views= views;
        this.orientation = orientation;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(c).inflate(R.layout.layoutcategory3 , viewGroup ,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        Picasso.with(c).load(urls.get(i)).placeholder(R.drawable.load).into(viewHolder.image);
        viewHolder.text.setText(views.get(i) + "  Views");


        viewHolder.image.setOnLongClickListener(new View.OnLongClickListener() {
                                                    @Override
                                                    public boolean onLongClick(View v) {
                                                        final Dialog d = new Dialog(c);
                                                        if (orientation.get(i).equals("potrait"))
                                                            d.setContentView(R.layout.category_dialog);
                                                        else
                                                            d.setContentView(R.layout.category_dialog2);

                                                        ImageView image = d.findViewById(R.id.dialog_image);
                                                        Button b = d.findViewById(R.id.view_wallpaper);
                                                        b.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                Intent a = new Intent(c, ViewWallpaper.class);
                                                                a.putExtra("ImageUrl", urls.get(i));
                                                                c.startActivity(a);
                                                                d.dismiss();

                                                            }
                                                        });
                                                        Picasso.with(c).load(urls.get(i)).placeholder(R.drawable.load)
                                                                .into(image);
                                                        d.show();
                                                        return true;
                                                    }
                                                });


                viewHolder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int[] h = {0};
                        final Dialog a = new Dialog(c
                                , android.R.style.Theme_Black_NoTitleBar_Fullscreen);

                        a.setContentView(R.layout.fulldialog2);
                        a.getWindow().getAttributes().windowAnimations = R.style.Dilaoganimation;
                        a.show();
                        Toolbar t = a.findViewById(R.id.fulltoolbar);
                        t.setNavigationOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                a.dismiss();
                            }
                        });
                        ImageView image = a.findViewById(R.id.fulldialog_image);
                        final AppBarLayout barLayout = a.findViewById(R.id.fullapp_bar_layout);
                        final Button b =a.findViewById(R.id.fulldialogbutton2);
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


                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                AlertDialog alert = new SpotsDialog(c);
                                alert.show();
                                alert.setMessage("Please Wait...");


                                String name = UUID.randomUUID().toString() + ".png";
                                Picasso.with(c)
                                        .load(urls.get(i))
                                        .into(
                                                new downloadHelperClass(
                                                        c,
                                                        alert,
                                                        c.getContentResolver(),
                                                        name,
                                                        "Wallpaper App Output")
                                        );
                                Toolbar t = a.findViewById(R.id.fulltoolbar);
                                t.setNavigationOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        a.dismiss();
                                    }
                                });
                            }
                        });


                        Picasso.with(a.getContext())
                                .load(urls.get(i))
                                .placeholder(R.drawable.load)
                                .into(image);



                    }
                });

            }


    @Override
    public int getItemCount() {
        return urls.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView text;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.dailyimage);
            text = itemView.findViewById(R.id.imageviews);



        }
    }
}
