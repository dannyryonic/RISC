package com.truiton.rtmpplayer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Danny on 2018/01/22.
 */

public class Ip_Dialog extends AppCompatDialogFragment {
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    private EditText mName;
    private Button btnLogin;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater= getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.ip_dialog,null);
        mName = (EditText) view.findViewById(R.id.etName);


        mPreferences = PreferenceManager.getDefaultSharedPreferences(Ip_Dialog.this.getActivity());
        //mPreferences = getSharedPreferences("tabian.com.sharedpreferencestest", Context.MODE_PRIVATE);

        checkSharedPreferences();
        mEditor = mPreferences.edit();
        builder.setView(view)
                .setTitle("Enter IP Address")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })

              .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {

                      //save the name
                      String name = mName.getText().toString();
                      mEditor.putString(getString(R.string.name), name);
                      mEditor.commit();
                      Intent intent = new Intent(Ip_Dialog.this.getActivity(),Front_Cam.class);
                      startActivity(intent);
                      System.exit(0);
                  }
              });

        return builder.create();

    }
    /**
     * Check the shared preferences and set them accordingly
     */
    private void checkSharedPreferences(){
        String name = mPreferences.getString(getString(R.string.name), "");

        mName.setText(name);



    }
}
