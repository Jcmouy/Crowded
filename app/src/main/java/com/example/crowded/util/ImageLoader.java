package com.example.crowded.util;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

public class ImageLoader {

    public void loadImage(Context context, String url, ImageView imageView) {
            Glide.with(context)
                .load(url)
                .fitCenter()
                .transition(DrawableTransitionOptions.withCrossFade())
                .transform(new MultiTransformation(new CenterCrop(), new RoundedCorners(1)))
                .into(imageView);
    }

    public void loadImage(Context context, File file, ImageView imageView) {
        Glide.with(context)
                .load(file)
                .fitCenter()
                .transition(DrawableTransitionOptions.withCrossFade())
                .transform(new MultiTransformation(new CenterCrop(), new RoundedCorners(1)))
                .into(imageView);
    }

    public void loadImage(Context context, Uri uri, ImageView imageView) {
        Glide.with(context)
                .load(uri)
                .fitCenter()
                .transition(DrawableTransitionOptions.withCrossFade())
                .transform(new MultiTransformation(new CenterCrop(), new RoundedCorners(1)))
                .into(imageView);
    }

    public void loadImage(Context context, Integer resId, ImageView imageView) {
        Glide.with(context)
                .load(resId)
                .fitCenter()
                .transition(DrawableTransitionOptions.withCrossFade())
                .transform(new MultiTransformation(new CenterCrop(), new RoundedCorners(1)))
                .into(imageView);
    }

    public void loadImageWithoutTransform(Context context, Uri uri, ImageView imageView) {
        Glide.with(context)
                .load(uri)
                .into(imageView);
    }

    public void loadImageWithCircularCrop(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .apply(RequestOptions.circleCropTransform())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }

    public void removeCache(Context context){
        Glide.get(context).clearMemory();
    }
}
