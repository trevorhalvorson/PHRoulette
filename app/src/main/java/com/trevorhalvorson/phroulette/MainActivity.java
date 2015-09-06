package com.trevorhalvorson.phroulette;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Random;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String ENDPOINT = "https://api.producthunt.com/v1/";
    public static final String API_KEY = "0d7c7b0716d03845c56d5f208096e92d229da39fef68f99d3fdbc4673a0fda86";
    public static final String API_SECRET = "845fbef880d2b38bb449925c58ad4ce3037d2c03ee8d82af0fcc2015be390a69";
    public static final String DEVELOPER_TOKEN = "22e4a0cfd1095acf974c87ff36e793f93ab6e4c218358036b2add75064ecf7ba";
    private API api;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);

        if (savedInstanceState == null) {
            Log.i(TAG, "onCreate ");
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(ENDPOINT)
                    .setRequestInterceptor(new RequestInterceptor() {
                        @Override
                        public void intercept(RequestFacade request) {
                            request.addHeader("Authorization", "Bearer " + DEVELOPER_TOKEN);
                        }
                    })
                    .build();

            api = restAdapter.create(API.class);
        }
    }

    private void getNewProduct() {
        api.getProduct(getRandomDay(), new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject jsonObject, Response response) {
                        if (jsonObject != null && jsonObject.has("posts")) {
                            JsonArray products = jsonObject.getAsJsonArray("posts");
                            Random random = new Random();
                            JsonObject object = products.get(random.nextInt(products.size())).getAsJsonObject();
                            product = new Product(object);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container, WebViewFragment
                                            .newInstance(product.getRedirect_url()))
                                    .commit();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.i(TAG, "error Message " + error.getMessage());
                    }
                }

        );
    }

    @NonNull
    private String getRandomDay() {
        Random random = new Random();
        return String.valueOf(random.nextInt((365 - 1) + 1) + 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new:
                getNewProduct();
                return true;
            case R.id.action_web:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(product.getRedirect_url())));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
