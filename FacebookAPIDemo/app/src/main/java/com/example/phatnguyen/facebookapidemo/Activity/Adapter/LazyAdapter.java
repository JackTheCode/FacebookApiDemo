package com.example.phatnguyen.facebookapidemo.Activity.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.phatnguyen.facebookapidemo.Activity.Model.FaceBookUser;
import com.example.phatnguyen.facebookapidemo.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Created by phatnguyen on 10/19/17.
 */

public class LazyAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<FaceBookUser> itemsList;
    private static LayoutInflater inflater=null;

    public LazyAdapter(Context context, ArrayList<FaceBookUser> l) {
        this.context = context;
        itemsList=l;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public int getCount() {
        return (itemsList == null) ? 0 : itemsList.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_user_item, null);
        TextView userName = vi.findViewById(R.id.userName);
        userName.setText(itemsList.get(position).getName());
        ImageView profilePicture = vi.findViewById(R.id.profilePicture);
        Picasso.with(context).load(itemsList.get(position).getUrl_image()).into(profilePicture);
        return vi;
    }
}
