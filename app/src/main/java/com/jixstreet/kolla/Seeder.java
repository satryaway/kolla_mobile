package com.jixstreet.kolla;

import com.jixstreet.kolla.login.LoginBackground;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by satryaway on 2/15/2017.
 * satryaway@gmail.com
 */

public class Seeder {

    public static List<LoginBackground> getLoginBackgroundList() {
        List<LoginBackground> loginBackgrounds = new ArrayList<>();
        loginBackgrounds.add(new LoginBackground("Coworking Space Nyaman di Jakarta", R.drawable.dummy_bg));
        loginBackgrounds.add(new LoginBackground("Event Teknologi Seru Setiap Bulan", R.drawable.dummy_bg));
        loginBackgrounds.add(new LoginBackground("Update Berita Terbaru Seputar Teknologi", R.drawable.dummy_bg));
        loginBackgrounds.add(new LoginBackground("Dapatkan Koneksi Baru dan Kawan Baru", R.drawable.dummy_bg));

        return loginBackgrounds;
    }
}
