package com.rov.pcms.make_rov;

import java.io.OutputStream;
import java.util.*;

import android.bluetooth.*;
import android.util.Log;

public class BluetoothThread extends Thread{
    private static final UUID SerialPortServiceClass_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothSocket mmSocket;
    private BluetoothDevice mmDevice;
    private Queue<String> msgQueue; //string queue for storing message

    BluetoothThread(BluetoothDevice device){
        mmSocket = null;
        mmDevice = device;

        msgQueue = new LinkedList<String>();

        BluetoothSocket tmp = null;
        try{
            tmp = device.createRfcommSocketToServiceRecord(SerialPortServiceClass_UUID);
        }catch(Exception e){ }

        mmSocket = tmp;
    }
    public BluetoothSocket getMmSocket(){
        return mmSocket;
    }
    public BluetoothDevice getMmDevice(){
        return mmDevice;
    }
    public void AddMessage(String s){
        msgQueue.offer(s);
        Log.i("sent", "sent");
    }
    @Override
    public void run(){
        try{
            mmSocket.connect();
            OutputStream out = mmSocket.getOutputStream();

            //send message via outputstream
            while(true){
                String tmpStr = msgQueue.poll(); //pop
                if(tmpStr != null) //has text in queue
                    out.write(tmpStr.getBytes());
            }
        }catch(Exception e){ e.printStackTrace(); }
    }
}