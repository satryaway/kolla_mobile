package com.jixstreet.kolla.user;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.model.UserData;
import com.jixstreet.kolla.utility.ImageUtils;
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by satryaway on 6/21/2017.
 * satryaway@gmail.com
 */

@EViewGroup(R.layout.item_user_list)
public class UserListView extends RelativeLayout {

    @ViewById(R.id.user_image_iv)
    protected ImageView userImageIv;

    @ViewById(R.id.user_name_tv)
    protected TextView userNameTv;

    @ViewById(R.id.user_job_tv)
    protected TextView userJobTv;

    @ViewById(R.id.user_company_tv)
    protected TextView userCompanyTv;

    private UserData userData;
    private Context context;
    private OnUserSelectedListener onUserSelectedListener;

    public UserListView(Context context) {
        super(context);
        this.context = context;
    }

    public UserListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
        setValue();
    }

    private void setValue() {
        ImageUtils.loadImageRound(context, userData.profile_picture, userImageIv);
        ViewUtils.setTextView(userNameTv, userData.name);
        ViewUtils.setTextView(userJobTv, userData.job_title);
        ViewUtils.setTextView(userCompanyTv, userData.company);
    }

    public void setOnUserSelectedListener(OnUserSelectedListener onUserSelectedListener) {
        this.onUserSelectedListener = onUserSelectedListener;
    }

    @Click(R.id.content_wrapper)
    protected void onUserSelected() {
        onUserSelectedListener.onClick(userData);
    }
}
