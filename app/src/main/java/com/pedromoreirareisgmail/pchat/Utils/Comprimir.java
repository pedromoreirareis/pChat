package com.pedromoreirareisgmail.pchat.Utils;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;

public class Comprimir {

    private static Bitmap mBitmapCompressor = null;

    public static byte[] comprimirImagem(Context context, File filePath, int maxLargura, int maxAltura, int minQualidade) {

        try {

            mBitmapCompressor = new Compressor(context)
                    .setMaxWidth(maxLargura)
                    .setMaxHeight(maxAltura)
                    .setQuality(minQualidade)
                    .compressToBitmap(filePath);

        } catch (IOException e) {

            e.printStackTrace();
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        mBitmapCompressor.compress(Bitmap.CompressFormat.JPEG, minQualidade, byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] comprimirImagemNoLA(Context context, File filePath, int minQualidade) {

        try {

            mBitmapCompressor = new Compressor(context)
                    .setQuality(minQualidade)
                    .compressToBitmap(filePath);

        } catch (IOException e) {

            e.printStackTrace();
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        mBitmapCompressor.compress(Bitmap.CompressFormat.JPEG, minQualidade, byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }
}