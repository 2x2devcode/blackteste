package org.bitcoincore.qt;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.system.ErrnoException;
import android.system.Os;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.qtproject.qt5.android.bindings.QtActivity;

import java.io.File;

public class BitcoinQtActivity extends QtActivity
{
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Request necessary permissions for modern Android
        checkAndRequestPermissions();

        // Create bitcoin directory
        final File bitcoinDir = new File(getFilesDir().getAbsolutePath() + "/.bitcoin");
        if (!bitcoinDir.exists()) {
            bitcoinDir.mkdirs();
        }
    }

    private void checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // For Android 13+ (API 33+), request new media permissions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                String[] permissions = {
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_AUDIO,
                    Manifest.permission.POST_NOTIFICATIONS
                };

                boolean needsPermission = false;
                for (String permission : permissions) {
                    if (ContextCompat.checkSelfPermission(this, permission)
                        != PackageManager.PERMISSION_GRANTED) {
                        needsPermission = true;
                        break;
                    }
                }

                if (needsPermission) {
                    ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
                }
            }
            // For Android 6.0 to 12 (API 23-32)
            else {
                String[] permissions = {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                };

                boolean needsPermission = false;
                for (String permission : permissions) {
                    if (ContextCompat.checkSelfPermission(this, permission)
                        != PackageManager.PERMISSION_GRANTED) {
                        needsPermission = true;
                        break;
                    }
                }

                if (needsPermission) {
                    ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                          int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            // Handle permission results if needed
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }

            if (!allGranted) {
                // Log or notify user that some permissions were denied
                // The app may have limited functionality
            }
        }
    }
}
