package com.app.alc40;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.Objects;



public class WebFragment extends Fragment {
    private WebView web;
  private   ProgressDialog progressDialog;
   private SwipeRefreshLayout swipeRefreshLayout;

    @SuppressLint("SetJavaScriptEnabled")
    @JavascriptInterface
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.web_fragment, container, false);

        //HIDE TITLE BAR
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar().hide();




        web = view.findViewById(R.id.web);

        Load();


        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setDomStorageEnabled(true);
        web.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        web.setWebViewClient(new myWebViewClient());

       //Swipe to refresh
        swipeRefreshLayout = view.findViewById(R.id.swipe);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        web.loadUrl("https://andela.com/alc/");
                    }
                }, 8000);
            }
        });

        // Scheme colors for animation
        swipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );
        web.loadUrl("https://andela.com/alc/");
        return view;
    }


    private void Load() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("ALC 4.0");
        progressDialog.setIcon(R.drawable.ic_settings_black_24dp);
        progressDialog.setMessage("Loading ... Please wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    class myWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String request) {
            view.loadUrl(request);
            return true;
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                       SslError error) {
            handler.proceed();
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            web.loadUrl("file:///android_asset/lost.html");
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_LONG).show();

        }

    }

}
