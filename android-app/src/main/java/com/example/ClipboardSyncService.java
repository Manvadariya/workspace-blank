package com.example;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import javax.bluetooth.BluetoothAdapter;
import javax.bluetooth.BluetoothDevice;
import javax.bluetooth.BluetoothSocket;

public class ClipboardSyncService extends Service {

    private static final String TAG = "ClipboardSyncService";
    private ClipboardManager clipboardManager;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice bluetoothDevice;
    private BluetoothSocket bluetoothSocket;
    private OutputStream outputStream;

    @Override
    public void onCreate() {
        super.onCreate();
        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Log.e(TAG, "Bluetooth is not supported on this device");
            stopSelf();
            return;
        }

        clipboardManager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                ClipData clipData = clipboardManager.getPrimaryClip();
                if (clipData != null && clipData.getItemCount() > 0) {
                    CharSequence clipText = clipData.getItemAt(0).getText();
                    if (clipText != null) {
                        sendClipboardData(clipText.toString());
                    }
                }
            }
        });
    }

    private void sendClipboardData(String data) {
        if (bluetoothDevice == null) {
            // Replace with the MAC address of your Windows computer
            String deviceAddress = "00:11:22:33:44:55";
            bluetoothDevice = bluetoothAdapter.getRemoteDevice(deviceAddress);
        }

        if (bluetoothSocket == null) {
            try {
                bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(UUID.randomUUID());
                bluetoothSocket.connect();
                outputStream = bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error creating Bluetooth socket", e);
                return;
            }
        }

        try {
            outputStream.write(data.getBytes());
        } catch (IOException e) {
            Log.e(TAG, "Error sending clipboard data", e);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bluetoothSocket != null) {
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing Bluetooth socket", e);
            }
        }
    }
}
