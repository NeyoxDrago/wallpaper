package app.sample.app.wallpaper;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class adapterforcategories extends RecyclerView.Adapter<adapterforcategories.ViewHolder>{

    private Context c;
    private List<String> urls = new ArrayList<>();
    private List<String> names = new ArrayList<>();

    public adapterforcategories(Context c, List<String> urls, List<String> names) {
        this.c = c;
        this.urls = urls;
        this.names = names;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(c).inflate(R.layout.categoryitem , viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        RequestOptions options = new RequestOptions().placeholder(R.drawable.load);

        Glide.with(c).load(urls.get(i))
                .apply(options)
                .into(viewHolder.image);

        viewHolder.name.setText(names.get(i));

    }

    @Override
    public int getItemCount() {
        return urls.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.categoryitemimage);
            name = itemView.findViewById(R.id.categoryitemtext);



        }
    }

}
