package com.HITech.HILearn.ui;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;

import com.HITech.HILearn.calc.ScientificCal;
import com.HITech.HILearn.calc.StandardCal;
import com.HITech.HILearn.calc.UnitArea;
import com.HITech.HILearn.calc.UnitLength;
import com.HITech.HILearn.calc.UnitTemperature;
import com.HITech.HILearn.calc.UnitWeight;
import com.bumptech.glide.Glide;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.ads.MobileAds;
import com.HITech.HILearn.R;
import com.HITech.HILearn.adapter.MainAdapter;
import com.HITech.HILearn.database.DatabaseAccess;
import com.HITech.HILearn.model.MainModel;
import com.HITech.HILearn.model.SetModel;
import com.HITech.HILearn.receiver.AlarmReceiver;
import com.HITech.HILearn.utils.Constant;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.thekhaeng.pushdownanim.PushDownAnim;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import co.paystack.android.PaystackSdk;

import static com.thekhaeng.pushdownanim.PushDownAnim.DEFAULT_PUSH_DURATION;
import static com.thekhaeng.pushdownanim.PushDownAnim.DEFAULT_RELEASE_DURATION;
import static com.thekhaeng.pushdownanim.PushDownAnim.MODE_SCALE;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainAdapter.MainItemClick {
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    MainAdapter mainAdapter;
    ImageView btn_drawer;
    DrawerLayout drawer;
    LinearLayout btn_find_missing;
    List<MainModel> mainModels = new ArrayList<>();
    int id;
    Menu menu;
    SwitchCompat switcher;
    MenuItem nav_language;
    MenuItem nav_sound;
    MenuItem nav_coin;
    MenuItem nav_discus;

   public ImageView imageView, imcalc;
public String textName, textEmail, phoneNum;
    FirebaseAuth mAuth;


    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constant.setDefaultLanguage(this);
        setContentView(R.layout.activity_main);
        PaystackSdk.initialize(getApplicationContext());

        MobileAds.initialize(this, initializationStatus -> {

        });
        mAuth = FirebaseAuth.getInstance();

        setNotification();
        init();
        setClick();
//        gmailsign();
imcalc = findViewById(R.id.imcalc);
imcalc.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        showDialog();
//        Intent intent = new Intent(getApplicationContext(), com.HITech.HILearn.calc.MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
//        startActivity(intent);
//        showDialog();
    }
});
    }


    public void setNotification() {
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 100, pendingIntent);
        PackageManager packageManager = getPackageManager();
        ComponentName componentName = new ComponentName(this, AlarmReceiver.class);
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    private void setClick() {
        btn_drawer.setOnClickListener(v -> drawer.openDrawer(GravityCompat.START));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            ActivityCompat.finishAffinity(this);
        }
    }

    private void init() {
        progressDialog = new ProgressDialog(MainActivity.this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        menu = navigationView.getMenu();



        nav_sound = menu.findItem(R.id.nav_sound);
        nav_coin = menu.findItem(R.id.nav_coin);
        nav_discus = menu.findItem(R.id.nav_discus);

        View actionView = MenuItemCompat.getActionView(nav_sound);

        switcher = actionView.findViewById(R.id.drawer_switch);


        switcher.setOnCheckedChangeListener((compoundButton, b) -> {
            Constant.setSound(getApplicationContext(), b);
            setOnOffSound();
        });
        setOnOffSound();

        nav_coin.setTitle(getString(R.string.coins) + getString(R.string.single_space) + Constant.getCoins(getApplicationContext()));
        btn_drawer = findViewById(R.id.btn_drawer);


        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
//        Constant.setDefaultLanguage(this);

        if (Constant.getIsFirstTime(getApplicationContext())) {
            Constant.setIsFirstTime(getApplicationContext());
            new GetAllData().execute();

        } else {
            setAdapter();
        }
    }


    public void setAdapter() {
        mainModels.clear();
        mainModels = Constant.getMainModel(MainActivity.this);

        int actionBarHeight = 0;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }

        mainAdapter = new MainAdapter(MainActivity.this, mainModels, (width), (height - (actionBarHeight + 80)));
        recyclerView.setAdapter(mainAdapter);
        mainAdapter.setMainClickListener(MainActivity.this);
        recyclerView.scrollToPosition(getIntent().getIntExtra(Constant.POSITION, 0));


    }


    public class GetAllData extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            List<SetModel> setModelList;
            List<SetModel> setModelList1;
            List<Integer> integers = new ArrayList<>();
            List<Integer> integers1 = new ArrayList<>();
            List<Integer> integers2 = new ArrayList<>();
            List<Integer> integers3 = new ArrayList<>();

            int practice_set = 0;
            int practice_set1 = 0;
            int practice_set2 = 0;
            int practice_set3 = 0;

            for (int i = 0; i < 4; i++) {

                String tableName;
//                int i = 0;

                if (i == 0) {
                    tableName = getString(R.string.maths_set);
                } else if (i == 1) {
                    tableName = getString(R.string.english_set);
                } else if (i == 2) {
                    tableName = getString(R.string.agric_set);
                } else {
                    tableName = getString(R.string.biology_set);
                }
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(MainActivity.this);
                databaseAccess.open();
                setModelList = databaseAccess.getSetdata(tableName, 1, false);
                databaseAccess.close();

                for (int j = 0; j < setModelList.size(); j++) {
                    integers.add(setModelList.get(j).practice_set);
                }
                DatabaseAccess db = DatabaseAccess.getInstance(MainActivity.this);
                db.open();
                setModelList1 = db.getSetdata(tableName, 1, false);
                db.close();
                for (int j = 0; j < setModelList1.size(); j++) {
                    integers1.add(setModelList1.get(j).practice_set);
                }
            }

            for (int i = 0; i < integers.size(); i++) {
                practice_set = practice_set + integers.get(i);
            }
            for (int i = 0; i < integers1.size(); i++) {

                practice_set1 = practice_set1 + integers1.get(i);
            }
            for (int i = 0; i < integers2.size(); i++) {
                practice_set2 = practice_set2 + integers.get(i);
            }
            for (int i = 0; i < integers3.size(); i++) {

                practice_set3 = practice_set3 + integers1.get(i);
            }


            Constant.setFirstQuiz(getApplicationContext(), practice_set);
            Constant.setSecondQuiz(getApplicationContext(), practice_set1);
            Constant.setThirdQuiz(getApplicationContext(), practice_set2);
            Constant.setFourthQuiz(getApplicationContext(), practice_set3);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            setAdapter();


        }


    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation analytical.xml item clicks here.
        int id = item.getItemId();
         if (id == R.id.nav_share) {
            Constant.share(MainActivity.this);
        } else if (id == R.id.nav_rate_us) {
            showRatingDialog();
        } else if (id == R.id.nav_join) {
             Intent intent = new Intent(MainActivity.this, help.class);
             startActivity(intent);
         }else if (id == R.id.nav_feedback) {
            showFeedbackDialog(this);
        }  else if (id == R.id.nav_logout) {
             mAuth.signOut();
             Intent intent = new Intent(MainActivity.this, AuthenticationActivity.class);
             startActivity(intent);
    }
//         else if (id == R.id.nav_pay) {
//             Intent intent = new Intent(MainActivity.this, Pay.class);
//             startActivity(intent);
//         }
         else if (id == R.id.nav_discus) {
             Intent intent = new Intent(MainActivity.this, DiscussionActivity.class);
             startActivity(intent);
        }else if (id == R.id.nav_novels) {
             shownovels();
//             Intent intent = new Intent(MainActivity.this, Articles.class);
//             startActivity(intent);
         }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setOnOffSound() {
        if (Constant.getSound(getApplicationContext())) {
            nav_sound.setTitle(getString(R.string.sound) + "/" + getString(R.string.on));
            switcher.setChecked(true);
        } else {
            nav_sound.setTitle(getString(R.string.sound) + "/" + getString(R.string.off));
            switcher.setChecked(false);
        }

    }

    @Override
    public void mainItemClick(int main_id, int position, String title, int themePosition) {

        Intent intent;
        String tableName;

        tableName = Constant.getSecondTableName(getApplicationContext(), position);
        intent = new Intent(MainActivity.this, SetActivity.class);
        if (main_id == 3) {
//            isFraction = true;
            intent = new Intent(MainActivity.this, SetActivity.class);
//            intent.putExtra(Constant.ID, (position + 1));
            tableName = Constant.getThirdTableName(getApplicationContext(), position);
        } else if (main_id == 4) {
            tableName = Constant.getFourthTableName(getApplicationContext(), position);
            intent = new Intent(MainActivity.this, SetActivity.class);
//            intent.putExtra(Constant.TITLE, title);
//            intent.putExtra(Constant.ID, (position + 1));
        } else if (main_id == 2) {
        tableName = Constant.getSecondTableName(getApplicationContext(), position);
        intent = new Intent(MainActivity.this, SetActivity.class);
//        intent.putExtra(Constant.TITLE, title);
//        intent.putExtra(Constant.ID, (position + 1));
    }
        else if (main_id == 1) {
            tableName = Constant.getFirstTableName(getApplicationContext(), position);
            intent = new Intent(MainActivity.this, SetActivity.class);
        }
        else{
            Toast.makeText(this, "no table found", Toast.LENGTH_LONG).show();
        }

        if (!TextUtils.isEmpty(tableName)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra(Constant.MAIN_ID, main_id);
            intent.putExtra(Constant.POSITION, position);
            intent.putExtra(Constant.TABLE_NAME, tableName);
            intent.putExtra(Constant.THEMEPOSITION, themePosition);
            intent.putExtra(Constant.MAIN_THEME, themePosition);

            startActivity(intent);

        }

    }




    public static void showFeedbackDialog(Activity activity) {
        final androidx.appcompat.app.AlertDialog alertDialog;
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_feedback, null);
        builder.setView(view);
        final EditText edt_feedback = view.findViewById(R.id.edt_feedback);
        TextView btn_submit = view.findViewById(R.id.btn_submit);
        TextView btn_cancel = view.findViewById(R.id.btn_cancel);
        alertDialog = builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimationTheme;
        PushDownAnim.setPushDownAnimTo(btn_submit).setScale(MODE_SCALE, 0.89f).setDurationPush(DEFAULT_PUSH_DURATION).setDurationRelease(DEFAULT_RELEASE_DURATION);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        alertDialog.show();
        btn_cancel.setOnClickListener(v -> alertDialog.dismiss());
        btn_submit.setOnClickListener(v -> {

            if (!TextUtils.isEmpty(edt_feedback.getText().toString())) {
                alertDialog.dismiss();
                sendFeedbackFromUser(activity, edt_feedback.getText().toString());
            } else {
                Toast.makeText(activity, "" + activity.getString(R.string.empty_feedback), Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void showRatingDialog() {
        final AlertDialog alert_dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_rating, null);
        builder.setView(view);
        final RatingBar rating_bar = view.findViewById(R.id.rating_bar);
        TextView btn_submit = view.findViewById(R.id.btn_submit);
        TextView tv_no = view.findViewById(R.id.tv_no);
        PushDownAnim.setPushDownAnimTo(btn_submit).setScale(MODE_SCALE, 0.89f).setDurationPush(DEFAULT_PUSH_DURATION).setDurationRelease(DEFAULT_RELEASE_DURATION);

        alert_dialog = builder.create();
        alert_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alert_dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTheme;
        alert_dialog.show();

        btn_submit.setOnClickListener(v -> {
            if (rating_bar.getRating() >= 3) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")));

                } catch (ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")));
                }
                alert_dialog.dismiss();
            } else if (rating_bar.getRating() <= 0) {
                Toast.makeText(MainActivity.this, "" + getString(R.string.rating_error), Toast.LENGTH_SHORT).show();
            }

        });
        tv_no.setOnClickListener(v -> alert_dialog.dismiss());
    }


    private static void sendFeedbackFromUser(Activity activity, String txt) {
        String mailto = activity.getString(R.string.feedback_mail) +
                "?cc=" + "" +
                "&subject=" + Uri.encode(activity.getString(R.string.app_name)) +
                "&body=" + Uri.encode(txt);

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse(mailto));

        try {
            activity.startActivity(emailIntent);
        } catch (ActivityNotFoundException ignored) {
        }

    }
    private void gmailsign(){

        imageView = findViewById(R.id.ivProfile);


        FirebaseUser user = mAuth.getCurrentUser();

        Glide.with(this)
                .load(user.getPhotoUrl())
                .into(imageView);
       textName = (user.getDisplayName());
        textEmail= (user.getEmail());
        phoneNum = (user.getPhoneNumber());
    }

    @Override
    protected void onStart() {
        super.onStart();

        //if the user is not logged in
        //opening the login activity
        if (mAuth.getCurrentUser() == null) {
//            finish();
            Intent intent = new Intent(this, AuthenticationActivity.class );
            startActivity(intent);
//            startActivity(new Intent(this, AuthenticationActivity.class));
        }else{
            gmailsign();
        }
    }
    public void showDialog() {
        // Override active layout
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.content_main1    , null);
        // AlertDialog used for pop-Ups
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        // Used to link or get views in the dialogBox

        builder.setCancelable(false)
                // Used to set Negative button to cancel
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogBox, int id) {
                        dialogBox.cancel();

                    }
                });

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button button, button1, button2;
        button = alertDialog.findViewById(R.id.button);
        button1 = alertDialog.findViewById(R.id.button1);
        button2 = alertDialog.findViewById(R.id.button2);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, StandardCal.class);
                startActivity(intent);
                alertDialog.dismiss();


            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ScientificCal.class);
                startActivity(intent);alertDialog.dismiss();

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, UnitCoverter.class);
//                startActivity(intent);
                showUnitConverterDialog();
                alertDialog.dismiss();
            }
        });
    }

    private void showUnitConverterDialog() {
        // Override active layout
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.content_unit_coverter    , null);
        // AlertDialog used for pop-Ups
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(view);

        // Used to link or get views in the dialogBox

        builder.setCancelable(false)
                // Used to set Negative button to cancel
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogBox, int id) {
                        dialogBox.cancel();

                    }
                });

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button button = alertDialog.findViewById(R.id.area);
        Button button1 = alertDialog.findViewById(R.id.length);
        Button button2 = alertDialog.findViewById(R.id.weight);
        Button button3 = alertDialog.findViewById(R.id.tempearture);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UnitTemperature.class);
                startActivity(intent);
                alertDialog.dismiss();


            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UnitLength.class);
                startActivity(intent);
                alertDialog.dismiss();


            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UnitArea.class);
                startActivity(intent);alertDialog.dismiss();

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UnitWeight.class);
                startActivity(intent);
                alertDialog.dismiss();
            }
        });

    }
    private void showArticle() {
        // Override active layout
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.activity_articles    , null);
        // AlertDialog used for pop-Ups
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(view);

        // Used to link or get views in the dialogBox

        builder.setCancelable(false)
                // Used to set Negative button to cancel
                .setTitle(R.string.sweet_sixteen)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogBox, int id) {
                        dialogBox.cancel();

                    }
                });

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        expListView = (ExpandableListView) alertDialog.findViewById(R.id.lvExp);
        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return false;
            }
        });
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });
        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });
        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
//                Toast.makeText(
//                        getApplicationContext(),
//                        listDataHeader.get(groupPosition)
//                                + " : "
//                                + listDataChild.get(
//                                listDataHeader.get(groupPosition)).get(
//                                childPosition), Toast.LENGTH_SHORT)
//                        .show();
                return false;
            }
        });

    }
    public void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add(getString(R.string.sweeth1));
        listDataHeader.add(getString(R.string.sweeth2));
        listDataHeader.add(getString(R.string.sweeth3));
        listDataHeader.add(getString(R.string.sweeth4));
        listDataHeader.add(getString(R.string.sweeth5));
        listDataHeader.add(getString(R.string.sweeth6));
        listDataHeader.add(getString(R.string.sweeth7));
        listDataHeader.add(getString(R.string.sweeth8));
        listDataHeader.add(getString(R.string.sweeth9));

        // Adding child data
        List<String> introduction = new ArrayList<String>();
        introduction.add(getString(R.string.sweetb1));
        List<String> towhom = new ArrayList<String>();
        towhom.add(getString(R.string.sweetb2));
        List<String> components = new ArrayList<String>();
        components.add(getString(R.string.sweetb3));
        List<String> yourself = new ArrayList<String>();
        yourself.add(getString(R.string.sweetb4));
        List<String> Checks_For_Babies = new ArrayList<String>();
        Checks_For_Babies.add(getString(R.string.sweetb5));

        List<String> importance = new ArrayList<String>();
        importance.add(getString(R.string.sweetb6));

        List<String> exercise = new ArrayList<String>();
        exercise.add(getString(R.string.sweetb7));

        List<String> blood_spot = new ArrayList<String>();
        blood_spot.add(getString(R.string.sweetb8));
        List<String> ethiopia = new ArrayList<String>();
        ethiopia.add(getString(R.string.sweetb9));


        listDataChild.put(listDataHeader.get(0), introduction); // Header, Child data
        listDataChild.put(listDataHeader.get(1), towhom);
        listDataChild.put(listDataHeader.get(2), components);
        listDataChild.put(listDataHeader.get(3), yourself);
        listDataChild.put(listDataHeader.get(4), Checks_For_Babies);
        listDataChild.put(listDataHeader.get(5), importance);
        listDataChild.put(listDataHeader.get(6), exercise);
        listDataChild.put(listDataHeader.get(7), blood_spot);
        listDataChild.put(listDataHeader.get(8), ethiopia);


    }

    public void showpdf(String name) {
        // Override active layout
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.activity_pdfview    , null);
        // AlertDialog used for pop-Ups
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        // Used to link or get views in the dialogBox

        builder.setCancelable(false)
                // Used to set Negative button to cancel
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        dialogBox.cancel();
                    }
                });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        PDFView pdfView = alertDialog.findViewById(R.id.pdfView);
        pdfView.fromAsset(name).load();
    }




    public void shownovels() {
        // Override active layout
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.novels    , null);
        // AlertDialog used for pop-Ups
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        // Used to link or get views in the dialogBox

        builder.setCancelable(false)
                // Used to set Negative button to cancel
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        dialogBox.cancel();
                    }
                });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button donne = alertDialog.findViewById(R.id.donne);
        Button macbeth = alertDialog.findViewById(R.id.macbeth);
        Button sweetsixteen = alertDialog.findViewById(R.id.sweetsixteen);
        Button othello = alertDialog.findViewById(R.id.othello);

        donne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showpdf("done.pdf");
            }
        });
        othello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showpdf("othello.pdf");
            }
        });

        sweetsixteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showArticle();
            }
        });
        macbeth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showpdf("macbeth.pdf");
            }
        });
        }
}