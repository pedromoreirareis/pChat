package com.pedromoreirareisgmail.pchat.Utils;

import android.content.Context;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PicassoDownload {


    public static void civChachePlaceholder(final Context context, final String urlImagem, final int drawable, final CircleImageView civImagem) {

        Picasso.with(context)
                .load(urlImagem)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(context.getDrawable(drawable))
                .into(civImagem, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                        Picasso.with(context)
                                .load(urlImagem)
                                .placeholder(context.getDrawable(drawable))
                                .into(civImagem);
                    }
                });

    }
}
