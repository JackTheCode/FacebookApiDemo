package com.example.phatnguyen.facebookapidemo.Activity.Task;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.phatnguyen.facebookapidemo.Activity.Adapter.LazyAdapter;
import com.example.phatnguyen.facebookapidemo.Activity.Interface.GetUserDataTaskDelegate;
import com.example.phatnguyen.facebookapidemo.Activity.Model.FaceBookUser;
import com.example.phatnguyen.facebookapidemo.Activity.Utils.Constants;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by phatnguyen on 10/19/17.
 */

public class GetUserDataTask extends AsyncTask<String,Void,Void> {
    public GraphRequest graphRequest;
    public GraphResponse graphResponse;
    public ArrayList<FaceBookUser> resultList = new ArrayList<>();
    public URL profilePicture;
    public Bundle params = new Bundle();
    public GetUserDataTaskDelegate delegate = null;
    Context context;

    public GetUserDataTask(Context context,GetUserDataTaskDelegate delegate) {
        this.context = context;
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Void doInBackground(String...query) {
        if(!query[0].equals("")){
            params.putString("type", "user");
            params.putString("q", query[0]);
            params.putString("offset",query[1]);
            graphRequest = new GraphRequest(Constants.ACCESS_TOKEN, "/search", params, HttpMethod.GET);
            graphResponse = graphRequest.executeAndWait();
            try {
                JSONArray jsonFirst = graphResponse.getJSONObject().getJSONArray("data");
                if (jsonFirst != null) {
                    resultList.clear();
                    for (int i = 0; i < jsonFirst.length(); i++) {
                        JSONObject first = jsonFirst.getJSONObject(i);
                        if (first.has("name")) {
                            String name = first.getString("name");
                            String id = first.getString("id");
                            try {
                                profilePicture = new URL("https://graph.facebook.com/" + id + "/picture?width=150&height=150");
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                            FaceBookUser a = new FaceBookUser(name, id, profilePicture.toString());
                            resultList.add(a);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            delegate.didReceivedData(resultList);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
           LazyAdapter adapter = new LazyAdapter(context,resultList);
           delegate.didReceivedAdapter(adapter);
        }

}
