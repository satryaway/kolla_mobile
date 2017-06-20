package com.jixstreet.kolla.profile.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.jixstreet.kolla.CommonConstant;
import com.jixstreet.kolla.R;
import com.jixstreet.kolla.model.UserData;
import com.jixstreet.kolla.utility.CastUtils;
import com.jixstreet.kolla.utility.DialogUtils;
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by satryaway on 6/20/2017.
 * satryaway@gmail.com
 */

@EFragment(R.layout.fragment_profile_detail)
public class ProfileDetailFragment extends Fragment {

    @ViewById(R.id.email_tv)
    protected TextView emailTv;

    @ViewById(R.id.phone_tv)
    protected TextView phoneTv;

    @ViewById(R.id.credit_count_tv)
    protected TextView creditCountTv;

    @ViewById(R.id.company_tv)
    protected TextView companyTv;

    private static final String PROFILE = "profile";
    private UserData userData;
    private GetProfileJson getProfileJson;

    public static ProfileDetailFragment newInstance(UserData userData) {
        Bundle args = new Bundle();
        args.putString(PROFILE, CastUtils.toString(userData));
        ProfileDetailFragment fragment = new ProfileDetailFragment_();
        fragment.setArguments(args);
        return fragment;
    }

    @AfterViews
    protected void onViewsCreated() {
        userData = CastUtils.fromString(getArguments().getString(PROFILE), UserData.class);
        if (userData != null) {
            setValue();
            getProfileJson = new GetProfileJson(getActivity(), userData.id);
            getProfile();
        }
    }

    private void setValue() {
        ViewUtils.setTextView(emailTv, userData.email);
        ViewUtils.setTextView(phoneTv, userData.phone_no);
        ViewUtils.setTextView(companyTv, userData.company);
    }

    private void setCredit() {
        ViewUtils.setTextView(creditCountTv, userData.credit.main_credit);
    }

    private void getProfile() {
        getProfileJson.get(new OnGetProfile() {
            @Override
            public void onSuccess(GetProfileJson.Response response) {
                userData = response.data;
                setValue();
                setCredit();
            }

            @Override
            public void onFailure(String message) {
                DialogUtils.makeSnackBar(CommonConstant.failed, getActivity(), message);
            }
        });
    }
}
