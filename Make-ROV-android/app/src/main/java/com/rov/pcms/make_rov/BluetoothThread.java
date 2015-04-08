package com.rov.pcms.make_rov;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

import android.bluetooth.*;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

public class BluetoothThread extends Thread{
    private static final UUID SerialPortServiceClass_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothSocket mmSocket;
    private BluetoothDevice mmDevice;

    private Queue<String> msgQueue; //string queue for storing message

    TextView myLabel;
    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;


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

    public void beginListenForData()
    {
        final Handler handler = new Handler();
        final byte delimiter = 10; //This is the ASCII code for a newline character

        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopWorker)
                {
                    try
                    {
                        int bytesAvailable = mmInputStream.available();
                        if(bytesAvailable > 0)
                        {
                            byte[] packetBytes = new byte[bytesAvailable];
                            mmInputStream.read(packetBytes);
                            for(int i=0;i<bytesAvailable;i++)
                            {
                                byte b = packetBytes[i];
                                if(b == delimiter)
                                {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    handler.post(new Runnable()
                                    {
                                        public void run()
                                        {
                                            //TODO: do something here

                                        }
                                    });
                                }
                                else
                                {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    }
                    catch (IOException ex)
                    {
                        stopWorker = true;
                    }
                }
            }
        });

        workerThread.start();
    }




}