package com.jixstreet.kolla;

import com.jixstreet.kolla.login.IntroBackground;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by satryaway on 2/15/2017.
 * satryaway@gmail.com
 */

public class Seeder {

    public static List<IntroBackground> getLoginBackgroundList() {
        List<IntroBackground> loginBackgrounds = new ArrayList<>();
        loginBackgrounds.add(new IntroBackground("Coworking Space Nyaman di Jakarta", R.drawable.dummy_bg));
        loginBackgrounds.add(new IntroBackground("Event Teknologi Seru Setiap Bulan", R.drawable.dummy_bg));
        loginBackgrounds.add(new IntroBackground("Update Berita Terbaru Seputar Teknologi", R.drawable.dummy_bg));
        loginBackgrounds.add(new IntroBackground("Dapatkan Koneksi Baru dan Kawan Baru", R.drawable.dummy_bg));

        return loginBackgrounds;
    }
}
