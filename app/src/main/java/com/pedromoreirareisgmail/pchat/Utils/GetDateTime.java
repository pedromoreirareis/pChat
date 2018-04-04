package com.pedromoreirareisgmail.pchat.Utils;

import android.app.Application;
import android.content.Context;

import com.pedromoreirareisgmail.pchat.R;

public class GetDateTime extends Application {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 60 * HOUR_MILLIS;

        public static String getTimeAgo(long time, Context context){

            if(time < 1000000000000L){

                time *= 1000;
            }

            long now = System.currentTimeMillis();
            if(time > now || time <= 0){

                return null;
            }

            final long diff = now - time;

            if(diff < MINUTE_MILLIS){

                return context.getString(R.string.time_agora_pouco);

            }else if( diff < 2 * MINUTE_MILLIS){

                return context.getString(R.string.time_um_minuto);

            }else if(diff < 59 * MINUTE_MILLIS){

                int minutos = (int) diff/MINUTE_MILLIS;

                return String.format(context.getString(R.string.time_minutos_atras),String.valueOf(minutos));


            }else if(diff < 90 * MINUTE_MILLIS){

                return context.getString(R.string.time_uma_hora_atras);

            }else if(diff < 24 * HOUR_MILLIS){

                int horas = (int) diff/HOUR_MILLIS;

                return String.format(context.getString(R.string.time_horas_atras),String.valueOf(horas));

            }else if(diff < 48 * HOUR_MILLIS){

                return context.getString(R.string.time_ontem);

            }else{

                int dias = (int) diff/DAY_MILLIS;

                return String.format(context.getString(R.string.time_dias_atras),String.valueOf(dias));
            }


        }
}