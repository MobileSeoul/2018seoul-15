package com.sejongsoftware.seoulapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    String URL = "http://13.125.195.161";
    Handler handler = new Handler();
    final String chromePackageName = "com.android.chrome";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if( isAppInstalled( chromePackageName ) ) {

            Log.d("ChromeVersion : ", getAppVersion( chromePackageName ) + "");

            if( getAppVersion( chromePackageName ) < 349710001 )  {
                Toast.makeText(getApplicationContext(), "크롬을 업데이트 해주세요", Toast.LENGTH_SHORT).show();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + chromePackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + chromePackageName)));
                }

                finish();
            }
        } else {
            Toast.makeText(getApplicationContext(), "크롬을 설치하셔야 합니다", Toast.LENGTH_SHORT).show();
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + chromePackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + chromePackageName)));
            }

            finish();
        }

        webView = (WebView) findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(false);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDomStorageEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(true);


        webView.loadUrl(URL);
        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new JavascriptInterface(), "seoulApp");
        webView.setHorizontalScrollBarEnabled(false);
        webView.setVerticalScrollBarEnabled(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }

        if ((keyCode == KeyEvent.KEYCODE_BACK) && (webView.canGoBack() == false)){
            //다이아로그박스 출력
            new AlertDialog.Builder(this)
                    .setTitle("프로그램 종료")
                    .setMessage("Seoul App을 종료하시겠습니까?")
                    .setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    })
                    .setNegativeButton("아니오",  null).show();
        }

        return super.onKeyDown(keyCode, event);
    }


    final class JavascriptInterface {
        @android.webkit.JavascriptInterface  // 최근에는 이 어노테이션을 붙여줘야 동작하게 되어 있다..
        public void toggleBookmark(final String SVCID, final boolean isStarted){ // 반드시 final이어야 한다.
            handler.post(new Runnable() {
                @Override
                public void run() {
                    File file = new File(getFilesDir(), "user_bookmark.txt");
                    FileReader fr = null;
                    FileWriter fw = null;
                    BufferedReader br = null;
                    String data;
                    String before = "";
                    char ch;


                    if( file.exists() == true ) {
                        boolean flags = false;

                        try {
                            fr = new FileReader(file);
                            br = new BufferedReader(fr);

                            while ((data = br.readLine()) != null) {
                                if( data.equals(SVCID) ) {
                                    flags = true;
                                }
                                else {
                                    before += data + "\n";
                                }
                            }
                            Log.i("ch", "hello");
                            fr.close();

                            fw = new FileWriter(file);
                            if( flags )
                            {
                                fw.write(before);
                                webView.loadUrl("javascript:app.$children[0].$children[0].setIsBookmarked(false)");
                                if( isStarted ) {
                                    Toast.makeText(getApplicationContext(), "즐겨찾기가 해제되었습니다", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                fw.write(before + SVCID);
                                webView.loadUrl("javascript:app.$children[0].$children[0].setIsBookmarked(true)");
                                if( isStarted ) {
                                    Toast.makeText(getApplicationContext(), "즐겨찾기가 등록되었습니다", Toast.LENGTH_SHORT).show();
                                }
                            }
                            fw.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        try {
                            fw = new FileWriter(file);

                            fw.write(SVCID);

                            fw.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        @android.webkit.JavascriptInterface
        public void updateList() {
            BookmarkListTask bookmarkListTask = new BookmarkListTask();
            bookmarkListTask.execute();
        }

    }

    public class BookmarkListTask extends AsyncTask<Void, Void, Void>
    {
        ArrayList<String> svcids = new ArrayList();

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.i("svcids", svcids.toString());
            webView.loadUrl("javascript:app.$children[0].$children[0].updateBookmarkedList("+ new JSONArray(svcids) +")");

            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            File file = new File(getFilesDir(), "user_bookmark.txt");
            FileReader fr = null;

            try {
                fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                String str = "";

                while ( (str = br.readLine()) != null ) {
                    svcids.add(str);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    boolean isAppInstalled(String strAppPackage) {
        PackageManager pm = getPackageManager();
        PackageInfo pi;

        try {
            pi = pm.getPackageInfo(strAppPackage, PackageManager. GET_ACTIVITIES);
        }
        catch (PackageManager.NameNotFoundException e) {
            return false ;
        }

        return true ;
    }

    int getAppVersion(String strAppPackage) {
        PackageManager pm = getPackageManager();
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(strAppPackage, PackageManager. GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            return -1;
        }

        return pi.versionCode;
    }
}
