package app.sample.app.wallpaper;

import android.arch.persistence.room.Database;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Categories extends AppCompatActivity {

    private List<String> urls = new ArrayList<>();
    private List<String> names = new ArrayList<>();
    RecyclerView mrecycler;
    DatabaseReference mdef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        mrecycler = findViewById(R.id.recyclercategory);
        mdef = FirebaseDatabase.getInstance().getReference().child("Categories");
        mrecycler.setLayoutManager(new LinearLayoutManager(this));
        mrecycler.setHasFixedSize(true);
        mrecycler.setItemViewCacheSize(20);
        mrecycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mrecycler.setDrawingCacheEnabled(true);
        mrecycler.setAlwaysDrawnWithCacheEnabled(true);

        mdef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot i : dataSnapshot.getChildren())
                {
                    urls.add(i.child("url").getValue().toString());
                    names.add(i.child("name").getValue().toString());
                }

                adapterforcategories adapter = new adapterforcategories(Categories.this , urls , names);
                mrecycler.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
