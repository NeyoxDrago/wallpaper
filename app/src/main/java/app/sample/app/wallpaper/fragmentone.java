package app.sample.app.wallpaper;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class fragmentone extends Fragment {

    private static fragmentone INSTANCE = null;
    private RecyclerView mrecycler;
    List<String> names = new ArrayList<>();
    List<String> Urls = new ArrayList<>();
    List<String> orientation = new ArrayList<>();


    DatabaseReference mdef;
    ProgressDialog dialog;
    int pagesize =20;
    adpterforcategory adapter;

    public fragmentone() {
        // Required empty public constructor
    }

    public static fragmentone getinstance()
    {
        if(INSTANCE==null)
            INSTANCE = new fragmentone();

        return INSTANCE;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        final View v =  inflater.inflate(R.layout.fragment_fragmentone, container, false);
      mrecycler = v.findViewById(R.id.recycler);
        dialog= new ProgressDialog(v.getContext());
        dialog.setMessage("Loading");
        dialog.show();



      mdef = FirebaseDatabase.getInstance().getReference().child("Category");

      adapter = new adpterforcategory(v.getContext(),names,Urls , orientation , pagesize);

        adapter.setOnBottomReachedListener(new onBottomReachedListener() {
            @Override
            public void OnBottomReached(int position) {

                pagesize = pagesize+10;
                adapter.notifyDataSetChanged();
            }
        });

      mrecycler.setHasFixedSize(true);
      mrecycler.setNestedScrollingEnabled(true);
      mrecycler.setItemViewCacheSize(20);
      mrecycler.setDrawingCacheEnabled(true);
      mrecycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
      final StaggeredGridLayoutManager mlayout = new StaggeredGridLayoutManager(2,LinearLayout.VERTICAL);
      mrecycler.setLayoutManager(mlayout);

        final ProgressBar progressBar = v.findViewById(R.id.progress_bar);

        mrecycler.setSaveEnabled(true);

        mdef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot i : dataSnapshot.getChildren()) {

                    if(!Urls.contains(i.child("ImageUrl").getValue().toString().trim()))
                    {
                        Urls.add(i.child("ImageUrl").getValue().toString().trim());
                        names.add(i.child("Name").getValue().toString().trim());
                        orientation.add(i.child("orientation").getValue().toString().trim());
                    }
                }
                progressBar.setVisibility(View.GONE);

                adapter = new adpterforcategory(v.getContext(),names,Urls , orientation , pagesize);
                adapter.notifyDataSetChanged();

                mrecycler.setAdapter(adapter);
                dialog.dismiss();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(v.getContext(), "Nhi Chla chutiye", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        return v;

        }

}