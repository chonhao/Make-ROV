package com.rov.pcms.make_rov;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;

import static com.rov.pcms.make_rov.R.id.editMotorForwardBtn;
import static com.rov.pcms.make_rov.R.id.outlet1StatusBtn;


public class EditMotorAllocationProfiles extends ActionBarActivity {
//---------------------UI components init-----------------------------------
    private Button[] editMovements = new Button[6];
    private String[] editMovementsString = new String[6];

    private Dialog dialog;
    private Button[] outletStatusBtn = new Button[6];
    private int[] outletStatusBtnContent = new int[6];
    private String[] dialogBtnLabel = new String[3];
    private Button dialogPoositiveBtn,dialogNegativeBtn;
//---------------------Shared Preference TAG values-------------------------
    private static final String EDIT_FORWARD = "edit-forward";
    private static final String EDIT_BACKWARD = "edit-backward";
    private static final String EDIT_LEFT_TURN = "edit-left-turn";
    private static final String EDIT_RIGHT_TURN = "edit-right-turn";
    private static final String EDIT_UPRWARD = "edit-upward";
    private static final String EDIT_DOWNWARD = "edit-downward";
//---------------------Additional values--------------------------------------
    private int i;
    private String prefrenceName;
    private int currentMovement;
    private String currentMovementString;
//--------------------File writing values------------------------------------
    private String TAG = "File writing";
    private File fileList2[];
    Context _context = EditMotorAllocationProfiles.this;
    File extFile;
    String absPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_motor_allocation_profiles);
//-----------------------UI init-----------------------------------------------
        Toolbar toolbar = (Toolbar)findViewById(R.id.app_bar2);
        toolbar.setBackground(getResources().getDrawable(R.color.motor_act_primaryColor));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editMovements[0] = (Button)findViewById(R.id.editMotorForwardBtn);
        editMovements[1] = (Button)findViewById(R.id.editMotorBackwardBtn);
        editMovements[2] = (Button)findViewById(R.id.editMotorLeftTurnBtn);
        editMovements[3] = (Button)findViewById(R.id.editMotorRightTurnBtn);
        editMovements[4] = (Button)findViewById(R.id.editMotorUpwardBtn);
        editMovements[5] = (Button)findViewById(R.id.editMotorDownwardBtn);
        //---------------------Dialog setup---------------------------------
        dialog = new Dialog(EditMotorAllocationProfiles.this);
        dialog.setContentView(R.layout.alertdialog_edit_motor_profile);
        outletStatusBtn[0] = (Button)dialog.findViewById(R.id.outlet1StatusBtn);
        outletStatusBtn[1] = (Button)dialog.findViewById(R.id.outlet2StatusBtn);
        outletStatusBtn[2] = (Button)dialog.findViewById(R.id.outlet3StatusBtn);
        outletStatusBtn[3] = (Button)dialog.findViewById(R.id.outlet4StatusBtn);
        outletStatusBtn[4] = (Button)dialog.findViewById(R.id.outlet5StatusBtn);
        outletStatusBtn[5] = (Button)dialog.findViewById(R.id.outlet6StatusBtn);
        dialogPoositiveBtn = (Button)dialog.findViewById(R.id.saveChangesBtn);
        dialogNegativeBtn  = (Button)dialog.findViewById(R.id.discardBtn);
        outletStatusBtnContent[0]=0; outletStatusBtnContent[1]=0;
        outletStatusBtnContent[2]=0; outletStatusBtnContent[3]=0;
        outletStatusBtnContent[4]=0; outletStatusBtnContent[5]=0;
        dialogBtnLabel[0]="STOP";
        dialogBtnLabel[1]="POSITIVE";
        dialogBtnLabel[2]="NEGATIVE";
        //---------------------Dialog setup completed-------------------------------
//--------------------Additional setup----------------------------------------
        editMovementsString = new String[]{
            "FORWARD","BACKWARD","LEFT TURN","RIGHT TURN","UPWARD","DOWNWARD"
        };
        Bundle bundle = getIntent().getExtras();
        prefrenceName = bundle.getString(MultiMotorAllocationActivity.FILE_NAME);
        try {
            fileList2 = _context.getExternalFilesDirs(Environment.DIRECTORY_DOWNLOADS);
            extFile = fileList2[1];
        }catch (Exception e){
            Toast.makeText(this,"Can't find any External Storage. Files will be written to Internal Storage",Toast.LENGTH_LONG).show();
            extFile = Environment.getExternalStorageDirectory();
        }catch (NoSuchMethodError noSuchMethodError){
            Toast.makeText(this,"Devices under Kitkat do not support External Storage. Files will be written to Internal Storage",Toast.LENGTH_LONG).show();
            extFile = Environment.getExternalStorageDirectory();
        }
        absPath = extFile.getAbsolutePath();
//        if(fileList2.length == 1) {
//            Log.d(TAG, "external device is not mounted.");
//        } else {
//            Log.d(TAG, "external device is mounted.");
//            Log.d(TAG, "external device download : " + absPath);
//        }

//--------------------Component listeners-------------------------------------
        dialogPoositiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentMovementString = getCurrentMovementValueString();
                SharedPreferences editMotorPreference = getSharedPreferences(prefrenceName, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = editMotorPreference.edit();
                editor.putString(editMovementsString[currentMovement],currentMovementString);
                editor.commit();
                dialog.dismiss();
//              -----------debug-------------------
//                Log.i(editMovementsString[currentMovement], currentMovementString);
//                Toast.makeText(EditMotorAllocationProfiles.this,currentMovementString,Toast.LENGTH_LONG).show();
//                SharedPreferences sp = getSharedPreferences(prefrenceName, Context.MODE_PRIVATE);
//                Log.i("preference content",sp.getString(editMovementsString[currentMovement],""));
//                Toast.makeText(EditMotorAllocationProfiles.this,sp.getString(editMovementsString[currentMovement],"nothing"),Toast.LENGTH_LONG).show();
                currentMovement = -1;
            }
        });
        dialogNegativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                currentMovement = -1;
            }
        });
        for(i=0;i<6;i++){
            final int temp_n = i;
            editMovements[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences = getSharedPreferences(prefrenceName,MODE_PRIVATE);
                    String getSavedContentString = sharedPreferences.getString(editMovementsString[temp_n],"[0, 0, 0, 0, 0, 0]");
                    String[] savedContentStringSplitted = getSavedContentString.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ","").split(",");
                    int[] savedcontentSplitted = new int[savedContentStringSplitted.length];
                    for (int i = 0; i < savedContentStringSplitted.length; i++) {
                        final int temp__i = i;
                        try {
                            savedcontentSplitted[temp__i] = Integer.parseInt(savedContentStringSplitted[temp__i]);
                        } catch (NumberFormatException nfe) {};
                    }
                    for(int i=0;i<6;i++) {
                        outletStatusBtnContent[i] = savedcontentSplitted[i];
                        outletStatusBtn[i].setText(dialogBtnLabel[outletStatusBtnContent[i]]);
                    }
                    dialog.setTitle("Movement "+editMovementsString[temp_n]);
                    dialog.show();
                    currentMovement = temp_n;
                }
            });

            outletStatusBtn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    outletStatusBtnContent[temp_n]++;
                    if(outletStatusBtnContent[temp_n]==3) outletStatusBtnContent[temp_n]=0;
                    outletStatusBtn[temp_n].setText(dialogBtnLabel[outletStatusBtnContent[temp_n]]);
                }
            });
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_motor_allocation_profiles, menu);
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
        }else if(id == R.id.exportMotorProfile) {
            SharedPreferences sharedPreferences = getSharedPreferences(prefrenceName,MODE_PRIVATE);
            File file = new File(absPath,prefrenceName+".txt");
            FileOutputStream f = null;
            PrintWriter pw = null;
            try {
                f = new FileOutputStream(file);
                pw = new PrintWriter(f);
                for(int i=0;i<6;i++){
                    final int tempi = i;
//                    Log.i("export " + tempi, sharedPreferences.getString(editMovementsString[tempi], "[0, 0, 0, 0, 0, 0]"));
                    pw.println(sharedPreferences.getString(editMovementsString[tempi], "[0, 0, 0, 0, 0, 0]").replace("[","")
                            .replace("]","").replace(" ","").replace(",",""));
                }
                pw.flush();
                pw.close();
                f.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Toast.makeText(this, "File exported to "+file.getPath(),Toast.LENGTH_LONG).show();
        }



        return super.onOptionsItemSelected(item);
    }

    private String getCurrentMovementValueString(){
        return Arrays.toString(outletStatusBtnContent);
    }
}
