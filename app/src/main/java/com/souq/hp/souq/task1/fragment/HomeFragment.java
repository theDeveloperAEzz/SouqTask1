package com.souq.hp.souq.task1.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.souq.hp.souq.R;
import com.souq.hp.souq.task1.model.HomeAdapter;
import com.souq.hp.souq.task1.model.ModelOne;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Bidi;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class HomeFragment extends Fragment {
    View rootView;
    int categoryId = 0;
    String countryId = "&countryId=1";
    String url = "http://souq.hardtask.co/app/app.asmx/GetCategories?categoryId=" + categoryId + countryId;
    GridView gridView;
    HomeAdapter mAdapter;
    ArrayList<ModelOne> modelOneArrayList;
    Toolbar toolbar;
    String sub;
    String id;
    public static String title, idSub;
    boolean b;
    SharedPreferences langPreferences;
    SharedPreferences.Editor langPrefsEditor;
    public static boolean saveLang;

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @SuppressLint({"CommitPrefEdits", "WrongConstant"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        initView();
        if (isOnline()) {
            sharedPreferencesSaveLang();
            getDataAndSetupGridView(savedInstanceState);

        } else {
            Toast.makeText(getActivity(), "check connection", Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }

    @SuppressLint("WrongConstant")
    void sharedPreferencesSaveLang() {
        langPreferences = getActivity().getSharedPreferences("langPrefs", MODE_PRIVATE);
        langPrefsEditor = langPreferences.edit();
        saveLang = langPreferences.getBoolean("saveLang", false);
        if (saveLang) {
            gridView.setLayoutDirection(Bidi.DIRECTION_RIGHT_TO_LEFT);
        }
    }

    void getDataAndSetupGridView(final Bundle savedInstanceState) {
        modelOneArrayList = new ArrayList<>();
        mAdapter = new HomeAdapter(getContext(), R.layout.item_home_recycle, modelOneArrayList, saveLang);
        getData();
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id1) {
                final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment fragment = null;
                if (savedInstanceState == null) {
                    fragment = new FagmentNavigationView();
                    Bundle args = new Bundle();
                    if (saveLang) {
                        title = modelOneArrayList.get(position).getTitleAR();
                    } else {
                        title = modelOneArrayList.get(position).getTitleEN();
                    }
                    idSub = modelOneArrayList.get(position).getId();
                    args.putString("idKey", idSub);
                    args.putString("idTitle", title);
                    args.putBoolean("saveLang", saveLang);
                    fragment.setArguments(args);
                    ft.replace(R.id.container, fragment, "findThisFragment").addToBackStack(null).commit();
                }
            }
        });
    }

    void initView() {
        gridView = rootView.findViewById(R.id.recycler_home);
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar2);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar().setTitle("");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.main2, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.arabic_nav) {
            if (isOnline()) {
                setLocale("ar");
            }
            return true;
        } else if (id == R.id.english_nav) {
            if (isOnline()) {
                setLocale("en");
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        if (lang.equals("ar")) {
            langPrefsEditor.putBoolean("saveLang", true);
            langPrefsEditor.apply();

        } else {
            langPrefsEditor.clear();
            langPrefsEditor.apply();
        }
        final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment fragment = null;
        fragment = new HomeFragment();
        ft.replace(R.id.container, fragment, "findThisFragment").commit();


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
                                sub = jsonObject.getString("SubCategories");
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

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
