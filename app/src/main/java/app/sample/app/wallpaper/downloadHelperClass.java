package app.sample.app.wallpaper;


import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.ref.WeakReference;

public class downloadHelperClass implements Target {

    private Context c;
    private WeakReference<AlertDialog> alertdialog;
    private WeakReference<ContentResolver> contentresolver;
    private String name , desc;

    public downloadHelperClass(Context c,AlertDialog alertdialog, ContentResolver contentresolver, String name, String desc) {
        this.c = c;
        this.alertdialog = new WeakReference<>(alertdialog);
        this.contentresolver = new WeakReference<>(contentresolver);
        this.name = name;
        this.desc = desc;
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

        ContentResolver r =contentresolver.get();
        AlertDialog alert = alertdialog.get();
        if(r!=null)
            MediaStore.Images.Media.insertImage(r ,bitmap ,name ,desc);

        alert.dismiss();
        Toast.makeText(c, "File Saved to Storage Successfully...", Toast.LENGTH_SHORT).show();


        NotificationCompat.Builder notification
                = new NotificationCompat.Builder(c);

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setDataAndType(Uri.parse("InternalStorage/Pictures/"+name+"/"), "*/*");;
        PendingIntent pIntent = PendingIntent.getActivity(c, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        notification
                .setAutoCancel(false)
                .setContentTitle("Downloaded")
                .addAction(R.drawable.ic_back ,"View Image" ,pIntent);

        notification.setSmallIcon(R.drawable.ic_file_download);
        notification.setTicker("Ticker here");

        final NotificationManager notificationManager = (NotificationManager)
                c.getSystemService(Context.NOTIFICATION_SERVICE);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("my_channel" , "channel_name" , NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notification.setChannelId("my_channel");


        notificationManager.notify(4092 , notification.build());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                notificationManager.cancel(4092);

            }
        },15000);

    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }

}
