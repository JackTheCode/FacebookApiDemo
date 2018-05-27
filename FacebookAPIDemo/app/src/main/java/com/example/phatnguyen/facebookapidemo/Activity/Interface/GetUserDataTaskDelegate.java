package com.example.phatnguyen.facebookapidemo.Activity.Interface;

import android.widget.ListAdapter;

import com.example.phatnguyen.facebookapidemo.Activity.Adapter.LazyAdapter;
import com.example.phatnguyen.facebookapidemo.Activity.Model.FaceBookUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phatnguyen on 10/19/17.
 */



public interface GetUserDataTaskDelegate {
    void didReceivedData(ArrayList<FaceBookUser> resultList);
    void didReceivedAdapter(LazyAdapter adapter);
}