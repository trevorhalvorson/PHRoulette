package com.trevorhalvorson.phroulette;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

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
    private API api;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);

        ImageView kitty_button = (ImageView) findViewById(R.id.phkitty_button);
        kitty_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNewProduct();
            }
        });

        Animation hoverAnimation = AnimationUtils.loadAnimation(this,
                R.anim.hover_anim);
        kitty_button.setAnimation(hoverAnimation);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addHeader("Authorization", "Bearer " +
                                getResources().getString(R.string.token));
                    }
                })
                .build();

        api = restAdapter.create(API.class);

    }

    private void getNewProduct() {
        api.getProduct(getRandomDay(), new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject jsonObject, Response response) {
                        if (jsonObject != null && jsonObject.has("posts")) {
                            JsonArray products = jsonObject.getAsJsonArray("posts");
                            Random random = new Random();
                            JsonObject object = products
                                    .get(random.nextInt(products.size()))
                                    .getAsJsonObject();
                            product = new Product(object);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container,
                                            WebViewFragment.newInstance(product.getRedirect_url()))
                                    .commit();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.i(TAG, "error Message " + error.getMessage());
                        Snackbar.make(findViewById(R.id.container),
                                R.string.error,
                                Snackbar.LENGTH_LONG).show();
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
