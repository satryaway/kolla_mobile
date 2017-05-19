package com.jixstreet.kolla.Friend;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.utility.ImageUtils;
import com.jixstreet.kolla.utility.ViewUtils;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by satryaway on 5/19/2017.
 * satryaway@gmail.com
 */

@EViewGroup(R.layout.item_friend_thumb)
public class FriendThumbView extends RelativeLayout {

    private final Context context;
    private Friend friend;

    @ViewById(R.id.friend_image_iv)
    protected ImageView friendImageIv;

    @ViewById(R.id.friend_name_tv)
    protected TextView friendNameTv;

    private OnThumbClickListener onThumbClickListener;

    public FriendThumbView(Context context) {
        super(context);
        this.context = context;
    }

    public void setFriend(Friend friend) {
        this.friend = friend;
//        setValue();
        setDummy();
    }

    private void setDummy() {
        ViewUtils.setTextView(friendNameTv, "");
        ImageUtils.loadImageRoundCornerNoScale(context, "", friendImageIv);
    }

    private void setValue() {
        if (friend == null) return;

        ViewUtils.setTextView(friendNameTv, friend.name);
        ImageUtils.loadImageRoundCornerNoScale(context, friend.imageUrl, friendImageIv);
    }

    public interface OnThumbClickListener {
        void onClick(Friend friend);
    }

    public void setOnThumbClickListener(OnThumbClickListener onThumbClickListener) {
        this.onThumbClickListener = onThumbClickListener;
    }

    @Click(R.id.content_wrapper)
    protected void selectItem() {
        this.onThumbClickListener.onClick(friend);
    }
}
