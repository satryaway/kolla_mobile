package com.jixstreet.kolla.booking.room;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jixstreet.kolla.R;
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

    @ViewById(R.id.warning_iv)
    protected ImageView warningIv;

    private OnRoomSelected onRoomSelected;

    private Room room;
    private Context context;

    public RoomView(Context context) {
        super(context);
        this.context = context;
    }

    public RoomView(Context context, AttributeSet attrs) {
        super(context, attrs);
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
        if (room.size.equals("0")) {
            room.isOnlyView = true;
            fullBookedStateTv.setVisibility(VISIBLE);
            seatCountTv.setVisibility(GONE);
        } else {
            fullBookedStateTv.setVisibility(GONE);
            seatCountTv.setVisibility(VISIBLE);
        }
    }

    public void setColorCount(String expectedSize) {
        int expectedSizeCount = Integer.valueOf(expectedSize);
        int availableSizeCount = Integer.valueOf(room.size);
        warningIv.setVisibility(expectedSizeCount < availableSizeCount ? GONE : VISIBLE);

    }

    public void setOnRoomSelected(OnRoomSelected onRoomSelected) {
        this.onRoomSelected = onRoomSelected;
    }

    @Click(R.id.main_wrapper)
    void openDetail() {
        if (room != null)
            this.onRoomSelected.onSelect(room);
    }

    public void setIsOnlyView(boolean isOnlyView) {
        if (room != null)
            room.isOnlyView = isOnlyView;
    }
}
