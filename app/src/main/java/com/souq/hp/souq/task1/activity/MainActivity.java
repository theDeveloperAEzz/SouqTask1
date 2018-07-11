package com.souq.hp.souq.task1.activity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.support.v7.app.AppCompatActivity;

import com.souq.hp.souq.R;
import com.souq.hp.souq.task1.fragment.FagmentNavigationView;
import com.souq.hp.souq.task1.fragment.HomeFragment;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = null;
        if (savedInstanceState == null) {
            fragment = new HomeFragment();
            ft.add(R.id.container, fragment).commit();
        }
    }


    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
//        Intent refresh = new Intent(this, );
//        startActivity(refresh);
//        finish();
    }
}
