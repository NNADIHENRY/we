package com.HITech.HILearn.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


import com.HITech.HILearn.R;
import com.HITech.HILearn.database.DatabaseAccess;
import com.HITech.HILearn.methods.TitleStrings;
import com.HITech.HILearn.model.ColorModel;
import com.HITech.HILearn.model.HistoryModel;
import com.HITech.HILearn.model.MainModel;
import com.HITech.HILearn.model.StoreSetModel;
import com.HITech.HILearn.ui.ActivityDailyTest;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class Constant {


    public static final String IsFraction = "IsFraction";
    public static final String TABLE_NAME = "TABLE_NAME";
    public static final String TIME_FORMAT = "hh:mm a";
    public static final String IsReminder = "IsReminder";
    public static final String MAIN_ID = "Main_position";
    public static final String TITLE = "TITLE";
    public static final String LEVEL = "LEVEL";
    public static final String ID = "ID";
    public static final String PRACTICE_SET = "PRACTICE_SET";
    public static final String RIGHT_ANSWER = "RIGHT_ANSWER";
    public static final String WRONG_ANSWER = "WRONG_ANSWER";
    public static final int LEVEL_SIZE = 100;
    public static final int TYPE_MIXED = 1;
    public static final int TYPE_PERCENTAGE = 2;
    public static final int TYPE_SQUARE = 3;
    public static final int TYPE_SQUARE_ROOT = 4;
    public static final int TYPE_CUBE = 5;
    public static final int TYPE_CUBE_ROOT = 6;
    public static final int TYPE_FIND_MISSING = 7;
    public static final String THEMEPOSITION = "THEME_POSITION";
    public static final String POSITION = "POSITION";
    public static final int DEFAULT_QUESTION_SIZE = 20;
    public static final int TIMER = 60;
    private static final String MyPref = "MyPref";
    private static final String HISTORY = "HISTORY";
    private static final String IsFirstTime = "IsFirstTime";
    private static final String HISTORY_SIZE = "HISTORY_SIZE";
    private static final String COINS = "Coins";
    private static final String FIRST_QUIZ = "FIRST_QUIZ";
    private static final String SECOND_QUIZ = "SECOND_QUIZ";
    private static final String THIRD_QUIZ = "THIRD_QUIZ";
    private static final String FOURTH_QUIZ = "FOURTH_QUIZ";
    public static final String MAIN_THEME = "Main_theme";
    public static final String SCORE = "score";
    private static final String SOUND = "Sound";
    private static final String IsAutoFill = "IsAutoFill";
    private static final String AutoFillText = "AutoFillText";
    private static final String LANGUAGE_CODE = "LANGUAGE_CODE";
    private static final String REMINDER_TIME = "REMINDER_TIME";
    public static final String PDF_PATH = Environment.getExternalStorageDirectory().toString() + "/PDF/";
    public static final String PDF_FOLDER_NAME = "/PDF/";
    public static final String DATE = "date";
    public static final int QUIZ_SIZE = 20;
    public static final int TEXT_LENGTH = 6;
    public static final int DELAY_SEOCND = 400;


    public static String getTextString(String s) {
        return s;
    }


    public static int getPlusScore(int countTime) {
        if (countTime < 30 && countTime >= 25) {
            return 500;
        } else if (countTime < 25 && countTime >= 15) {
            return 400;
        } else if (countTime < 15 && countTime >= 5) {
            return 250;
        } else {
            return 100;
        }
    }


    public static int getLayout(Activity activity, boolean isFraction, boolean isDailyQuiz) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

//        if (isFraction) {
//            if (width == 1440) {
//                return R.layout.activity_1440_fraction;
//            } else {
//                return R.layout.activity_fraction;
//            }
//        } else
        if (isDailyQuiz) {
            if (width == 1440) {
                return R.layout.activity_daily_quiz_1440;
            } else {
                return R.layout.activity_daily_quiz;
            }
        } else {
            if (width == 1440) {
                return R.layout.activity_1440_quiz;
            } else {
                return R.layout.activity_quiz;
            }
        }


    }

    public static void share(Context context) {
        Intent sharingIntent1 = new Intent(Intent.ACTION_SEND);
        sharingIntent1.setType("text/plain");
        sharingIntent1.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name));
        sharingIntent1.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + context.getPackageName() + System.getProperty("line.separator"));
        context.startActivity(Intent.createChooser(sharingIntent1, "Share via"));

    }


    public static List<StoreSetModel> getTitleList(String tableName, Context context) {

        if (tableName.equals(context.getString(R.string.maths_set))) {
            return TitleStrings.getMathsSetTitles(context);
        } else if (tableName.equals(context.getString(R.string.english_set))) {
            return TitleStrings.getEnglishSetTitles(context);
        } else if (tableName.equals(context.getString(R.string.biology_set))) {
            return TitleStrings.getBiologySetTitles(context);
        }else if (tableName.equals(context.getString(R.string.chemistry_set))) {
            return TitleStrings.getChemistrySetTitles(context);
        } else if (tableName.equals(context.getString(R.string.physics_set))) {
            return TitleStrings.getPhysicsSetTitles(context);
        }else if (tableName.equals(context.getString(R.string.crk_set))) {
            return TitleStrings.getCrkSetTitles(context);
        } else if (tableName.equals(context.getString(R.string.irk_set))) {
            return TitleStrings.getIrkSetTitles(context);
        }else if (tableName.equals(context.getString(R.string.geography_set))) {
            return TitleStrings.getGeographySetTitles(context);
        } else if (tableName.equals(context.getString(R.string.government_set))) {
            return TitleStrings.getGovernmentSetTitles(context);
        }else if (tableName.equals(context.getString(R.string.history_set))) {
            return TitleStrings.getHistorySetTitles(context);
        } else if (tableName.equals(context.getString(R.string.civic_set))) {
            return TitleStrings.getCivicSetTitles(context);
        }else if (tableName.equals(context.getString(R.string.account_set))) {
            return TitleStrings.getAccountSetTitles(context);
        } else if (tableName.equals(context.getString(R.string.economics_set))) {
            return TitleStrings.getEconomicsSetTitles(context);
        }else if (tableName.equals(context.getString(R.string.commerce_set))) {
            return TitleStrings.getCommerceSetTitles(context);
        } else if (tableName.equals(context.getString(R.string.literature_set))) {
            return TitleStrings.getLiteratureSetTitles(context);
        }
        else {
            return TitleStrings.getAgricSetTitles(context);
        }
    }


    public static String getType(Context context, int level_no) {
        if (level_no <= 30) {
            return context.getString(R.string.easy);
        } else if (level_no <= 65) {
            return context.getString(R.string.medium);
        } else {
            return context.getString(R.string.hard);

        }
    }


    public static void setDefaultLanguage(Activity activity) {
        Locale locale = new Locale(getLanguageCode(activity));
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        activity.getBaseContext().getResources().updateConfiguration(config,
                activity.getBaseContext().getResources().getDisplayMetrics());
    }

      public static String getLanguageCode(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        return sharedPreferences.getString(LANGUAGE_CODE, context.getString(R.string.en_code));
    }


    public static void setSound(Context context, boolean s) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SOUND, s);
        editor.apply();
    }


    public static void setLanguageCode(Context context, String s) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LANGUAGE_CODE, s);
        editor.apply();
    }

    public static void setReminderTime(Context context, String s) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(REMINDER_TIME, s);
        editor.apply();
    }

    public static String getReminderTime(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        return sharedPreferences.getString(REMINDER_TIME, context.getString(R.string.default_reminder_text));
    }


    public static boolean getSound(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(SOUND, true);
    }


    public static int getRefIdType(int level_no) {
        if (level_no <= 30) {
            return 1;
        } else if (level_no <= 65) {
            return 2;
        } else {
            return 3;
        }
    }

    public static String getCurrentDate(Activity activity) {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat(activity.getString(R.string.date_format), Locale.US);
        return df.format(c);

    }

    public static String getDateFormat(Activity activity, Date date) {
        SimpleDateFormat df = new SimpleDateFormat(activity.getString(R.string.date_format), Locale.US);
        return df.format(date);

    }

    public static void addHistoryData(Activity activity, HistoryModel historyModel) {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(activity);
        databaseAccess.open();
        databaseAccess.insertHistoryDataData(historyModel);
        databaseAccess.close();

    }


    public static List<HistoryModel> getHistoryModel(Context context) {
        List<HistoryModel> modelList = new ArrayList<>();

        for (int i = 0; i < getHistorySize(context); i++) {
            SharedPreferences sharedPreferences1 = context.getSharedPreferences(MyPref, Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences1.getString(HISTORY + i, null);
            HistoryModel model = gson.fromJson(json, HistoryModel.class);

            if (model != null) {
                modelList.add(model);
            }
        }

        return modelList;
    }

    public static void addModel(Context context, List<HistoryModel> historyModels) {

        setHistorySize(context, historyModels.size());
        for (int i = 0; i < historyModels.size(); i++) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(MyPref, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(historyModels.get(i));
            editor.putString(HISTORY + i, json);
            editor.apply();

        }
    }

    public static String getLanguageCodeFromLanguage(Context context, String s) {


        if (s.equalsIgnoreCase(context.getString(R.string.english))) {
            return context.getString(R.string.en_code);
        } else if (s.equalsIgnoreCase(context.getString(R.string.french))) {
            return context.getString(R.string.fr_code);
        } else if (s.equalsIgnoreCase(context.getString(R.string.spanish))) {
            return context.getString(R.string.es_code);
        } else if (s.equalsIgnoreCase(context.getString(R.string.arabic))) {
            return context.getString(R.string.ar_code);
        } else if (s.equalsIgnoreCase(context.getString(R.string.russian))) {
            return context.getString(R.string.ru_code);
        } else if (s.equalsIgnoreCase(context.getString(R.string.malay))) {
            return context.getString(R.string.ms_code);
        } else if (s.equalsIgnoreCase(context.getString(R.string.mandarin_chinese))) {
            return context.getString(R.string.zh_code);
        }




        return null;
    }





    public static Bitmap getBitmapFromAsset(Context activity, String strName) {
        AssetManager assetManager = activity.getAssets();
        InputStream istr = null;
        try {
            istr = assetManager.open(strName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(istr);
    }

    public static int getStarCount(int progress) {
        if (progress == 0) {
            return 0;
        } else {
            if (progress < 50) {
                return 1;
            } else if (progress < 90 && progress > 50) {
                return 2;
            } else {
                return 3;
            }
        }
    }

    private static void setHistorySize(Context context, int history_size) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(HISTORY_SIZE, history_size);
        editor.apply();
        editor.apply();
    }

    private static int getHistorySize(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(HISTORY_SIZE, 0);
    }


    public static void setCoins(Context context, int coins) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(COINS, coins);
        editor.apply();
        editor.apply();
    }

    public static int getCoins(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(COINS, 0);
    }


    public static List<MainModel> getMainModel(Activity context) {

        List<MainModel> mainModelList = new ArrayList<>();
        mainModelList.add(new MainModel(1, context.getString(R.string.jamb), getFirstQuiz(context), getFirstOperationList(context)));
        mainModelList.add(new MainModel(2, "", getFirstQuiz(context), getSecondOperationList(context)));
        mainModelList.add(new MainModel(3, "", getFirstQuiz(context), getthirdOperationList(context)));
        mainModelList.add(new MainModel(4, "", getFirstQuiz(context), getFourthOperationList(context)));
        return mainModelList;
    }

    public static int getFirstQuiz(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(FIRST_QUIZ, 0);
    }

    private static int getSecondQuiz(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(SECOND_QUIZ, 0);
    }


    public static void setFirstQuiz(Context context, int integerQuiz) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(FIRST_QUIZ, integerQuiz);
        editor.apply();
        editor.apply();
    }

    public static void setSecondQuiz(Context context, int decimalQuiz) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SECOND_QUIZ, decimalQuiz);
        editor.apply();
        editor.apply();
    }
    public static int getThirdQuiz(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(THIRD_QUIZ, 0);
    }

    private static int getFourthQuiz(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(FOURTH_QUIZ, 0);
    }


    public static void setThirdQuiz(Context context, int integerQuiz) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(THIRD_QUIZ, integerQuiz);
        editor.apply();
        editor.apply();
    }

    public static void setFourthQuiz(Context context, int decimalQuiz) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(FOURTH_QUIZ, decimalQuiz);
        editor.apply();
        editor.apply();
    }

    public static String getToolbarTitle(Activity context, String tableName) {
        setDefaultLanguage(context);
        if (tableName.equals(context.getString(R.string.maths_set))) {
            return context.getString(R.string.maths);
        } else if (tableName.equals(context.getString(R.string.agric_set))) {
            return context.getString(R.string.agric);
        } else if (tableName.equals(context.getString(R.string.biology_set))) {
            return context.getString(R.string.biology);
        }else if (tableName.equals(context.getString(R.string.chemistry_set))) {
            return context.getString(R.string.chemistry);
        } else if (tableName.equals(context.getString(R.string.physics_set))) {
            return context.getString(R.string.physics);
        }else if (tableName.equals(context.getString(R.string.crk_set))) {
            return context.getString(R.string.crk);
        } else if (tableName.equals(context.getString(R.string.irk_set))) {
            return context.getString(R.string.irk);
        }else if (tableName.equals(context.getString(R.string.geography_set))) {
            return context.getString(R.string.geography);
        } else if (tableName.equals(context.getString(R.string.government_set))) {
            return context.getString(R.string.government);
        }else if (tableName.equals(context.getString(R.string.history_set))) {
            return context.getString(R.string.history);
        } else if (tableName.equals(context.getString(R.string.civic_set))) {
            return context.getString(R.string.civiceducation);
        }else if (tableName.equals(context.getString(R.string.account_set))) {
            return context.getString(R.string.account);
        } else if (tableName.equals(context.getString(R.string.commerce_set))) {
            return context.getString(R.string.commerce);
        }else if (tableName.equals(context.getString(R.string.economics_set))) {
            return context.getString(R.string.economics);
        } else if (tableName.equals(context.getString(R.string.literature_set))) {
            return context.getString(R.string.lit);
        }
        else {
            return context.getString(R.string.english);
        }
    }



    public static String getFirstToolbarTitleFromId(Activity context, int id) {
        setDefaultLanguage(context);
        if (id == 1) {
            return context.getString(R.string.maths);
        } else if (id == 2) {
            return context.getString(R.string.english);
        } else if (id == 3) {
            return context.getString(R.string.agric);
        } else if (id == 3) {
            return context.getString(R.string.agric);
        } else {
            return context.getString(R.string.biology);
        }
    }

    public static String getFourthToolbarTitleFromId(Activity context, int id) {
        setDefaultLanguage(context);
        if (id == 1) {
            return context.getString(R.string.account);
        } else if (id == 2) {
            return context.getString(R.string.commerce);
        } else if (id == 3) {
            return context.getString(R.string.economics);
        } else {
            return context.getString(R.string.lit);
        }
    }
    public static String getThirdToolbarTitleFromId(Activity context, int id) {
        setDefaultLanguage(context);
        if (id == 1) {
            return context.getString(R.string.geography);
        } else if (id == 2) {
            return context.getString(R.string.government);
        } else if (id == 3) {
            return context.getString(R.string.history);
        } else {
            return context.getString(R.string.civiceducation);
        }
    }
    public static String getSecondToolbarTitleFromId(Activity context, int id) {
        setDefaultLanguage(context);
        if (id == 1) {
            return context.getString(R.string.chemistry);
        } else if (id == 2) {
            return context.getString(R.string.physics);
        } else if (id == 3) {
            return context.getString(R.string.crk);
        } else {
            return context.getString(R.string.irk);
        }
    }


    public static boolean getIsFirstTime(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(IsFirstTime, true);
    }


    public static void setIsFirstTime(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IsFirstTime, false);
        editor.apply();
    }


    public static String getFirstTableName(Context context, int position) {
        String tableName;
        if (position == 0) {
            tableName = context.getString(R.string.maths_set);
        } else if (position == 1) {
            tableName = context.getString(R.string.english_set);
        } else if (position == 2) {
            tableName = context.getString(R.string.agric_set);
        } else {
            tableName = context.getString(R.string.biology_set);
        }
        return tableName;
    }
    public static String getSecondTableName(Context context, int position) {
        String tableName;
        if (position == 0) {
            tableName = context.getString(R.string.chemistry_set);
        } else if (position == 1) {
            tableName = context.getString(R.string.physics_set);
        } else if (position == 2) {
            tableName = context.getString(R.string.crk_set);
        } else {
            tableName = context.getString(R.string.irk_set);
        }
        return tableName;
    }
    public static String getThirdTableName(Context context, int position) {
        String tableName;
        if (position == 0) {
            tableName = context.getString(R.string.geography_set);
        } else if (position == 1) {
            tableName = context.getString(R.string.government_set);
        } else if (position == 2) {
            tableName = context.getString(R.string.history_set);
        } else {
            tableName = context.getString(R.string.civic_set);
        }
        return tableName;
    }
    public static String getFourthTableName(Context context, int position) {
        String tableName;
        if (position == 0) {
            tableName = context.getString(R.string.account_set);
        } else if (position == 1) {
            tableName = context.getString(R.string.commerce_set);
        } else if (position == 2) {
            tableName = context.getString(R.string.economics_set);
        } else {
            tableName = context.getString(R.string.literature_set);
        }
        return tableName;
    }


    public static GradientDrawable customViewOval(int[] colors) {
        GradientDrawable shape = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        shape.setShape(GradientDrawable.OVAL);
        return shape;
    }


    public static List<String> getFirstOperationList(Activity context) {
        setDefaultLanguage(context);
        List<String> mainModelList = new ArrayList<>();
        mainModelList.add(context.getString(R.string.maths));
        mainModelList.add(context.getString(R.string.english));
        mainModelList.add(context.getString(R.string.agric));
        mainModelList.add(context.getString(R.string.biology));
        return mainModelList;
    }
    private static List<String> getSecondOperationList(Activity context) {
        setDefaultLanguage(context);
        List<String> mainModelList = new ArrayList<>();
        mainModelList.add(context.getString(R.string.chemistry));
        mainModelList.add(context.getString(R.string.physics));
        mainModelList.add(context.getString(R.string.crk));
        mainModelList.add(context.getString(R.string.irk));
        return mainModelList;
    }
    private static List<String> getthirdOperationList(Activity context) {
        setDefaultLanguage(context);
        List<String> mainModelList = new ArrayList<>();
        mainModelList.add(context.getString(R.string.geography));
        mainModelList.add(context.getString(R.string.government));
        mainModelList.add(context.getString(R.string.history));
        mainModelList.add(context.getString(R.string.civiceducation));
        return mainModelList;
    }



    public static List<ColorModel> getDrawbles() {
        List<ColorModel> integerList = new ArrayList<>();
        integerList.add(new ColorModel(R.drawable.bg_addition, R.color.orangeColorPrimary, R.color.orangeColorPrimaryDark, R.drawable.cell_orange));
        integerList.add(new ColorModel(R.drawable.bg_subtraction, R.color.cyanColorPrimary, R.color.cyanColorPrimaryDark, R.drawable.cell_cyan));
        integerList.add(new ColorModel(R.drawable.bg_multiplication, R.color.violetColorPrimary, R.color.violetColorPrimaryDark, R.drawable.cell_violet));
        integerList.add(new ColorModel(R.drawable.bg_division, R.color.blueColorPrimary, R.color.blueColorPrimaryDark, R.drawable.cell_blue));
        integerList.add(new ColorModel(R.drawable.bg_addition, R.color.orangeColorPrimary, R.color.orangeColorPrimaryDark, R.drawable.cell_orange));
        integerList.add(new ColorModel(R.drawable.bg_subtraction, R.color.cyanColorPrimary, R.color.cyanColorPrimaryDark, R.drawable.cell_cyan));
        integerList.add(new ColorModel(R.drawable.bg_multiplication, R.color.violetColorPrimary, R.color.violetColorPrimaryDark, R.drawable.cell_violet));
        integerList.add(new ColorModel(R.drawable.bg_division, R.color.blueColorPrimary, R.color.blueColorPrimaryDark, R.drawable.cell_blue));
        integerList.add(new ColorModel(R.drawable.bg_addition, R.color.orangeColorPrimary, R.color.orangeColorPrimaryDark, R.drawable.cell_orange));
        integerList.add(new ColorModel(R.drawable.bg_subtraction, R.color.cyanColorPrimary, R.color.cyanColorPrimaryDark, R.drawable.cell_cyan));
        integerList.add(new ColorModel(R.drawable.bg_multiplication, R.color.violetColorPrimary, R.color.violetColorPrimaryDark, R.drawable.cell_violet));
        integerList.add(new ColorModel(R.drawable.bg_division, R.color.blueColorPrimary, R.color.blueColorPrimaryDark, R.drawable.cell_blue));
        integerList.add(new ColorModel(R.drawable.bg_addition, R.color.orangeColorPrimary, R.color.orangeColorPrimaryDark, R.drawable.cell_orange));
        integerList.add(new ColorModel(R.drawable.bg_subtraction, R.color.cyanColorPrimary, R.color.cyanColorPrimaryDark, R.drawable.cell_cyan));
        integerList.add(new ColorModel(R.drawable.bg_multiplication, R.color.violetColorPrimary, R.color.violetColorPrimaryDark, R.drawable.cell_violet));
        integerList.add(new ColorModel(R.drawable.bg_division, R.color.blueColorPrimary, R.color.blueColorPrimaryDark, R.drawable.cell_blue));
        integerList.add(new ColorModel(R.drawable.bg_addition, R.color.orangeColorPrimary, R.color.orangeColorPrimaryDark, R.drawable.cell_orange));
        integerList.add(new ColorModel(R.drawable.bg_subtraction, R.color.cyanColorPrimary, R.color.cyanColorPrimaryDark, R.drawable.cell_cyan));
        return integerList;
    }

    public static List<Integer> getFirstDefaultIcons1() {
        List<Integer> integerList = new ArrayList<>();
        integerList.add(R.drawable.ic_mixed);
        integerList.add(R.drawable.english_icon);
        integerList.add(R.drawable.agric_icon);
        integerList.add(R.drawable.biology_icon);
        return integerList;
    }
    public static List<Integer> getSecondDefaultIcons1() {
        List<Integer> integerList = new ArrayList<>();
        integerList.add(R.drawable.chemistry_icon);
        integerList.add(R.drawable.physics_icon);
        integerList.add(R.drawable.crk);
        integerList.add(R.drawable.irk_icon);
        return integerList;
    }
    public static List<Integer> getThirdDefaultIcons1() {
        List<Integer> integerList = new ArrayList<>();
        integerList.add(R.drawable.geography_icon);
        integerList.add(R.drawable.government_icon);
        integerList.add(R.drawable.history);
        integerList.add(R.drawable.literature_icon);
        return integerList;
    }


    public static List<Integer> getMixedIcons() {
        List<Integer> integerList = new ArrayList<>();
        integerList.add(R.drawable.account_icon);
        integerList.add(R.drawable.commerce_icon);
        integerList.add(R.drawable.economics_icon);
        integerList.add(R.drawable.literature_icon);
//        integerList.add(R.drawable.cube);
//        integerList.add(R.drawable.cube_root);
        return integerList;
    }

    private static List<String> getFourthOperationList(Activity context) {
        setDefaultLanguage(context);
        List<String> mainModelList = new ArrayList<>();
        mainModelList.add(context.getString(R.string.account));
        mainModelList.add(context.getString(R.string.commerce));
        mainModelList.add(context.getString(R.string.economics));
        mainModelList.add(context.getString(R.string.lit));
//        mainModelList.add(context.getString(R.string.crk));
//        mainModelList.add(context.getString(R.string.irk));
        return mainModelList;
    }


    public static void showDoneDialog(final Activity activity) {
        setDefaultLanguage(activity);
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_today_done, null);
        builder.setView(view);
        builder.setCancelable(false);


        TextView btn_done;
        btn_done = view.findViewById(R.id.btn_done);


        final AlertDialog setDialog = builder.create();
        setDialog.show();
        Objects.requireNonNull(setDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);


        btn_done.setOnClickListener(view1 -> {
            Intent intent = new Intent(activity, ActivityDailyTest.class);
            activity.startActivity(intent);
            setDialog.dismiss();
        });

    }


}

