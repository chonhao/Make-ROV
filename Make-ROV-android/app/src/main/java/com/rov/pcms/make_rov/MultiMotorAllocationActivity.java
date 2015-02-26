package com.rov.pcms.make_rov;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;


public class MultiMotorAllocationActivity extends ActionBarActivity {

    public static final int TO_EIDT_RESULT_CODE = 0x1001;
    public static final String FILE_NAME = "file-name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_motor_allocation);
//-----------------------UI init-----------------------------------------------
        Toolbar toolbar = (Toolbar)findViewById(R.id.app_bar2);
        toolbar.setBackground(getResources().getDrawable(R.color.motor_act_primaryColor));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        com.melnykov.fab.FloatingActionButton motorFab = (com.melnykov.fab.FloatingActionButton) findViewById(R.id.motorFab);
//--------------------Additional setup----------------------------------------
//--------------------Component listeners-------------------------------------
        motorFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                        ,LinearLayout.LayoutParams.MATCH_PARENT);
               final EditText adEditText = new EditText(MultiMotorAllocationActivity.this);
                       adEditText.setLayoutParams(layout);
                       adEditText.setHint("You Must Enter The File Name Here");
                       adEditText.setText("New_Pofile");
                       adEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                adEditText.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        InputMethodManager inputMethodManager= (InputMethodManager) MultiMotorAllocationActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                                        inputMethodManager.showSoftInput(adEditText, InputMethodManager.SHOW_IMPLICIT);
                                    }
                                });
                            }
                        });
                       adEditText.requestFocus();
                new AlertDialog.Builder(MultiMotorAllocationActivity.this)
                        .setTitle("New profile name")
                        .setView(adEditText)
                        .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String fileName = adEditText.getText().toString().trim();
                                if (fileName.isEmpty()||fileName.length()==0||fileName.equals("")|| TextUtils.isEmpty(fileName)) {
                                }else{
                                    Bundle bundle = new Bundle();
                                    bundle.putString(FILE_NAME, fileName);
                                    Intent intent = new Intent(MultiMotorAllocationActivity.this, EditMotorAllocationProfiles.class);
                                    intent.putExtras(bundle);
                                    startActivityForResult(intent, TO_EIDT_RESULT_CODE);
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_multi_motor_allocation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
