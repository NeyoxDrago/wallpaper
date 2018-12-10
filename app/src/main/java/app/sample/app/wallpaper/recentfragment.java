package app.sample.app.wallpaper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.ArrayList;
import java.util.List;

import app.sample.app.wallpaper.DatabaseFiles.LocalDATABASE;
import app.sample.app.wallpaper.DatabaseFiles.datasource;
import app.sample.app.wallpaper.DatabaseFiles.repository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class recentfragment extends Fragment {

    public static recentfragment INSTANCE = null;
    public List<recents> Recent = new ArrayList<>();
    CompositeDisposable compositeDisposable;
    repository Repository;
    Context c;
    recerntAdapter adapter;

    public recentfragment(){}

    @SuppressLint("ValidFragment")
    public recentfragment(Context c) {
        // Required empty public constructor

        this.c = c;

        adapter= new recerntAdapter(c , Recent);
        adapter.notifyDataSetChanged();

        compositeDisposable = new CompositeDisposable();
        LocalDATABASE d = LocalDATABASE.getInstance(c);
        Repository = (repository) repository.getInstance(datasource.getInstance(d.Dao()));

        loadRecents();
    }

    public void loadRecents() {
        Disposable disposable = Repository.getAllRecents()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<recents>>() {
                    @Override
                    public void accept(List<recents> recents) throws Exception {
                        ongetALLRecents(recents);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(c, "Error Received", Toast.LENGTH_SHORT).show();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                    }
                });

        compositeDisposable.add(disposable);
    }

    public void ongetALLRecents(List<recents> recents) {
        Recent.clear();
        Recent.addAll(recents);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public static recentfragment getinstance(Context c)
    {
        if(INSTANCE==null)
            INSTANCE = new recentfragment(c);

        return INSTANCE;

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_recentfragment, container, false);



        final RecyclerView mrecycler = v.findViewById(R.id.recentrecycler);
        mrecycler.setHasFixedSize(true);
        mrecycler.setLayoutManager(new GridLayoutManager( c,2));
        adapter = new recerntAdapter(v.getContext() , Recent);
        mrecycler.setAdapter(adapter);

        final SwipeRefreshLayout refreshLayout = v.findViewById(R.id.refreshlayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                INSTANCE.loadRecents();
                mrecycler.setAdapter(adapter);
                refreshLayout.setRefreshing(false);
            }
        });

        return v;
    }

}
