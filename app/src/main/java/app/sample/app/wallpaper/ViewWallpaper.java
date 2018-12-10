package app.sample.app.wallpaper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import static app.sample.app.wallpaper.Uploadfile.GALLERY_CODE;

public class ViewWallpaper extends AppCompatActivity {

    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton fab;
    ImageView imageView;
    CoordinatorLayout rootlayout;

    CompositeDisposable compositeDisposable;
    repository Repository;
    String Url;

    FloatingActionMenu fabmenu;
    com.github.clans.fab.FloatingActionButton fbshare , Ishare;

    CallbackManager callbackManager;
    ShareDialog shareDialog;

    DatabaseReference mdef;
    Bitmap b;

    Uri resultUri;

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {


//            DisplayMetrics displayMetrics = new DisplayMetrics();
//
//            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//            int height = displayMetrics.heightPixels;
//            int width = displayMetrics.widthPixels;
//            bitmap = Bitmap.createScaledBitmap(bitmap,width, height, true);
//
//            WallpaperManager Wmanager = WallpaperManager.getInstance(getApplicationContext());
//            try {
////                Intent a = new Intent(Wmanager.getCropAndSetWallpaperIntent(getImageUri(ViewWallpaper.this ,bitmap)));
////                startActivity(a);
//
//                Wmanager.setBitmap(bitmap);
//                Snackbar.make(rootlayout , "Wallpaper was set" , Snackbar.LENGTH_LONG).show();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }


            CropImage.activity(getImageUri(ViewWallpaper.this , bitmap))
                    .start(ViewWallpaper.this);


        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private Target facebooktarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            SharePhoto sharePhoto = new SharePhoto.Builder()
                    .setBitmap(bitmap)
                    .build();

            if(ShareDialog.canShow(SharePhotoContent.class))
            {
                SharePhotoContent sharePhotoContent = new SharePhotoContent.Builder()
                        .addPhoto(sharePhoto)
                        .build();

                shareDialog.show(sharePhotoContent);
            }
            SharePhotoContent sharePhotoContent = new SharePhotoContent.Builder()
                    .addPhoto(sharePhoto)
                    .build();

            shareDialog.show(sharePhotoContent);

        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_wallpaper);

        Url = getIntent().getStringExtra("ImageUrl");

        mdef = FirebaseDatabase.getInstance().getReference().child("Category");


        ////////////////////////

        compositeDisposable = new CompositeDisposable();
        LocalDATABASE d = LocalDATABASE.getInstance(this);
        Repository = (repository) repository.getInstance(datasource.getInstance(d.Dao()));

        ///////////////////////

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        ///////////////////////

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        rootlayout = findViewById(R.id.rootlayout);
        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedTextAppearence);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedTextAppearence);


        imageView = findViewById(R.id.collapsingImage);
        Picasso.with(this)
                .load(Url)
                .placeholder(R.drawable.load)
                .into(imageView);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int[] i = {0};
                final Dialog a = new Dialog(ViewWallpaper.this
                        , android.R.style.Theme_Black_NoTitleBar_Fullscreen);

                a.setContentView(R.layout.fulldialog);
                a.getWindow().getAttributes().windowAnimations = R.style.Dilaoganimation;
                a.show();
                ImageView image = a.findViewById(R.id.fulldialog_image);
                final AppBarLayout barLayout = a.findViewById(R.id.fullapp_bar_layout);
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(i[0] ==0){
                            barLayout.setExpanded(false , false);
                            barLayout.setVisibility(View.GONE);
                            i[0] =1;
                        }
                        else
                        {
                            barLayout.setExpanded(true , true);
                            barLayout.setVisibility(View.VISIBLE);
                            i[0] =0;
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

                Picasso.with(a.getContext())
                        .load(Url)
                        .placeholder(R.drawable.load)
                        .into(image);
            }
        });

        fab = findViewById(R.id.fab);
        fabmenu = findViewById(R.id.menu);

        if(MyApplication.i==1) {
            new TapTargetSequence(this)
                    .targets(
                            TapTarget.forView(findViewById(R.id.fab), "Set Wallpaper", "Click this button , to set the desired Image above as your device WallPaper.")
                                    .outerCircleColor(R.color.red)
                                    .outerCircleAlpha(0.96f)
                                    .targetCircleColor(R.color.white)
                                    .titleTextSize(20)
                                    .targetRadius(40)
                                    .descriptionTextSize(10)
                                    .cancelable(true)
                                    .transparentTarget(true),
                            TapTarget.forView(findViewById(R.id.menu), "Share Options", "Click here , to get further share this wallpaper on FaceBook.")
                                    .outerCircleColor(R.color.blue)
                                    .outerCircleAlpha(0.96f)
                                    .targetCircleColor(R.color.green)
                                    .titleTextSize(20)
                                    .targetRadius(40)
                                    .descriptionTextSize(10)
                                    .cancelable(true)
                                    .transparentTarget(true),
                            TapTarget.forView(findViewById(R.id.downloadfab), "Download", "Click Here , to Downlaod this image to your device storage.")
                                    .outerCircleColor(R.color.green)
                                    .outerCircleAlpha(0.96f)
                                    .targetCircleColor(R.color.blue)
                                    .titleTextSize(20)
                                    .targetRadius(40)
                                    .descriptionTextSize(10)
                                    .cancelable(true)
                                    .transparentTarget(true)


                    ).start();

            MyApplication.i=0;

        }
        fbshare = findViewById(R.id.facebook_share);

        sharetofacebook(Url);
        updatecount();


        final ProgressDialog ddialog = new ProgressDialog(this);
        ddialog.setMessage("Setting Wallpaper!!");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ddialog.show();
                Picasso.with(ViewWallpaper.this)
                        .load(Url).into(target);

                ddialog.dismiss();
            }
        });


        FloatingActionButton dfab = findViewById(R.id.downloadfab);
        dfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ActivityCompat.checkSelfPermission(ViewWallpaper.this , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE} , 1000);
                    }
                }
                else
                {

                    AlertDialog alert = new SpotsDialog(ViewWallpaper.this);
                    alert.show();
                    alert.setMessage("Please Wait...");

                    String name = UUID.randomUUID().toString()+".png";

                    Picasso.with(ViewWallpaper.this)
                            .load(Url).into(
                                    new downloadHelperClass(
                                            ViewWallpaper.this,
                                            alert ,
                                            getApplicationContext().getContentResolver() ,
                                            name ,
                                            "Wallpaper App Output")
                    );



                }
            }
        });

    }

    public void sharetofacebook(final String url) {
        fbshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Toast.makeText(ViewWallpaper.this, "Wallpaper shared Successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        finish();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(ViewWallpaper.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                Picasso.with(ViewWallpaper.this)
                        .load(url)
                        .into(facebooktarget);

            }
        });
    }

    private void updatecount() {

        final String[] v = {""};
        mdef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot i: dataSnapshot.getChildren())
                {

                    if(Url.equals(i.child("ImageUrl").getValue().toString())) {

                        v[0] = i.getKey();

                    }
                }

                mdef = mdef.child(v[0]);
                final int[] val = new int[1];
                val[0] =1;
                mdef.child("count").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        val[0] = Integer.parseInt(dataSnapshot.getValue().toString());
                        val[0] +=1;
                        Map<String , Object> map = new HashMap<>();
                        map.put("count" , val[0]);
                        mdef.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful())
                                {
                                    Log.d("Update" , "data updated");
                                }

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        Toast.makeText(ViewWallpaper.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void addtorecents() {


        Disposable disposable = Observable
                .create(new ObservableOnSubscribe<Object>() {
                                                      @Override
                                                      public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                                                          recents r = new recents(
                                                                  Url,
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

                                   Toast.makeText(ViewWallpaper.this, "Task Not Done", Toast.LENGTH_SHORT).show();
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
    protected void onDestroy() {
        super.onDestroy();
        Picasso.with(this).cancelRequest(target);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Use Above Navigate button to go to previous page", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                 resultUri = result.getUri();
                WallpaperManager Wmanager = WallpaperManager.getInstance(getApplicationContext());
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    Wmanager.setBitmap(bitmap);
                    Snackbar.make(rootlayout ,"Wallpaper has changed successfully!!" , Snackbar.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

}
