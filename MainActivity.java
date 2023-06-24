package com.yawpos.earl_hostel_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ImageSlider mainslider;
    CardView news,price,near,one,comm,videos,vacancy,girls,self;
    NestedScrollView scrollView;
    RecyclerView recviewhostel;
    hosteladapter adapter;
    EditText et_search;
    BottomNavigationView mBottomNavigation;
    TabLayout tabLayout;
    ViewPager viewPager;
    RecyclerView move;
    FirebaseRemoteConfig remoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        move = findViewById(R.id.recviewhostel);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        } catch (DatabaseException e) {
            // Handle the exception, if necessary
            Log.e("Firebase", "Error enabling offline persistence: " + e.getMessage());
        }



        FirebaseMessaging.getInstance().subscribeToTopic("Dean")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Done";
                        if (!task.isSuccessful()) {
                            msg = "failed";
                        }

                    }
                });

        mBottomNavigation =(BottomNavigationView) findViewById(R.id.bottom_navigation);
        mBottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.Hostel:
                        int lastIndex = recviewhostel.getAdapter().getItemCount() - 1;
                        recviewhostel.smoothScrollToPosition(lastIndex);
                        break;

                    case R.id.INFORMATION:
                        Intent intent= new Intent(MainActivity.this, Dean_Catalyst.class);
                        startActivity(intent);
                        break;
                    case R.id.CONTACT:
                        Intent intent3= new Intent(MainActivity.this, Contact2.class);
                        startActivity(intent3);
                        break;
                }

                return true;
            }
        });

        recviewhostel=(RecyclerView) findViewById(R.id.recviewhostel);
        recviewhostel.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<model> options=
                new FirebaseRecyclerOptions.Builder<model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Dean Hostel"),model.class)
                        .build();
        adapter=new hosteladapter(options);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recviewhostel.setLayoutManager(layoutManager);
        recviewhostel.setAdapter(adapter);

        long startTime = System.currentTimeMillis();
        final long threshold = 3000; // 2 minutes

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                long endTime = System.currentTimeMillis();
                long elapsedTime = endTime - startTime;

                if (elapsedTime > threshold) {
                    // Images took too long to load, indicating a poor internet connection
                    Toast.makeText(getApplicationContext(), "Images took too long to load. Poor internet connection.", Toast.LENGTH_LONG).show();
                }

                // Unregister the observer after the initial data has been loaded
                adapter.unregisterAdapterDataObserver(this);
            }
        });


        news=findViewById(R.id.news);
        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,New.class);
                startActivity(intent);
            }
        });
        price=findViewById(R.id.prices);
        price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Price.class);
                startActivity(intent);
            }
        });
        one=findViewById(R.id.one);
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,One.class);
                startActivity(intent);
            }
        });
        self=findViewById(R.id.self);
        self.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Self.class);
                startActivity(intent);
            }
        });
        vacancy=findViewById(R.id.vacancy);
        vacancy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Vacancy.class);
                startActivity(intent);
            }
        });
        near=findViewById(R.id.near);
        near.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Near.class);
                startActivity(intent);
            }
        });
        videos=findViewById(R.id.videos);
        videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Videos.class);
                startActivity(intent);
            }
        });
        girls=findViewById(R.id.girls);
        girls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Girls.class);
                startActivity(intent);
            }
        });

        comm=findViewById(R.id.communities);
        comm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Comm.class);
                startActivity(intent);
            }
        });
        int currentVersionCode;
        currentVersionCode=getCurrentVersionCode();
        Log.d("myApp4",String.valueOf(currentVersionCode));
        remoteConfig=FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings=new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(5)
                .build();
        remoteConfig.setConfigSettingsAsync(configSettings);
        remoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()){
                    final String new_version_code=remoteConfig.getString("new_version_codeeeeeeee");
                    if (Integer.parseInt(new_version_code)>getCurrentVersionCode()){
                        showUpdateDialog();
                    }
                }
            }
        });
        mainslider=(ImageSlider) findViewById(R.id.image_slider);

        final List<SlideModel> remoteimages=new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("Sliding")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                        for (DataSnapshot data:datasnapshot.getChildren() )

                            remoteimages.add(new SlideModel(data.child("url").getValue().toString(),data.child("title").getValue().toString(), ScaleTypes.FIT));
                        mainslider.setImageList(remoteimages, ScaleTypes.FIT);
                        mainslider.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onItemSelected(int i) {

                                Intent intent=new Intent(MainActivity.this,Slider.class);
                                startActivity(intent);
                            }
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
        tabLayout=findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Hostels"));



    }

    private int getCurrentVersionCode() {
        PackageInfo packageInfo=null;
        try {
            packageInfo=getPackageManager().getPackageInfo(getPackageName(), 0);

        }catch (Exception e){
            Log.d("myApp4",e.getMessage());
        }
        return packageInfo.versionCode;
    }

    private void showUpdateDialog() {
        final AlertDialog dialog=new AlertDialog.Builder(this)
                .setTitle("New Update Available")
                .setMessage("Update Now")
                .setPositiveButton("update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.yawpos.earl_hostel_app")));
                        }catch (Exception e){
                            Toast.makeText(MainActivity.this, "try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .show();
        dialog.setCancelable(false);

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("lastVisiblePosition", ((LinearLayoutManager) recviewhostel.getLayoutManager()).findFirstVisibleItemPosition());
        editor.apply();
    }

    private void processsearch(String s) {

        FirebaseRecyclerOptions<model>options=
                new FirebaseRecyclerOptions.Builder<model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Dean Hostel").startAt(s).endAt(s+"\uf8ff"),model.class)
                        .build();
        adapter=new hosteladapter(options);
        adapter.startListening();

        recviewhostel.setAdapter(adapter);
    }
    @Override
    public void onBackPressed() {
        finishAffinity();
    }

}
