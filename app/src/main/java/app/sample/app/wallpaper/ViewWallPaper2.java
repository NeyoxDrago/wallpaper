package app.sample.app.wallpaper;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import app.sample.app.wallpaper.DatabaseFiles.repository;
import dmax.dialog.SpotsDialog;
import io.reactivex.disposables.CompositeDisposable;

import static android.graphics.Rect.CREATOR;

public class ViewWallPaper2 extends AppCompatActivity {

    FloatingActionButton fab;
    ImageView imageView;

    CompositeDisposable compositeDisposable;
    repository Repository;
    String Url;

    FloatingActionMenu fabmenu;
    com.github.clans.fab.FloatingActionButton fbshare, download, wall;

    CallbackManager callbackManager;
    ShareDialog shareDialog;

    DatabaseReference mdef;

    Uri resultUri;

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {



            DisplayMetrics displayMetrics = new DisplayMetrics();

            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            bitmap = Bitmap.createScaledBitmap(bitmap,width, height, true);

            final WallpaperManager Wmanager = WallpaperManager.getInstance(getApplicationContext());




            final Dialog ddialog = new Dialog(ViewWallPaper2.this);
            ddialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            ddialog.setContentView(R.layout.cropdialog);

            ddialog.show();

            CardView crop = ddialog.findViewById(R.id.cropcard);
            CardView set = ddialog.findViewById(R.id.setcard);

            final Bitmap finalBitmap = bitmap;
            crop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CropImage.activity(getImageUri(ViewWallPaper2.this , finalBitmap))
                            .start(ViewWallPaper2.this);

                    ddialog.dismiss();

                    if(fabmenu.isOpened())
                        fabmenu.close(true);

                }
            });
            set.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Toast.makeText(ViewWallPaper2.this, "done", Toast.LENGTH_SHORT).show();
                            Wmanager.setBitmap(finalBitmap,null,true ,WallpaperManager.FLAG_LOCK | WallpaperManager.FLAG_SYSTEM);
                        }else {
                            Toast.makeText(ViewWallPaper2.this, "This Feature is not supported by your device !!!", Toast.LENGTH_SHORT).show();

                        }
                        ddialog.dismiss();

                        if(fabmenu.isOpened())
                            fabmenu.close(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            });

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

            if (ShareDialog.canShow(SharePhotoContent.class)) {
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_wallpaper2);


        imageView = findViewById(R.id.collapsingImage2);
        mdef = FirebaseDatabase.getInstance().getReference().child("Category");

        final String Url = getIntent().getStringExtra("Url");
        fbshare = findViewById(R.id.facebook_share);
        wall = findViewById(R.id.fab);
        download = findViewById(R.id.downloadfab);

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        fabmenu= findViewById(R.id.menu);
        final AppBarLayout appBarLayout = findViewById(R.id.app_bar_layout2);

        Glide.with(this)
                .load(Url).into(imageView);

        final Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        final int[] h = {0};
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(h[0] ==0)
                {
                    appBarLayout.setExpanded(false,false);
                    appBarLayout.setVisibility(View.GONE);
                    if(fabmenu.isOpened())
                        fabmenu.close(true);

                    fabmenu.setVisibility(View.GONE);

                    h[0] =1;

                }
                else
                {
                    appBarLayout.setExpanded(true,true);
                    appBarLayout.setVisibility(View.VISIBLE);
                    if(fabmenu.isOpened())
                        fabmenu.close(true);

                    fabmenu.setVisibility(View.VISIBLE);

                    h[0] =0;
                }

            }
        });


        fbshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog g = new ProgressDialog(ViewWallPaper2.this);
                g.setMessage("Loading...");
                g.setCancelable(false);
                g.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        g.setCancelable(true);

                    }
                },16000);

                Picasso.with(ViewWallPaper2.this)
                        .load(Url)
                        .into(facebooktarget);

                shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Toast.makeText(ViewWallPaper2.this, "Wallpaper shared Successfully", Toast.LENGTH_SHORT).show();
                        g.dismiss();
                    }

                    @Override
                    public void onCancel() {
                        g.dismiss();
                        finish();
                        Intent a = new Intent(ViewWallPaper2.this , MainActivity.class);
                        startActivity(a);

                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(ViewWallPaper2.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                        g.dismiss();
                        finish();
                        Intent a = new Intent(ViewWallPaper2.this , MainActivity.class);
                        startActivity(a);
                    }
                });

                if(fabmenu.isOpened())
                    fabmenu.close(true);

            }
        });


        wall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.with(ViewWallPaper2.this)
                        .load(Url)
                        .into(target);
            }
        });




        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(ViewWallPaper2.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
                    }

                } else {

                    AlertDialog alert = new SpotsDialog(ViewWallPaper2.this);
                    alert.show();
                    alert.setMessage("Please Wait...");

                    String name = UUID.randomUUID().toString() + ".png";

                    Picasso.with(ViewWallPaper2.this)
                            .load(Url).into(
                            new downloadHelperClass(
                                    ViewWallPaper2.this,
                                    alert,
                                    getApplicationContext().getContentResolver(),
                                    name,
                                    "Wallpaper App Output")
                    );


                }
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

                        Toast.makeText(ViewWallPaper2.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Picasso.with(this).cancelRequest(target);
    }

    @Override
    public void onBackPressed() {
        //Toast.makeText(this, "Use Above Navigate button to go to previous page", Toast.LENGTH_LONG).show();
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

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


}
