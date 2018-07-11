package com.souq.hp.souq.task1.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.souq.hp.souq.R;
import com.souq.hp.souq.task1.model.FragmentNavigationViewAdapter;
import com.souq.hp.souq.task1.model.ModelOne;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Bidi;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class FagmentNavigationView extends Fragment
        implements NavigationView.OnNavigationItemSelectedListener {
    View rootView;
    ImageView imageViewIcon, imageViewSub;
    TextView textViewToolbar, textViewTitleSub, textViewCountSub;
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    String categoryId;
    String title;
    String countryId = "&countryId=1";
    String url;
    String id;
    String sub;
    GridView gridView;
    FragmentNavigationViewAdapter mAdapter;
    ArrayList<ModelOne> modelOneArrayList;
    Map subCatMap;
    boolean saveLang;
    SharedPreferences langPreferences;
    SharedPreferences.Editor langPrefsEditor;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @SuppressLint({"WrongConstant", "CommitPrefEdits"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_drawar, container, false);
        initView();
        langPreferences = getActivity().getSharedPreferences("langPrefs", MODE_PRIVATE);
        langPrefsEditor = langPreferences.edit();
        saveLang = new HomeFragment().saveLang;
        if (saveLang) {
            gridView.setLayoutDirection(Bidi.DIRECTION_RIGHT_TO_LEFT);
        }

        title = new HomeFragment().title;
        textViewToolbar.setText(title);
        categoryId = new HomeFragment().idSub;
        url = "http://souq.hardtask.co/app/app.asmx/GetCategories?categoryId=" + categoryId + countryId;
        modelOneArrayList = new ArrayList<>();
        mAdapter = new FragmentNavigationViewAdapter(getContext(), R.layout.item_recycle_nav_fragment, modelOneArrayList, saveLang);
        getData();
        imageViewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment fragment = null;
                if (savedInstanceState == null) {
                    fragment = new HomeFragment();
                    ft.replace(R.id.container, fragment, "findThisFragment").commit();
                }
            }
        });
        gridView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.main2, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    void initView() {
        textViewToolbar = rootView.findViewById(R.id.toolbar_title);
        imageViewIcon = rootView.findViewById(R.id.icon_back);
        if (Locale.getDefault().getDisplayLanguage().equals("English")) {
            imageViewIcon.setImageResource(R.drawable.top_bar_back_en);
        } else {
            imageViewIcon.setImageResource(R.drawable.top_bar_back);

        }
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar().setTitle("");

        drawer = (DrawerLayout) rootView.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) rootView.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        gridView = rootView.findViewById(R.id.recycler_navigation);
    }

    void getData() {
        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int x = 0; x < response.length(); x++) {
                                JSONObject jsonObject = response.getJSONObject(x);
                                id = jsonObject.getString("Id");
                                String titleEN = jsonObject.getString("TitleEN");
                                String titleAR = jsonObject.getString("TitleAR");
                                String photo = jsonObject.getString("Photo");
                                String productCount = jsonObject.getString("ProductCount");
                                String haveModel = jsonObject.getString("HaveModel");
//                                sub = jsonObject.getString("SubCategories");
//                                if (sub != null) {
//                                    subCatMap.put(id, sub);
//                                }
                                ModelOne countryInfo = new ModelOne(id, titleEN, titleAR, photo, productCount, haveModel);
                                modelOneArrayList.add(countryInfo);
                                mAdapter.notifyDataSetChanged();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(getContext()).add(jsonArrayRequest);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_share) {

//            setLocale("ar");

        } else if (id == R.id.nav_send) {
//            setLocale("en");
        }

        DrawerLayout drawer = (DrawerLayout) rootView.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        saveLang = langPreferences.getBoolean("saveLang", false);
        if (lang.equals("ar")) {
            langPrefsEditor.putBoolean("saveLang", true);
            langPrefsEditor.apply();

        } else {
            langPrefsEditor.clear();
            langPrefsEditor.apply();
        }
        final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment fragment = null;
        fragment = new FagmentNavigationView();
        ft.replace(R.id.container, fragment, "findThisFragment").commit();


    }

}
