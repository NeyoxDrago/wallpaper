package app.sample.app.wallpaper;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Parcelable;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.core.Context;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    RelativeLayout mainview;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR , 4);
        c.set(Calendar.MINUTE , 34);
        c.set(Calendar.SECOND , 00);
        startAlarm(c);

//        Intent a = new Intent(this , back.class);
//        ContextCompat.startForegroundService(this ,a);


        mainview = findViewById(R.id.mainview);
        viewPager = findViewById(R.id.viewpager);
        adapterforViewPager adapter = new adapterforViewPager( getSupportFragmentManager(), this);
        viewPager.setAdapter(adapter);
        adapter.saveState();

//        showDialog(this);

        tabLayout = findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        IntentFilter filter = new IntentFilter();
        filter.addAction("Alarm_action");
        registerReceiver(broadcastReceiver , filter);


        TextView signin = findViewById(R.id.signinbutton);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    if(FirebaseAuth.getInstance().getCurrentUser()==null)
                    {
                        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),1001);
                    }
                    else
                    {
                        Snackbar.make(tabLayout,"Already signed as " + FirebaseAuth.getInstance().getCurrentUser().getEmail(), Snackbar.LENGTH_SHORT).show();
                    }

                }
            }
        });

//        bview = findViewById(R.id.bottommenu);
//        bview.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                switch(menuItem.getItemId())
//                {
//                    case R.id.one:
//                       Intent a = new Intent(MainActivity.this , Uploadfile.class);
//                       startActivity(a);
//                       break;
//                }
//                return true;
//            }
//        });

//        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int i, float v, int i1) {
//
//            }
//
//            @Override
//            public void onPageSelected(int i) {
//
//                if(i!=0)
//                {
//                    bview.setVisibility(View.GONE);
//                }else bview.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int i) {
//
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1001 && resultCode==RESULT_CANCELED)
        {
            Toast.makeText(this, "User Not signed in.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.signin , menu);
        return true;
    }
    public static void showDialog(android.content.Context c)
    {
        List<String> Urls = new ArrayList<>();
        Urls.clear();
        Urls.add("https://i.pinimg.com/originals/e1/89/6d/e1896d7a333e753e0068a3b7e02dd61c.jpg");
        Urls.add("https://cdn-images-1.medium.com/max/1600/1*FwbW3sqHSKfO3B1QRRH2fQ.png");
        Urls.add("https://d9hhrg4mnvzow.cloudfront.net/seopages.adobeprojectm.com/make/wallpaper-maker/e486b051-wallpaper-1_09y0d809x0d8000000.jpeg");
        Urls.add("https://wallpaperclicker.com/storage/wallpaper/COOL-WALLPAPER-7017-79350280.jpg");

//        Toast.makeText(c, "Alarm triggered by wallpaper app", Toast.LENGTH_SHORT).show();

        Dialog d = new Dialog(c , android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        d.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        d.setContentView(R.layout.showdialog);

        ViewPager viewPager = d.findViewById(R.id.dialog_viewPager);
        viewPager.setClipToPadding(false);
        viewPager.setPadding(100, 100, 100, 100);
        viewPager.setPageMargin(20);

        Adapterforshow adapter = new Adapterforshow(Urls , c);
        viewPager.setAdapter(adapter);
        d.show();
        d.setCancelable(true);
    }

    private static  class Adapterforshow extends PagerAdapter {

        List<String> Urls;
        android.content.Context c;

        public Adapterforshow(List<String> urls, android.content.Context c) {
            Urls = urls;
            this.c = c;
        }

        @Override
        public int getCount() {
            return Urls.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return (view==o);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View v = LayoutInflater.from(c).inflate(R.layout.showitem , container,false);

            ImageView imageView = v.findViewById(R.id.showimage);
            RequestOptions options  = new RequestOptions().placeholder(R.drawable.ic_launcher_foreground);
            Glide.with(c).load(Urls.get(position)).apply(options).into(imageView);

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
            container.removeView((ImageView) object);
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(android.content.Context context, Intent intent) {

            if("Alarm_action".equals(intent.getAction()))
            {
                Toast.makeText(context, "Waah >>>>> Waah", Toast.LENGTH_SHORT).show();
                showDialog(MainActivity.this);
            }

        }
    };

    private void startAlarm(Calendar c) {

        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent a = new Intent();
        a.setAction("Alarm_action");
        a.putExtra("Url","Send Url from above");
        a.putExtra("show_Fragment" , 6);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this , 1 , a ,0);

        if(c.before(Calendar.getInstance()))
        {
            c.add(Calendar.DAY_OF_WEEK , c.get(Calendar.DAY_OF_WEEK)+1);
        }

        alarm.setExact(AlarmManager.RTC_WAKEUP , c.getTimeInMillis(),pendingIntent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}

    /*
    TODO: FIX THIS BUG
       1. NOTIFICATION OF PHOTO SAVED. done
       2. AUTHENTICATION done
       3. TARGETVIEW only once
       4. VIEW WALLPAPER BACK BUTTON done but bhut bekar working
       5. download dialog in special
       6. Trending page :: to top fab and to set limited scrolling.
       7. Wallpaper setting done
       8. Bottom Navigation done
     */