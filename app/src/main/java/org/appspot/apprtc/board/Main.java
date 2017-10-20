package org.appspot.apprtc.board;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import org.appspot.apprtc.R;

import java.util.ArrayList;

/**
 * Created by kippe_000 on 2017-10-07.
 */

public class Main extends Fragment {
    Context context;
    ListView listView;
    ArrayList<Data> arrayList_item;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.board_main, container, false); 

        context = view.getContext();
        listView = (ListView)view.findViewById(R.id.listView);
        arrayList_item = new ArrayList<>();

        return view;
    }

    void Get_post(){



    }


    class ListViewAdapter extends BaseAdapter{
        Context context;

        @Override
        public int getCount() {
            return arrayList_item.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList_item.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.board_item,parent,false);
            }



            return convertView;
        }
    }

}
