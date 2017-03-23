package com.jixstreet.kolla.booking.room.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

import com.jixstreet.kolla.R;
import com.jixstreet.kolla.Seeder;
import com.jixstreet.kolla.news.NewsHeaderItem;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by satryaway on 2/22/2017.
 * satryaway@gmail.com
 */

@EFragment(R.layout.fragment_room_detail_header_item)
public class RoomDetailHeaderItemFragment extends Fragment {

    @ViewById(R.id.room_detail_header_item_iv)
    protected ImageView roomDetailHeaderItemIv;

    @ViewById(R.id.room_detail_header_item_title_tv)
    protected TextView roomDetailHeaderTitleTv;

    @ViewById(R.id.room_detail_header_item_date_tv)
    protected TextView roomDetailHeaderDateTv;

    private static final String POSITION = "position";

    public static RoomDetailHeaderItemFragment newInstance(int position) {
        RoomDetailHeaderItemFragment fragment = new RoomDetailHeaderItemFragment_();
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION, position);
        fragment.setArguments(bundle);

        return fragment;
    }

    @AfterViews
    void OnViewsCreated() {
        int position = getArguments().getInt(POSITION);
        RoomDetailHeaderItem newsHeaderItem = Seeder.getRoomDetailHeaderList().get(position);
        roomDetailHeaderItemIv.setImageResource(newsHeaderItem.image);
        roomDetailHeaderTitleTv.setText(newsHeaderItem.title);
        roomDetailHeaderDateTv.setText(newsHeaderItem.description);
    }
}
