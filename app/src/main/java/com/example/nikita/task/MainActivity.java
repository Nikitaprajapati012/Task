package com.example.nikita.task;

import android.net.ConnectivityManager;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class MainActivity extends AppCompatActivity {
    boolean exists = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        isInternetOn();
        String ipAddress = "192.168.1.215";
        ServerSocket s = null;
        try {
            s = new ServerSocket(0);
            int port = s.getLocalPort();
            isHostAvailable(ipAddress, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void isHostAvailable(String ip, int port) {
        Socket sock = null;
        try {
            SocketAddress sockaddr = new InetSocketAddress(ip, port);
            sock = new Socket();
            int timeoutMs = 2000;   // 2 seconds
            sock.connect(sockaddr, timeoutMs);
            exists = true;
            Log.d("SUCESS", "@@....This ...." + "PORT==>" + port + " is available");
            Log.d("IPSOCKET", "@@" + sockaddr + "---PORT" + port + "---TIMEOUT" + timeoutMs);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("FAILED", "@@....This ...." + "---PORT" + port + "is not available");
            Log.d("IOException", "@@" + e.toString());
        } finally {
            if (sock != null) {
                try {
                    sock.close();
                } catch (IOException e) {
                    Log.d("FINALLY", "@@" + e.toString());
                    throw new RuntimeException("You should handle this error.", e);
                }
            }
        }
    }

    public final boolean isInternetOn() {
        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            // if connected with internet
            Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;
        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }

}
