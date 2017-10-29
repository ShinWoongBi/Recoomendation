package org.appspot.apprtc.collection;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.appspot.apprtc.R;

import java.util.ArrayList;

/**
 * Created by kippe_000 on 2017-10-07.
 */

public class Main extends Fragment {
    ListView listView;
    ArrayList<Data> arrayList;
    Adapter adapter;
    ArrayList<String> datelist;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.collection_main, container, false);

        listView = (ListView)view.findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        datelist = new ArrayList<>();
        adapter = new Adapter(getContext());
        listView.setAdapter(adapter);




        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        GetData();
    }

    void GetData(){
        CollectionDataBase collectionDataBase = new CollectionDataBase(getContext(), "recommendation.db", null, 1);
        arrayList = collectionDataBase.select();
        Log.d("size", arrayList.size()+"");
        datelist.clear();
        adapter.notifyDataSetChanged();
    }

    class Adapter extends BaseAdapter{
        Context context;

        Adapter(Context context){
            this.context = context;
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.collection_main_item, parent, false);
            }

            Data data = arrayList.get(position);

            String[] date = data.month_year.split("\\\\n");


            TextView textView = (TextView)convertView.findViewById(R.id.textView);
            ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView);

            if(data.flag == 1) {
                textView.setText(date[0] + ". " + date[1]);
                datelist.add(data.month_year);
                textView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
            }else {
                textView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);

                DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
                int width = dm.widthPixels;
                ViewGroup.LayoutParams params = imageView.getLayoutParams();
                params.width = width;
                params.height = width;
                imageView.setLayoutParams(params);


                imageView.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/recommendation/collection/." + data.path + ".jpg"));
            }
            return convertView;
        }
    }
}
