package me.mingdroid.setransition;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SourceActivity extends AppCompatActivity {
    private int exitPosition;
    private int enterPosition;
    private View[] sharedViews;

    private OnSharedViewListener sharedViewListener = new OnSharedViewListener() {
        @Override
        public void onSharedViewListener(View[] views, int enterPosition) {
            sharedViews = views;
            setCallback(enterPosition);
        }


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new RecyclerAdapter(getPhotos(), sharedViewListener));

    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            exitPosition = data.getIntExtra("exit_position", enterPosition);
        }
    }

    @TargetApi(21)
    private void setCallback(final int enterPosition) {
        this.enterPosition = enterPosition;
        setExitSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                if (exitPosition != enterPosition &&
                        names.size() > 0 && exitPosition < sharedViews.length) {
                    names.clear();
                    sharedElements.clear();
                    View view = sharedViews[exitPosition];
                    names.add(view.getTransitionName());
                    sharedElements.put(view.getTransitionName(), view);
                }
                setExitSharedElementCallback((SharedElementCallback) null);
                sharedViews = null;
            }
        });
    }

    public interface OnSharedViewListener {
        void onSharedViewListener(View[] views, int enterPosition);
    }

    //Test data
    private ArrayList<Uri>getPhotos() {
        ArrayList<Uri> data = new ArrayList<>();
        data.add(buildUri(R.mipmap.img0));
        data.add(buildUri(R.mipmap.img1));
        data.add(buildUri(R.mipmap.img2));
        data.add(buildUri(R.mipmap.img3));
        data.add(buildUri(R.mipmap.img4));
        data.add(buildUri(R.mipmap.img5));
        data.add(buildUri(R.mipmap.img0));
        data.add(buildUri(R.mipmap.img1));
        data.add(buildUri(R.mipmap.img2));
        data.add(buildUri(R.mipmap.img3));

        return data;
    }

    private Uri buildUri(@DrawableRes int resId) {
        return Uri.parse("android.resource://me.mingdroid.setransition/" + resId);
    }

}
