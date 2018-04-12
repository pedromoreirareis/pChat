package com.pedromoreirareisgmail.pchat.utils;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PicassoDownload {


    public static void civChachePlaceholder(final Context context, final String urlImagem, final int drawable, final CircleImageView civImagem) {

        Picasso.with(context)
                .load(urlImagem)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(context.getResources().getDrawable(drawable))
                .into(civImagem, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {

                        Picasso.with(context)
                                .load(urlImagem)
                                .placeholder(context.getResources().getDrawable(drawable))
                                .into(civImagem);
                    }
                });

    }




    public static void ivChache(final Context context, final String urlImagem, final ImageView ivImagem) {

        Picasso.with(context)
                .load(urlImagem)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(ivImagem, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {

                        Picasso.with(context)
                                .load(urlImagem)
                                .into(ivImagem);
                    }
                });

    }
}
