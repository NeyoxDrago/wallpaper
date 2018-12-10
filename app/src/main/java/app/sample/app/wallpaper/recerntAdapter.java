package app.sample.app.wallpaper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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

import java.util.List;
import java.util.UUID;

import dmax.dialog.SpotsDialog;

public class recerntAdapter extends RecyclerView.Adapter<recerntAdapter.ViewHolder> {

    private Context c;
    private List<recents> Recent;

    public recerntAdapter(Context c, List<recents> recent) {
        this.c = c;
        Recent = recent;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(c).inflate(R.layout.layoutcategory2, viewGroup , false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        Picasso.with(c).load(Recent.get(i).getImageLink())
                .placeholder(R.drawable.load)
                .into(viewHolder.image);

        viewHolder.image.setOnClickListener(new View.OnClickListener() {
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
                        .load(Recent.get(i).getImageLink())
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
                                    .load(Recent.get(i).getImageLink()).into(
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

    }

    @Override
    public int getItemCount() {
        return Recent.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.imagerecent);
        }
    }
}
