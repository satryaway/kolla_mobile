package com.jixstreet.kolla;

import android.content.Context;

import com.jixstreet.kolla.booking.BookingCategory;
import com.jixstreet.kolla.intro.IntroBackgroundItem;
import com.jixstreet.kolla.news.NewsHeaderItem;

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

    public static List<NewsHeaderItem> getNewsHeaderList() {
        List<NewsHeaderItem> newsHeaderItems = new ArrayList<>();
        newsHeaderItems.add(new NewsHeaderItem("Coworking Space Nyaman di Jakarta", "23 January 2016", R.drawable.dummy_bg));
        newsHeaderItems.add(new NewsHeaderItem("Event Teknologi Seru Setiap Bulan", "23 January 2016", R.drawable.dummy_bg));
        newsHeaderItems.add(new NewsHeaderItem("Update Berita Terbaru Seputar Teknologi", "23 January 2016", R.drawable.dummy_bg));
        newsHeaderItems.add(new NewsHeaderItem("Dapatkan Koneksi Baru dan Kawan Baru", "23 January 2016", R.drawable.dummy_bg));

        return newsHeaderItems;
    }

    public static List<BookingCategory> getBookingCategories(Context context) {
        List<BookingCategory> bookingCategories = new ArrayList<>();
        bookingCategories.add(new BookingCategory((R.drawable.dummy_bg), "Hot Desk"));
        bookingCategories.add(new BookingCategory((R.drawable.dummy_bg), "Cold Desk"));
        bookingCategories.add(new BookingCategory((R.drawable.dummy_bg), "Warm Desk"));
        bookingCategories.add(new BookingCategory((R.drawable.dummy_bg), "Normal Desk"));
        return bookingCategories;
    }
}
