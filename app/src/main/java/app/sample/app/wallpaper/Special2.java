package app.sample.app.wallpaper;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

public class Special2 extends AppCompatActivity {

    FeatureCoverFlow coverFlow;
    List<String> Urls = new ArrayList<>();
    DatabaseReference mdef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special2);


        coverFlow = findViewById(R.id.coverflow);
        mdef = FirebaseDatabase.getInstance().getReference().child("Special_Images");



      Urls.add("https://wallpapersultra.net/wp-content/uploads/We-Do-Not-Need-Magic-JK-Rowling-Wallpaper-For-iPhone-6-Plus.jpg");


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mdef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot i:dataSnapshot.getChildren())
                        {
                            Urls.add(i.getValue().toString());
                        }

                        adapterforspecial2 adapter = new adapterforspecial2(Special2.this , Urls);
                        coverFlow.setAdapter(adapter);
                        coverFlow.setSeletedItemPosition(0);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        },1);
        adapterforspecial2 adapter = new adapterforspecial2(Special2.this , Urls);
        coverFlow.setAdapter(adapter);

    }

}
