package com.jixstreet.kolla.booking.room;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.booking.room.detail.RoomDetailActivity_;
import com.jixstreet.kolla.utility.ActivityUtils;
import com.jixstreet.kolla.utility.ImageUtils;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by satryaway on 3/7/2017.
 * satryaway@gmail.com
 */

@EViewGroup(R.layout.item_room)
public class RoomView extends RelativeLayout {
    @ViewById(R.id.room_image_iv)
    protected ImageView roomImageIv;

    @ViewById(R.id.seat_count_tv)
    protected TextView seatCountTv;

    @ViewById(R.id.room_title_tv)
    protected TextView roomTitleTv;

    @ViewById(R.id.room_description_tv)
    protected TextView roomDescriptionTv;

    @ViewById(R.id.full_booked_state_tv)
    protected TextView fullBookedStateTv;

    private Room room;
    private Context context;

    public RoomView(Context context) {
        super(context);
        this.context = context;
    }

    public void setRoom(Room room) {
        this.room = room;
        setValue();
    }

    private void setValue() {
        ImageUtils.loadImageWithPlaceHolder(getContext(), room.imageUrl, roomImageIv, R.drawable.dummy_bg);
        seatCountTv.setText(getContext().getString(R.string.seat_available, room.size, room.measurement));
        roomDescriptionTv.setText(room.description);
        roomTitleTv.setText(room.name);
        if (room.isFullBooked) {
            fullBookedStateTv.setVisibility(VISIBLE);
            seatCountTv.setVisibility(GONE);
        } else {
            fullBookedStateTv.setVisibility(GONE);
            seatCountTv.setVisibility(VISIBLE);
        }
    }

    @Click(R.id.main_wrapper)
    void openDetail() {
        ActivityUtils.startActivity((Activity)context, RoomDetailActivity_.class);
    }
}
