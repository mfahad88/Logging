package com.example.fahad.logging;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public final class MyAppLogReader {

    private static final String TAG = MyAppLogReader.class.getCanonicalName();
    private static final String processId = Integer.toString(android.os.Process
            .myPid());

    public static void getLog(final TextView tv) {
       new Thread(new Runnable() {
           @Override
           public void run() {
               StringBuilder builder = new StringBuilder();

               try {
                   String[] command = new String[] { "logcat", "-d", "-v", "threadtime" };

                   Process process = Runtime.getRuntime().exec(command);

                   BufferedReader bufferedReader = new BufferedReader(
                           new InputStreamReader(process.getInputStream()));

                   String line;
                   while ((line = bufferedReader.readLine()) != null) {
                       if (line.contains(processId)) {
                           builder.append(line+"\n");
                           //Code here
                       }
                   }
               } catch (IOException ex) {
                   Log.e(TAG, "getLog failed", ex);
               }
               tv.append(builder.toString());
               sendMessage(builder.toString());
               //return builder;
           }
       }).start();
    }

    private static void sendMessage(String msg){
        DatagramSocket clientSocket = null;

        try {
            clientSocket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName("192.168.100.7");
            byte[] sendData = new byte[1024];
            String sentence = msg;
            sendData = sentence.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 41234);
            clientSocket.send(sendPacket);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            clientSocket.close();
        }


    }
}