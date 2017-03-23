package com.jixstreet.kolla.booking.room.detail.map;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jixstreet.kolla.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by satryaway on 3/23/2017.
 * satryaway@gmail.com
 */

@EFragment(R.layout.fragment_room_map)
public class RoomMapFragment extends Fragment {
    @ViewById(R.id.map_view)
    MapView mapView;

    private GoogleMap map;

    public static RoomMapFragment newInstance() {
        Bundle args = new Bundle();
        RoomMapFragment fragment = new RoomMapFragment_();
        fragment.setArguments(args);

        return fragment;
    }

    @AfterViews
    protected void onViewsCreated() {
        mapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(onMapReadyCallBack);
    }

    private OnMapReadyCallback onMapReadyCallBack = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;

            LatLng sydney = new LatLng(-34, 151);
            map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            map.animateCamera(CameraUpdateFactory.newLatLng(sydney));
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }
}
