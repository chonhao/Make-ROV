package com.rov.pcms.make_rov;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;


public class EditMotorAllocationProfiles extends ActionBarActivity {
//---------------------UI components init-----------------------------------
    public static final int MOTORS_AMOUNT = 8;
    private final int MOTORS_MOVEMENTS_AMOUNT = 6;
    private Button[] editMovements = new Button[MOTORS_MOVEMENTS_AMOUNT];
    private String[] editMovementsString = new String[MOTORS_MOVEMENTS_AMOUNT];

    private Dialog dialog;
    private Button[] outletStatusBtn = new Button[MOTORS_AMOUNT];
    private int[] outletStatusBtnContent = new int[MOTORS_AMOUNT];
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
    private String startActivityTAG;
//--------------------File writing values------------------------------------
    private String TAG = "File writing";
    private File fileList2[];
    private Context _context = EditMotorAllocationProfiles.this;
    private File extFile;
    private String absPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_motor_allocation_profiles);
//-----------------------Early Init-----------------------------------------
        Bundle bundle = getIntent().getExtras();
        prefrenceName = "Motor_Allocation";
        startActivityTAG = bundle.getString(MultiMotorAllocationActivity.FILE_NAME);
//-----------------------UI init-----------------------------------------------
        Toolbar toolbar = (Toolbar)findViewById(R.id.app_bar2);
        toolbar.setBackground(getResources().getDrawable(R.color.motor_act_primaryColor));
        setSupportActionBar(toolbar);
        setTitle("Custom Allocation");
        if(startActivityTAG.equals("Motor_Allocation")){
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        else if(startActivityTAG.equals("Motor_Allocation_SETUP")){
        }


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
        outletStatusBtn[6] = (Button)dialog.findViewById(R.id.outlet7StatusBtn);
        outletStatusBtn[7] = (Button)dialog.findViewById(R.id.outlet8StatusBtn);
        dialogPoositiveBtn = (Button)dialog.findViewById(R.id.saveChangesBtn);
        dialogNegativeBtn  = (Button)dialog.findViewById(R.id.discardBtn);
        outletStatusBtnContent[0]=1; outletStatusBtnContent[1]=1;
        outletStatusBtnContent[2]=1; outletStatusBtnContent[3]=1;
        outletStatusBtnContent[4]=1; outletStatusBtnContent[5]=1;
        outletStatusBtnContent[6]=1; outletStatusBtnContent[7]=1;
        dialogBtnLabel[1]="STOP";
        dialogBtnLabel[0]="POSITIVE";
        dialogBtnLabel[2]="NEGATIVE";
        //---------------------Dialog setup completed-------------------------------
//--------------------Additional setup----------------------------------------
        editMovementsString = new String[]{
            "FORWARD","RIGHT TURN","BACKWARD","LEFT TURN","UPWARD","DOWNWARD"
        };

        try {
            fileList2 = _context.getExternalFilesDirs(Environment.DIRECTORY_DOWNLOADS);
            extFile = fileList2[1];
        }catch (Exception e){
            Toast.makeText(this,"Can't find any External Storage. Files will be written to Internal Storage",Toast.LENGTH_LONG).show();
            extFile = Environment.getExternalStorageDirectory();
        }catch (NoSuchMethodError noSuchMethodError){
            Toast.makeText(this,"Devices under Android Kitkat do not support External Storage. Files will be written to Internal Storage",Toast.LENGTH_LONG).show();
            extFile = Environment.getExternalStorageDirectory();
        }
        try {
            absPath = extFile.getAbsolutePath();
        }catch(NullPointerException e){
            Log.i("Get Path error", "System can't ensure if external path exists");
            extFile = Environment.getExternalStorageDirectory();
            absPath = extFile.getAbsolutePath();
        }
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
                editor.apply();
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
        for(i=0;i<MOTORS_AMOUNT;i++){
            final int temp_n = i;
            outletStatusBtn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    outletStatusBtnContent[temp_n]++;
                    if(outletStatusBtnContent[temp_n]==3) outletStatusBtnContent[temp_n]=0;
                    outletStatusBtn[temp_n].setText(dialogBtnLabel[outletStatusBtnContent[temp_n]]);
                }
            });
        }
        for(int i =0;i<MOTORS_MOVEMENTS_AMOUNT;i++){
            final int temp_n = i;
            editMovements[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences = getSharedPreferences(prefrenceName,MODE_PRIVATE);
                    String getSavedContentString = sharedPreferences.getString(editMovementsString[temp_n],"[1, 1, 1, 1, 1, 1, 1, 1]");
                    String[] savedContentStringSplitted = getSavedContentString.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ","").split(",");
                    int[] savedcontentSplitted = new int[savedContentStringSplitted.length];
                    for (int i = 0; i < savedContentStringSplitted.length; i++) {
                        final int temp__i = i;
                        try {
                            savedcontentSplitted[temp__i] = Integer.parseInt(savedContentStringSplitted[temp__i]);
                        } catch (NumberFormatException nfe) {};
                    }
                    for(int i=0;i<MOTORS_AMOUNT;i++) {
                        outletStatusBtnContent[i] = savedcontentSplitted[i];
                        outletStatusBtn[i].setText(dialogBtnLabel[outletStatusBtnContent[i]]);
                    }
                    dialog.setTitle("Movement "+editMovementsString[temp_n]);
                    dialog.show();
                    currentMovement = temp_n;
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
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS|MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id == R.id.exportMotorProfile) {
            SharedPreferences sharedPreferences = getSharedPreferences(prefrenceName,MODE_PRIVATE);
            File file = new File(absPath,"Motor_Allocation"+".txt");
            FileOutputStream f = null;
            PrintWriter pw = null;
            try {
                f = new FileOutputStream(file);
                pw = new PrintWriter(f);
                pw.print(Integer.toString(MOTORS_AMOUNT));
                for(int i=0;i<MOTORS_MOVEMENTS_AMOUNT;i++){
                    final int tempi = i;
//                    Log.i("export " + tempi, sharedPreferences.getString(editMovementsString[tempi], "[1, 1, 1, 1, 1, 1, 1, 1]"));
                    pw.print(sharedPreferences.getString(editMovementsString[tempi], "[1, 1, 1, 1, 1, 1, 1, 1]").replace("[", "")
                            .replace("]", "").replace(" ", "").replace(",", ""));
                }
                for(int i=0;i<8;i++) {
                    pw.print("1");
                }
                pw.flush();
                pw.close();
                f.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Toast.makeText(this, "File exported to "+file.getPath(),Toast.LENGTH_LONG).show();
            if(startActivityTAG.equals("Motor_Allocation"))
                finish();
            else if(startActivityTAG.equals("Motor_Allocation_SETUP")){
                //nextPage
                SharedPreferences sharedPreferences1 = getSharedPreferences(BasicInformationActivity.ROV_BASIC_INFORMATION,MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences1.edit();
                editor.putString(BasicInformationActivity.FIRST_TIME_SETUP,"false").apply();
                startActivity(new Intent(EditMotorAllocationProfiles.this,SensorMoitor.class));


            }
        }



        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_to_right_close,R.anim.right_to_left_close);
        moveTaskToBack(false);
    }
    private String getCurrentMovementValueString(){
        return Arrays.toString(outletStatusBtnContent);
    }
}
