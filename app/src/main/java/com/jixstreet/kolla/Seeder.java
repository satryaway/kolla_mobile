package com.jixstreet.kolla;

import com.jixstreet.kolla.login.IntroBackgroundItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by satryaway on 2/15/2017.
 * satryaway@gmail.com
 */

public class Seeder {

    public static List<IntroBackgroundItem> getLoginBackgroundList() {
        List<IntroBackgroundItem> loginBackgrounds = new ArrayList<>();
        loginBackgrounds.add(new IntroBackgroundItem("Coworking Space Nyaman di Jakarta", R.drawable.dummy_bg));
        loginBackgrounds.add(new IntroBackgroundItem("Event Teknologi Seru Setiap Bulan", R.drawable.dummy_bg));
        loginBackgrounds.add(new IntroBackgroundItem("Update Berita Terbaru Seputar Teknologi", R.drawable.dummy_bg));
        loginBackgrounds.add(new IntroBackgroundItem("Dapatkan Koneksi Baru dan Kawan Baru", R.drawable.dummy_bg));

        return loginBackgrounds;
    }
}
