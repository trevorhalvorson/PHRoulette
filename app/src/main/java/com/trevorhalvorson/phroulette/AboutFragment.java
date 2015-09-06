package com.trevorhalvorson.phroulette;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Trevor on 9/6/2015.
 */
public class AboutFragment extends DialogFragment {

    public static AboutFragment newInstance() {

        return new AboutFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_about, null);

        TextView aboutTextView = (TextView) v.findViewById(R.id.about_text_view);
        aboutTextView.setText("PH Roulette is an open source ProductHunt.com product viewer.");

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("About")
                .setNegativeButton("GITHUB", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://github.com/trevorhalvorson/PHRoulette")));
                    }
                })
                .setPositiveButton("CLOSE", null)
                .create();
    }
}
