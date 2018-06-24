package com.jak.letsplay.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.jak.letsplay.R;

import java.util.HashMap;

public class Constants {
    private static String baseURL = "https://www.jak2018.freehosting.co.nz/api/";
    public static String loginURL = baseURL + "login.php";
    public static String friendsListUrl = baseURL + "getfriendslist.php";
    public static String gameStatsURL = baseURL + "getgamestats.php";
    public static String removeFriendURL = baseURL + "removefriend.php";
    public static String gamesListURL = baseURL + "getgameslist.php";
    public static String removeGameURL = baseURL + "removegame.php";
    public static String letsPlayURL = baseURL + "letsplay.php";
    public static String addGameURL = baseURL + "addgame.php";
    public static String registerURL = baseURL + "register.php";
    public static String searchURL = baseURL + "search.php";
    public static String addFriendURL = baseURL + "addfriend.php";
    public static String forgotPasswordURL = baseURL + "forgotpassword.php";
    public static String fcmTokenURL = baseURL + "fcmtoken.php";

    public static String PREF_USERNAME = "username";

    public static HashMap<String, String> gameFullNameMap;
    public static HashMap<String, String> gameShortNameMap;
    public static HashMap<String, Integer> gameBannerMap;
    public static HashMap<String, Integer> gameIconMap;
    public static HashMap<String, String> gamePlatformMap;

    static {
        gameFullNameMap = new HashMap<>();
        gameFullNameMap.put("csgo", "Counter Strike: Global Offensive");
        gameFullNameMap.put("fort", "Fortnite");
        gameFullNameMap.put("coc", "Clash of Clans");
        gameFullNameMap.put("cr", "Clash Royale");
    }

    static {
        gameBannerMap = new HashMap<>();
        gameBannerMap.put("csgo", R.drawable.csgo_banner);
        gameBannerMap.put("fort", R.drawable.fort_banner);
        gameBannerMap.put("coc", R.drawable.coc_banner);
        gameBannerMap.put("cr", R.drawable.cr_banner);
    }

    static {
        gameIconMap = new HashMap<>();
        gameIconMap.put("csgo", R.drawable.csgo);
        gameIconMap.put("fort", R.drawable.fort);
        gameIconMap.put("coc", R.drawable.coc);
        gameIconMap.put("cr", R.drawable.cr);
    }

    static {
        gameShortNameMap = new HashMap<>();
        gameShortNameMap.put("Counter Strike: Global Offensive", "csgo");
        gameShortNameMap.put("Fortnite", "fort");
        gameShortNameMap.put("Clash of Clans", "coc");
        gameShortNameMap.put("Clash Royale", "cr");
    }

    static {
        gamePlatformMap = new HashMap<>();
        gamePlatformMap.put("Custom URL ID", "custom");
        gamePlatformMap.put("Steam 64 ID", "steam64");
        gamePlatformMap.put("PC", "pc");
        gamePlatformMap.put("PS4", "psn");
        gamePlatformMap.put("Xbox", "xbl");
        gamePlatformMap.put("Android", "Android");
        gamePlatformMap.put("iOS", "iOS");
    }

    public static void toast(Context context, String message) {
        Toast t = Toast.makeText(context, message, Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();
    }

    public static void errorToast(Context context) {
        toast(context, "An error occurred. Please try again.");
    }
}
