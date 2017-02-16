package com.jixstreet.kolla.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.Seeder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by satryaway on 2/15/2017.
 * satryaway@gmail.com
 */

@EFragment(R.layout.fragment_login_background)
public class IntroBackgroundFragment extends Fragment {

    @ViewById(R.id.background_iv)
    protected ImageView backgroundIv;

    @ViewById(R.id.background_text_tv)
    protected TextView backgroundTextTv;

    private static final String POSITION = "position";

    public static IntroBackgroundFragment newInstance(int position) {
        IntroBackgroundFragment fragment = new IntroBackgroundFragment_();
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION, position);
        fragment.setArguments(bundle);

        return fragment;
    }

    @AfterViews
    void OnViewsCreated() {
        int position = getArguments().getInt(POSITION);
        IntroBackgroundItem loginBackground = Seeder.getLoginBackgroundList().get(position);
        backgroundIv.setImageResource(loginBackground.image);
        backgroundTextTv.setText(loginBackground.text);
    }
}
