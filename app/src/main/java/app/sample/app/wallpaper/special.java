package app.sample.app.wallpaper;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class special extends AppCompatActivity {

    ViewPager viewPager;
    private List<String> urls = new ArrayList<>();
    DatabaseReference mdef;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special);


        viewPager = findViewById(R.id.specialviewpager);
        viewPager.setClipToPadding(false);
        viewPager.setPadding(100, 100, 100, 100);

        viewPager.setPageMargin(20);


        mdef = FirebaseDatabase.getInstance().getReference().child("Special_Images");
        mdef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot i : dataSnapshot.getChildren())
                {
                    if(!urls.contains((i.getValue()).toString()))
                        urls.add(i.getValue().toString());

                }
                adapterforspecial adapter = new adapterforspecial(special.this , urls);
                viewPager.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
