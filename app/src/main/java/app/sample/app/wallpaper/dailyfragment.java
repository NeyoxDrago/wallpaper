package app.sample.app.wallpaper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;


public class dailyfragment extends Fragment   {

    private static dailyfragment INSTANCE = null;

    @SuppressLint("StaticFieldLeak")
    RecyclerView recycler;
    DatabaseReference mdef;
    List<String> Urls = new ArrayList<>();
    List<Integer> views = new ArrayList<>();
    List<String> orientation = new ArrayList<>();
    public dailyfragment() {
        // Required empty public constructor
    }

    protected Object doInBackground(Object[] objects) {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public static dailyfragment getinstance()
    {
        if(INSTANCE==null)
            INSTANCE = new dailyfragment();

        return INSTANCE;

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_dailyfragment, container, false);

        recycler = v.findViewById(R.id.dailyrecycler);
        recycler.setHasFixedSize(true);
        LinearLayoutManager linearLayout = new LinearLayoutManager(v.getContext());
        linearLayout.setReverseLayout(true);
        linearLayout.setStackFromEnd(true);
        recycler.setLayoutManager(linearLayout);


        mdef = FirebaseDatabase.getInstance().getReference().child("Category");
        Query query  = mdef.orderByChild("count").limitToLast(10);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Urls.clear();
                views.clear();
                orientation.clear();

                for(DataSnapshot i:dataSnapshot.getChildren())
                {
                    Urls.add(i.child("ImageUrl").getValue().toString());
                    views.add(Integer.valueOf(i.child("count").getValue().toString()));
                    orientation.add(i.child("orientation").getValue().toString());
                }

                adapterforTrending adapter = new adapterforTrending(v.getContext() , Urls ,views ,orientation);
                recycler.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final FloatingActionButton fab = v.findViewById(R.id.mainfab);

        SubActionButton.Builder action = new SubActionButton.Builder(Objects.requireNonNull(getActivity()));
        ImageView item1 = new ImageView(v.getContext());
        item1.setImageResource(R.drawable.ic_file_download);

        ImageView item2 = new ImageView(v.getContext());
        item2.setImageResource(R.drawable.ic_star);
        SubActionButton one = action.setContentView(item1)
                .setLayoutParams(
                        new com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton.LayoutParams(200,200))
                .build();
        SubActionButton two = action.setContentView(item2).build();

        final FloatingActionMenu menu = new FloatingActionMenu.Builder(getActivity())
                .addSubActionView(one)
                .addSubActionView(two)
                .attachTo(fab)
                .setStartAngle(225)
                .setEndAngle(315)
                .build();

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(v.getContext(), special.class);
                startActivity(a);
            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(v.getContext() , Categories.class);
                startActivity(a);
            }
        });


        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                        if(menu.isOpen())
                        {
                            menu.close(true);
                        }
                    fab.hide();
                } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                    fab.show();
                }
            }
        });

        return v;
    }

    @Override
    public void setInitialSavedState(@Nullable SavedState state) {
        super.setInitialSavedState(state);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }
}
