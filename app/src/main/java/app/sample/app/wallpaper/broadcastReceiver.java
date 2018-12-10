package app.sample.app.wallpaper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class broadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if("Wallpaper_ACTION".equals(intent.getAction()))
        {
            Toast.makeText(context, intent.getStringExtra("Url"), Toast.LENGTH_SHORT).show();
        }
        if("Alarm_Action".equals(intent.getAction())) {
            Toast.makeText(context, "Action Triggered", Toast.LENGTH_SHORT).show();

            AlertDialog alert = new AlertDialog.Builder(context)
                    .setMessage("Change WallPaper")
                    .setTitle("Dialog")
                    .create();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                alert.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
            }else
                alert.getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);


            try {



            }catch(WindowManager.BadTokenException e)
            {
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
            }        }




    }

}
