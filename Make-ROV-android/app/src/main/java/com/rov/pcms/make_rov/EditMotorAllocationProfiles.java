package com.rov.pcms.make_rov;

import android.app.AlertDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class EditMotorAllocationProfiles extends ActionBarActivity {
//---------------------UI components init-----------------------------------
    private Button[] editModions;
    private String[] editModionsString;
//---------------------Shared Preference TAG values-------------------------
    private static final String EDIT_FORWARD = "edit-forward";
    private static final String EDIT_BACKWARD = "edit-backward";
    private static final String EDIT_LEFT_TURN = "edit-left-turn";
    private static final String EDIT_RIGHT_TURN = "edit-right-turn";
    private static final String EDIT_UPRWARD = "edit-upward";
    private static final String EDIT_DOWNWARD = "edit-downward";
//---------------------Additional values--------------------------------------
    private int i;
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

//        editModions[0] = (Button)findViewById(R.id.editMotorForwardBtn);
//        editModions[1] = (Button)findViewById(R.id.editMotorBackwardBtn);
//        editModions[2] = (Button)findViewById(R.id.editMotorLeftTurnBtn);
//        editModions[3] = (Button)findViewById(R.id.editMotorRightTurnBtn);
//        editModions[4] = (Button)findViewById(R.id.editMotorUpwardBtn);
//        editModions[5] = (Button)findViewById(R.id.editMotorDownwardBtn);
//--------------------Additional setup----------------------------------------
        editModionsString = new String[]{
            "FORWARD","BACKWARD","LEFT TURN","RIGHT TURN","UPWARD","DOWNWARD"
        };
//--------------------Component listeners-------------------------------------
//        for(i=0;i<6;i++){
//            editModions[i].setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    new AlertDialog.Builder(EditMotorAllocationProfiles.this)
//                            .setTitle("EDITING "+editModionsString[i]);
//
//
//                }
//            });
//        }
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
        }

        return super.onOptionsItemSelected(item);
    }
}
