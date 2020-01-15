package info.kingpes.windowmanager;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    String a = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.window_manager_layout);

        Log.d("TAG", "Activity onCreate");

        a = getIntent().getAction();
        if (a != null) {
            requestPermission();
            Toast.makeText(this, a, Toast.LENGTH_SHORT).show();
        }

        findViewById(R.id.icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });
    }


    public void openYoutubeApp(String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            this.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            this.startActivity(webIntent);
        }
    }


    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(MainActivity.this)) {
            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent, 1000);
                    } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                        Toast.makeText(MainActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            };
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Windows Manager")
                    .setMessage("Cung capp quyen truy cap")
                    .setPositiveButton("Continue", listener)
                    .setNegativeButton("Cancel", listener)
                    .setCancelable(false)
                    .show();
        }else{
            openYoutubeApp(a);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startService(new Intent(MainActivity.this, WindowManagerService.class));
                    finish();
                }
            },10000);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(MainActivity.this)) {
                Toast.makeText(MainActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                openYoutubeApp(a);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startService(new Intent(MainActivity.this, WindowManagerService.class));
                        finish();
                    }
                },10000);
            }
        }
    }
}
