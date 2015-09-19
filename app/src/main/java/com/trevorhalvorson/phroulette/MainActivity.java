package com.trevorhalvorson.phroulette;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Random;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {
    private static final String ENDPOINT = "https://api.producthunt.com/v1/";

    private API api;
    private Product product;
    private boolean isViewingProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNewProduct();
            }
        });

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

        getNewProduct();

    }

    public void getNewProduct() {
        isViewingProduct = true;
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
                            invalidateOptionsMenu();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
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
        if (isViewingProduct) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_load_discussion:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container,
                                WebViewFragment.newInstance(product.getDiscussion_url()))
                        .commit();
                return true;
            case R.id.action_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, product.getRedirect_url());
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.action_share)));
                return true;
            case R.id.action_product_web:
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(product.getRedirect_url())));
                return true;
            case R.id.action_discussion_web:
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(product.getDiscussion_url())));
                return true;
            case R.id.action_about:
                FragmentManager fragmentManager = getSupportFragmentManager();
                AboutFragment dialog = AboutFragment
                        .newInstance();
                dialog.show(fragmentManager, null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
