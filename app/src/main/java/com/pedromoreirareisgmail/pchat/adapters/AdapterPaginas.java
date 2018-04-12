package com.pedromoreirareisgmail.pchat.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.pedromoreirareisgmail.pchat.R;
import com.pedromoreirareisgmail.pchat.fragments.ChatsFragment;
import com.pedromoreirareisgmail.pchat.fragments.FriendsFragment;
import com.pedromoreirareisgmail.pchat.fragments.RequestsFragment;

public class AdapterPaginas extends FragmentPagerAdapter {

    private Context mContext;

    public AdapterPaginas(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {

            case 0:
                return mContext.getString(R.string.titulo_frag_requests);

            case 1:
                return mContext.getString(R.string.titulo_frag_chats);

            case 2:
                return mContext.getString(R.string.titulo_frag_friends);

            default:
                return null;
        }
    }


    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:
                return new RequestsFragment();

            case 1:
                return new ChatsFragment();

            case 2:
                return new FriendsFragment();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}