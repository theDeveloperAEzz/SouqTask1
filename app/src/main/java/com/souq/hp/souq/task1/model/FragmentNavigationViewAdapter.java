package com.souq.hp.souq.task1.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.souq.hp.souq.R;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by TheDeveloper on 31/01/2018.
 */

public class FragmentNavigationViewAdapter extends ArrayAdapter<ModelOne> {
    Context context;
    int resource;
    ArrayList<ModelOne> gridobjects;
    boolean saveLang;

    public FragmentNavigationViewAdapter(@NonNull Context context, @LayoutRes int resource,
                                         @NonNull ArrayList<ModelOne> gridobjects, boolean saveLang) {
        super(context, R.layout.item_home_recycle, gridobjects);
        this.context = context;
        this.resource = resource;
        this.gridobjects = gridobjects;
        this.saveLang = saveLang;
    }

    @SuppressLint({"CutPasteId", "SetTextI18n"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ImageView imageView;
        final TextView textViewTitle;
        final TextView textViewCount;
        final RelativeLayout layout;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_recycle_nav_fragment, null);
            imageView = convertView.findViewById(R.id.image2);
            textViewTitle = convertView.findViewById(R.id.text_title2);
            textViewCount = convertView.findViewById(R.id.text_coun2);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            imageView.setImageResource(R.drawable.image4);
            if (saveLang) {
                textViewTitle.setText(gridobjects.get(position).getTitleAR());
            } else {
                textViewTitle.setText(gridobjects.get(position).getTitleEN());
            }
            textViewCount.setText("(" + gridobjects.get(position).getProductCount() + ")");
            convertView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 300));

            convertView.setTag(imageView);
        } else {
            imageView = (ImageView) convertView.getTag();
        }
        if (isOnline()) {
            Picasso.with(getContext()).load(gridobjects.get(position).getPhoto()).into(imageView);
        } else {

//            // convert from base64 to bitmap made by eng  :D :D
//            Bitmap myBitmapAgain = decodeBase64(gridobjects.get(position).getIMAGE_DATA());
//            imageView.setImageBitmap(myBitmapAgain);
        }

        return convertView;
    }

    //convert from String to base64
    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    //chick internet methoud
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

}
