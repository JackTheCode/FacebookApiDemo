package com.example.phatnguyen.facebookapidemo.Activity.Activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Parcelable;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;
import com.example.phatnguyen.facebookapidemo.Activity.Adapter.LazyAdapter;
import com.example.phatnguyen.facebookapidemo.Activity.Interface.GetUserDataTaskDelegate;
import com.example.phatnguyen.facebookapidemo.Activity.Model.FaceBookUser;
import com.example.phatnguyen.facebookapidemo.Activity.Task.GetUserDataTask;
import com.example.phatnguyen.facebookapidemo.Activity.Utils.Constants;
import com.example.phatnguyen.facebookapidemo.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by phatnguyen on 10/18/17.
 */

public class MainActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private MaterialSearchView searchView;
    private ListView listView;
    private Parcelable state;
    private String queryString = "";
    private Integer offset = 0;
    private Boolean flag_loading = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        hashChecker();
        setUpFacebookLoginBtn();
        setUpListView();
        setUpSearchView();

    }

    public void setUpListView() {
        listView = (ListView)findViewById(R.id.list_user);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            public void onScrollStateChanged(AbsListView view, int scrollState) {


            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

//                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
//                        offset += 25;
//                        new GetUserDataTask(getApplicationContext(), new GetUserDataTaskDelegate() {
//                            @Override
//                            public void didReceivedData(final ArrayList<FaceBookUser> resultList) {
//
//                            }
//
//                            @Override
//                            public void didReceivedAdapter(final LazyAdapter adapter) {
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        adapter.notifyDataSetChanged();
//                                    }
//                                });
//                            }
//                        }).execute(queryString, String.valueOf(offset));
//                  }
              }
          });

    }

    public void hashChecker() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.phatnguyen.facebookapidemo",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
        }
    }

    public void setUpSearchView() {
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setVoiceSearch(false);
        searchView.setCursorDrawable(R.drawable.custom_cursor);
        searchView.setEllipsize(true);
//        searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                queryString = query;
                new GetUserDataTask(getApplicationContext(),new GetUserDataTaskDelegate() {
                   @Override
                   public void didReceivedData(final ArrayList<FaceBookUser> resultList) {

                   }
                    @Override
                    public void didReceivedAdapter(final LazyAdapter adapter) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listView.setAdapter(adapter);
                            }
                        });
                    }
                }).execute(query,String.valueOf(offset));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });
    }

    public void setUpFacebookLoginBtn() {
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {


            @Override
            public void onSuccess(LoginResult loginResult) {
                Constants.ACCESS_TOKEN = loginResult.getAccessToken();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }
            return;
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search:
                searchView.showSearch();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }
}
