package com.jixstreet.kolla.booking.room.detail.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jixstreet.kolla.R;
import com.jixstreet.kolla.booking.room.Room;
import com.jixstreet.kolla.event.EventDetailActivity;
import com.jixstreet.kolla.utility.CastUtils;
import com.jixstreet.kolla.utility.DialogUtils;
import com.jixstreet.kolla.utility.PermissionUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by satryaway on 3/23/2017.
 * satryaway@gmail.com
 */

@EFragment(R.layout.fragment_room_map)
public class RoomMapFragment extends Fragment {

    private static final String ROOM_MAP = "Room Map";
    @ViewById(R.id.map_view)
    MapView mapView;

    private Bundle bundle;
    private Room room;
    private LatLng latlng;

    public static RoomMapFragment newInstance(Room room) {
        Bundle args = new Bundle();
        args.putString(ROOM_MAP, CastUtils.toString(room));

        RoomMapFragment fragment = new RoomMapFragment_();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        bundle = savedInstanceState;

        return null;
    }

    @AfterViews
    protected void onViewsCreated() {
        room = CastUtils.fromString(getArguments().getString(ROOM_MAP, ""), Room.class);
        if (room != null)
            setValue(room);
    }

    public void setValue(Room room) {
        this.room = room;
        if (getContext() != null && room != null) {
            latlng = getLatLng();

            if (latlng != null)
                initMap();
        }
    }

    private LatLng getLatLng() {
        if (room.map != null) {
            String[] latLngArray = room.map.split(",");
            if (latLngArray[0] != null && latLngArray[1] != null) {
                Double lat = Double.valueOf(latLngArray[0]);
                Double lng = Double.valueOf(latLngArray[1]);

                return new LatLng(lat, lng);
            } else {
                return null;
            }
        } else return null;
    }

    private void initMap() {
        mapView.onCreate(bundle);
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

            if (ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                PermissionUtils.requestPermissions(getActivity(), PermissionUtils.PERMISSIONS_LOCATION,
                        PermissionUtils.REQUEST_LOCATION, "Access Location");

                return;
            }

            googleMap.setMyLocationEnabled(false);
            googleMap.addMarker(new MarkerOptions()
                    .position(latlng)
                    .title("Marker in Sydney")
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 12.0f));
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionUtils.REQUEST_LOCATION: {
                if (grantResults.length < 1
                        && grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    DialogUtils.makeToast(getContext(), "Permission denied");
                    getActivity().finish();
                } {
                    mapView.getMapAsync(onMapReadyCallBack);
                }
            }
        }
    }

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
