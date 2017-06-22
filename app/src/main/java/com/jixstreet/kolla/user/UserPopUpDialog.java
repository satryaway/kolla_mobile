package com.jixstreet.kolla.user;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.dialog.PopUpDialog;
import com.jixstreet.kolla.login.LoginJson;
import com.jixstreet.kolla.model.UserData;
import com.jixstreet.kolla.utility.ImageUtils;
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by satryaway on 6/22/2017.
 * satryaway@gmail.com
 */

@EViewGroup(R.layout.dialog_user_profile_popup)
public class UserPopUpDialog extends LinearLayout implements PopUpDialog.Inflateable {

    @ViewById(R.id.profile_image_iv)
    protected ImageView profileImageIv;

    @ViewById(R.id.name_tv)
    protected TextView nameTv;

    @ViewById(R.id.job_tv)
    protected TextView jobTv;

    @ViewById(R.id.company_tv)
    protected TextView companyTv;

    @ViewById(R.id.follow_tv)
    protected TextView followTv;

    private final Context context;
    private UserData userData;
    private OnUserPopupClickListener onUserPopupClickListener;

    public UserPopUpDialog(Context context) {
        super(context);
        this.context = context;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
        setValue();
    }

    private void setValue() {
        ImageUtils.loadImageRound(context, userData.profile_picture, profileImageIv);
        ViewUtils.setTextView(nameTv, userData.name);
        ViewUtils.setTextView(jobTv, userData.job_title);
        ViewUtils.setTextView(companyTv, userData.company);
        ViewUtils.setTextView(followTv, context.getString(userData.is_followed ? R.string.unfollow : R.string.follow));
    }

    @Click(R.id.view_profile_tv)
    protected void viewProfile() {
        onUserPopupClickListener.onViewProfile(userData);
    }

    @Click(R.id.follow_tv)
    protected void followUser() {
        onUserPopupClickListener.onFollow(userData);
    }

    public void setOnUserPopupClickListener(OnUserPopupClickListener onUserPopupClickListener) {
        this.onUserPopupClickListener = onUserPopupClickListener;
    }

    @Override
    public void finishInflate() {
        onFinishInflate();
    }

    public interface OnUserPopupClickListener {
        void onViewProfile(UserData userData);

        void onFollow(UserData userData);
    }
}
