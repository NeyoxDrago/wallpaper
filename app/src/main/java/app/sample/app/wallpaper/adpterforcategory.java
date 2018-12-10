package app.sample.app.wallpaper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import app.sample.app.wallpaper.DatabaseFiles.LocalDATABASE;
import app.sample.app.wallpaper.DatabaseFiles.datasource;
import app.sample.app.wallpaper.DatabaseFiles.repository;
import dmax.dialog.SpotsDialog;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class adpterforcategory extends RecyclerView.Adapter<adpterforcategory.ViewHolder>{

    Context c;
    List<String> names = new ArrayList<>();
    List<String> Urls = new ArrayList<>();
    List<String> orientation = new ArrayList<>();
    onBottomReachedListener onBottomReachedListener;
    int pagesize;


    public adpterforcategory(Context c, List<String> names, List<String> urls , List<String> orientation , int pagesize) {
        this.c = c;
        this.names = names;
        Urls = urls;
        this.orientation = orientation;
        this.pagesize = pagesize;
    }

    public void setOnBottomReachedListener(onBottomReachedListener bottomReachedListener)
    {
        this.onBottomReachedListener = bottomReachedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(c).inflate(R.layout.layoutcategory , viewGroup , false);
        return new ViewHolder(v);

    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {



        final RequestOptions requestoptions = new RequestOptions()
                .placeholder(R.drawable.load);


        recentfragment recent = recentfragment.getinstance(c);
        recent.loadRecents();
        final List<recents> Recent = recent.Recent;


        for(int k=0;k<Recent.size();k++)
        {
            if(Recent.get(k).getImageLink().equals(Urls.get(i)))
            {
                viewHolder.like.setLiked(true);
                break;
            }
        }


        Glide.with(c).load(Urls.get(i)).apply(requestoptions).into(viewHolder.cat_img);

        viewHolder.cat_text.setText(names.get(i));
        int[] Colors = {Color.CYAN , Color.YELLOW, Color.GREEN };
        viewHolder.detail.setBackgroundColor(Colors[i%3]);


//        viewHolder.cat_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Dialog d = new Dialog(c);
//                if(orientation.get(i).equals("potrait"))
//                    d.setContentView(R.layout.category_dialog);
//                else
//                    d.setContentView(R.layout.category_dialog2);
//
//                ImageView image = d.findViewById(R.id.dialog_image);
//                Button b = d.findViewById(R.id.view_wallpaper);
//                b.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent  a = new Intent(c,ViewWallpaper.class);
//                        a.putExtra("ImageUrl" , Urls.get(i));
//                        c.startActivity(a);
//                        d.dismiss();
//                    }
//                });
//
//                Glide.with(c)
//                        .load(Urls.get(i))
//                        .apply(requestoptions).into(image);
//
//                d.show();
//            }
//        });

        viewHolder.cat_img.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View v) {

                Intent a = new Intent(c, ViewWallPaper2.class);
                a.putExtra("Url" , Urls.get(i));
                c.startActivity(a);

            }});

        viewHolder.like.setSaveEnabled(true);

        viewHolder.like.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                CompositeDisposable compositeDisposable = new CompositeDisposable();
                LocalDATABASE d = LocalDATABASE.getInstance(c);
               final repository Repository = (repository) repository.getInstance(datasource.getInstance(d.Dao()));

                Disposable disposable = Observable
                        .create(new ObservableOnSubscribe<Object>() {
                                    @Override
                                    public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                                        recents r = new recents(
                                                Urls.get(i),
                                                "Category",
                                                String.valueOf(System.currentTimeMillis())
                                        );
                                        Repository.insertRecents(r);
                                        emitter.onComplete();
                                    }
                                }
                        )
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Consumer<Object>() {
                                       @Override
                                       public void accept(Object o) throws Exception {

                                       }
                                   }, new Consumer<Throwable>() {
                                       @Override
                                       public void accept(Throwable throwable) throws Exception {

                                           Toast.makeText(c, "Item Already added", Toast.LENGTH_SHORT).show();
                                       }
                                   }
                                , new Action() {
                                    @Override
                                    public void run() throws Exception {

                                    }
                                });

                compositeDisposable.add(disposable);

            }

            @Override
            public void unLiked(LikeButton likeButton) {

                CompositeDisposable compositeDisposable = new CompositeDisposable();
                LocalDATABASE d = LocalDATABASE.getInstance(c);
                final repository Repository = (repository) repository.getInstance(datasource.getInstance(d.Dao()));

                Disposable disposable = Observable
                        .create(new ObservableOnSubscribe<Object>() {
                                    @Override
                                    public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                                        recents r = new recents(
                                                Urls.get(i),
                                                "Category",
                                                String.valueOf(System.currentTimeMillis())
                                        );
                                        Repository.deleterecents(r);
                                        emitter.onComplete();
                                    }
                                }
                        )
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Consumer<Object>() {
                                       @Override
                                       public void accept(Object o) throws Exception {

                                       }
                                   }, new Consumer<Throwable>() {
                                       @Override
                                       public void accept(Throwable throwable) throws Exception {

                                           Toast.makeText(c, "Task Not Done", Toast.LENGTH_SHORT).show();
                                       }
                                   }
                                , new Action() {
                                    @Override
                                    public void run() throws Exception {

                                    }
                                });

                compositeDisposable.add(disposable);

            }
        });

    }

    @Override
    public int getItemCount() {
        return Urls.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView cat_img;
        TextView cat_text;
        LinearLayout detail;
        LikeButton like;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            detail = itemView.findViewById(R.id.detail);
            cat_img = itemView.findViewById(R.id.categoryimage);
            cat_text = itemView.findViewById(R.id.categorytext);
            like = itemView.findViewById(R.id.categoryLike);

        }
    }

    public void sharetofacebook(final String url) {

    }


}
