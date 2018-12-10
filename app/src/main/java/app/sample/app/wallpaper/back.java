package app.sample.app.wallpaper;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import static app.sample.app.wallpaper.MyApplication.CHANNEL_ID;

public class back extends IntentService {

    PowerManager.WakeLock wakelock;
    public static final String TAG = "back";
    public static Notification notify;

    public back() {
        super("back");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {


        for(int i =0;i<100;i++)
        {
            Log.d("Activity running" , String.valueOf(i));
            SystemClock.sleep(2000);
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
        setIntentRedelivery(true);

        PowerManager manager = (PowerManager) getSystemService(POWER_SERVICE);
        wakelock = manager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK , "back:TAG" );

        wakelock.acquire(10*60*1000L /*10 minutes*/);


        Intent notificationintent = new Intent(this , MainActivity.class);
        PendingIntent intent1 = PendingIntent.getActivity(this , 0, notificationintent , 0);

         notify =
                new NotificationCompat
                        .Builder(this , CHANNEL_ID)
                        .setContentTitle("WallPaper App")
                        .setSmallIcon(R.drawable.ic_file_download)
                        .setContentText("Change WallPaper")
                        .setAutoCancel(false)
                        .setContentIntent(intent1)
                        .build();

         startForeground(1 , notify);
    }

    @Override
    public void onDestroy() {

        System.exit(0);
    }

}