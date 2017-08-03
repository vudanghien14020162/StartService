package com.example.hien.servicedownload;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String link
            = "http://afamily1.mediacdn.vn/k:thumb_w/600/VKefFccccccccccccSZQX6rFO6Qp9D/Image/2016/12/lan-huong-16133/cac-hotgirl-mat-tron.jpg";

    private static final String TAG = MainActivity.class.getSimpleName();

    private Intent intentService;

    private ImageView imgDownload;

    private MyBroadCast mMyBroadCast;

    @RequiresApi(api = 26)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewByIds();

        initComponents();

        setEvents();

        registerAction();
    }

    //nhan action
    @RequiresApi(api = 26)
    private void registerAction(){
        mMyBroadCast = new MyBroadCast();

        IntentFilter filter = new IntentFilter();

        filter.addAction(Cons.ACTION_BROADCAST);

//        registerReceiver(mMyBroadCast, filter, 1);
        registerReceiver(mMyBroadCast, filter);

    }

    private void findViewByIds() {
        imgDownload = (ImageView) findViewById(R.id.img_download);

    }

    private void initComponents() {

    }

    private void setEvents() {
        findViewById(R.id.btn_download).setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {

        intentService = new Intent(MainActivity.this, ServiceStartDow.class);

        Bundle bundle = new Bundle();

        bundle.putString("link", link);

        intentService.putExtras(bundle);

        startService(intentService);
    }

    private class MyBroadCast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            switch (action){
                case Cons.ACTION_BROADCAST:

                    String path = intent.getStringExtra("path");

                    Log.d(TAG, "Path File: " + path);

                    //dieu chinh anh
                    BitmapFactory.Options options = new BitmapFactory.Options();

                    ////tra ve bitmap la null neu inJustDecodeBounds = true
                    options.inJustDecodeBounds = true;

                    BitmapFactory.decodeFile(path, options);

                    int height_real = options.outHeight;
                    int width_real = options.outWidth;

                    BitmapFactory.Options option = new BitmapFactory.Options();

                    option.inJustDecodeBounds = false;

                    int imgWant = (height_real * width_real) / (254 * 154);

                    option.inSampleSize = imgWant;

                    Bitmap bitmap = BitmapFactory.decodeFile(path, option);


                    imgDownload.setImageBitmap(bitmap);

                    break;
                default: break;
            }
        }
    }



    @Override
    protected void onPause() {
        super.onPause();

        if(intentService != null){
            stopService(intentService);
        }


    }

    @Override
    protected void onStop() {
        super.onStop();

        if(intentService != null){
            stopService(intentService);
        }


    }

    @Override
    protected void onDestroy() {

        //huy nhan broadCast
        unregisterReceiver(mMyBroadCast);

        if(intentService != null){
            //huy service
            stopService(intentService);
        }


        super.onDestroy();
    }
}
