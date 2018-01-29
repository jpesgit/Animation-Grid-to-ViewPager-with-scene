package me.mingdroid.setransition;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by huang on 3/14/17.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
    private ArrayList<Uri> urisList;
    private SourceActivity.OnSharedViewListener sharedViewListener;
    private ImageView[] photoViews;
    private Integer count = 0;
    public RecyclerAdapter(ArrayList<Uri> uriList, SourceActivity.OnSharedViewListener sharedViewListener) {
        this.urisList = uriList;
        this.sharedViewListener = sharedViewListener;
        photoViews = new ImageView[uriList.size()];

    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_photo_view, parent, false);

            return new RecyclerViewHolder(view, sharedViewListener);

    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.bindView(urisList.get(position));
    }

    @Override
    public int getItemCount() {
        return urisList.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SourceActivity.OnSharedViewListener sharedViewListener;
        private Context context;
        private ImageView imageView;



        private ArrayList<Uri> uris;

        @SuppressLint("ResourceType")
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public RecyclerViewHolder(View itemView, SourceActivity.OnSharedViewListener sharedViewListener) {
            super(itemView);
            this.sharedViewListener = sharedViewListener;
            context = itemView.getContext();
            imageView = (ImageView) itemView.findViewById(R.id.image);
            imageView.setTag(count);
            imageView.setOnClickListener(this);
            photoViews[count] = imageView;

            count++;
        }

        public void bindView(Uri uri) {
            imageView.setImageURI(uri);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                imageView.setTransitionName(
                        context.getString(R.string.transition_name, getAdapterPosition(),(int) imageView.getTag()));
            }

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, DestinationActivity.class);
            intent.putParcelableArrayListExtra("uris", urisList);
            intent.putExtra("adapter_position", getAdapterPosition());
            intent.putExtra("current", (int) v.getTag());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((AppCompatActivity) context, v, v.getTransitionName());
                ((AppCompatActivity) context).startActivityForResult(intent, 0, options.toBundle());
                sharedViewListener.onSharedViewListener(photoViews, (int) v.getTag());
            } else {
                context.startActivity(intent);
            }
        }

    }

}
