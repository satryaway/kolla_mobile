package com.jixstreet.kolla.booking.room.detail.facility;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.utility.ImageUtils;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by satryaway on 3/23/2017.
 * satryaway@gmail.com
 */

@EViewGroup(R.layout.item_facility)
public class RoomFacilityItemView extends RelativeLayout {
    private final Context context;

    @ViewById(R.id.facility_icon_iv)
    protected ImageView facilityIconIv;

    @ViewById(R.id.facility_name_iv)
    protected TextView facilityNameIv;

    private RoomFacility roomFacility;

    public RoomFacilityItemView(Context context) {
        super(context);
        this.context = context;
    }

    public void setRoomFacility(RoomFacility roomFacility) {
        this.roomFacility = roomFacility;
        setValue();
    }

    private void setValue() {
        ImageUtils.loadLocalImage(context, facilityIconIv, roomFacility.icon);
        facilityNameIv.setText(roomFacility.name);
    }
}
