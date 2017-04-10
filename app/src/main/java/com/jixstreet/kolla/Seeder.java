package com.jixstreet.kolla;

import android.content.Context;

import com.jixstreet.kolla.booking.category.BookingCategory;
import com.jixstreet.kolla.booking.room.Room;
import com.jixstreet.kolla.booking.room.RoomHeader;
import com.jixstreet.kolla.booking.room.detail.RoomDetailHeaderItem;
import com.jixstreet.kolla.booking.room.detail.facility.RoomFacility;
import com.jixstreet.kolla.booking.room.payment.Banking;
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
        bookingCategories.add(new BookingCategory());
        return bookingCategories;
    }

    public static RoomHeader getRoomHeader() {
        return new RoomHeader("Hot Desk", "Suitable for individuals who needs a flexible work space");
    }

    public static ArrayList<Room> getRoomList() {
        ArrayList<Room> rooms = new ArrayList<>();
        rooms.add(new Room("xx", "Kolla Space - Sabang", "7 Eleven Sabang, 2nd Floor. KH. Agus Salim 32B, Sabang", "22 Seats", true));
        rooms.add(new Room("xx", "Kolla Space - Sabang", "7 Eleven Sabang, 2nd Floor. KH. Agus Salim 32B, Sabang", "22 Seats", false));
        rooms.add(new Room("xx", "Kolla Space - Sabang", "7 Eleven Sabang, 2nd Floor. KH. Agus Salim 32B, Sabang", "22 Seats", true));
        rooms.add(new Room("xx", "Kolla Space - Sabang", "7 Eleven Sabang, 2nd Floor. KH. Agus Salim 32B, Sabang", "22 Seats", true));
        rooms.add(new Room("xx", "Kolla Space - Sabang", "7 Eleven Sabang, 2nd Floor. KH. Agus Salim 32B, Sabang", "22 Seats", true));
        rooms.add(new Room("xx", "Kolla Space - Sabang", "7 Eleven Sabang, 2nd Floor. KH. Agus Salim 32B, Sabang", "22 Seats", false));
        rooms.add(new Room("xx", "Kolla Space - Sabang", "7 Eleven Sabang, 2nd Floor. KH. Agus Salim 32B, Sabang", "22 Seats", true));
        rooms.add(new Room("xx", "Kolla Space - Sabang", "7 Eleven Sabang, 2nd Floor. KH. Agus Salim 32B, Sabang", "22 Seats", true));
        rooms.add(new Room("xx", "Kolla Space - Sabang", "7 Eleven Sabang, 2nd Floor. KH. Agus Salim 32B, Sabang", "22 Seats", true));
        rooms.add(new Room("xx", "Kolla Space - Sabang", "7 Eleven Sabang, 2nd Floor. KH. Agus Salim 32B, Sabang", "22 Seats", false));
        rooms.add(new Room("xx", "Kolla Space - Sabang", "7 Eleven Sabang, 2nd Floor. KH. Agus Salim 32B, Sabang", "22 Seats", true));
        rooms.add(new Room("xx", "Kolla Space - Sabang", "7 Eleven Sabang, 2nd Floor. KH. Agus Salim 32B, Sabang", "22 Seats", true));
        return rooms;
    }

    public static List<String> getLocations() {
        List<String> locations = new ArrayList<>();
        locations.add("Jakarta");
        locations.add("Bandung");
        locations.add("Tangerang");
        locations.add("Bogor");
        locations.add("Bekasi");

        return locations;
    }

    public static List<String> getDurations(int count) {
        List<String> durationList = new ArrayList<>();
        for (int i = 1; i < count + 1; i++) {
            durationList.add(i + (i == 1 ? " hour" : " hours"));
        }

        return durationList;
    }

    public static List<String> getGuests(int count) {
        List<String> guestList = new ArrayList<>();
        for (int i = 1; i < count + 1; i++) {
            guestList.add(i + (i == 1 ? " person" : " persons"));
        }

        return guestList;
    }

    public static List<RoomDetailHeaderItem> getRoomDetailHeaderList() {
        List<RoomDetailHeaderItem> roomDetailHeaderItems = new ArrayList<>();
        roomDetailHeaderItems.add(new RoomDetailHeaderItem("Image 1", "This is dummy image, you could change it anytime on Seeder class", R.drawable.dummy_bg));
        roomDetailHeaderItems.add(new RoomDetailHeaderItem("Image 2", "This is dummy image, you could change it anytime on Seeder class", R.drawable.dummy_bg));
        roomDetailHeaderItems.add(new RoomDetailHeaderItem("Image 3", "This is dummy image, you could change it anytime on Seeder class", R.drawable.dummy_bg));
        roomDetailHeaderItems.add(new RoomDetailHeaderItem("Image 4", "This is dummy image, you could change it anytime on Seeder class", R.drawable.dummy_bg));

        return roomDetailHeaderItems;
    }

    public static List<RoomFacility> getRoomFacilities(int count) {
        List<RoomFacility> roomFacilities = new ArrayList<>();
        for (int i = 1; i < count+1; i++) {
            roomFacilities.add(new RoomFacility(R.drawable.com_facebook_close, "Facility " + i));
        }

        return roomFacilities;
    }

    public static List<Banking> getBankingList() {
        List<Banking> bankingList = new ArrayList<>();
        bankingList.add(new Banking(R.drawable.dummy_bg, "030-222-3797 a.n Ucing Gipsum "));
        bankingList.add(new Banking(R.drawable.dummy_bg, "000-5254-7878 a.n Ucing Gipsum "));
        bankingList.add(new Banking(R.drawable.dummy_bg, "11121-4545-23 a.n Ucing Gipsum "));

        return bankingList;
    }
}
