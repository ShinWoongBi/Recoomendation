package org.appspot.apprtc.chatbot;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.appspot.apprtc.R;
import org.appspot.apprtc.collection.EditActivity;

/**
 * Created by kippe_000 on 2017-10-07.
 */

public class Main extends Fragment {
    Button button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chatbot_main, container, false);

        button = (Button)view.findViewById(R.id.collection_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditActivity.class);
                intent.putExtra("data", button.getText().toString());
                startActivity(intent);
            }
        });

        return view;
    }
}
