package info.kingpes.windowmanager;

import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WindowManagerService extends Service {
    private WindowManager windowManager;
    private RelativeLayout icon;
    private WindowManager.LayoutParams layoutParams;

    private  String[] videos = {"J1VzE0S-jzk","JAhdeizXpaQ","DZDYZ9nRHfU","cBClD7jylos","1juIFmPyG-Y"};
    private static int index;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initView();
        return START_STICKY;
    }

    private void initView() {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        createIcon();


        showIcon();
    }

    private void showIcon() {
        windowManager.addView(icon, layoutParams);
    }

    private void createIcon() {
        icon = new RelativeLayout(this);
        View view = View.inflate(this, R.layout.window_manager_layout, icon);
        TextView textView = view.findViewById(R.id.icon);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index < videos.length) {
                    openApp(videos[index]);
                }
            }
        });


        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                //.LayoutParams.FLAG_NOT_TOUCHABLE |
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN ,
                        //WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

    }

    private void openApp(String id){
        Intent intent = new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(id);
        startActivity(intent);
        index++;
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("TAG", "Service onDestroy");
        windowManager.removeView(icon);
    }
}
