package org.appspot.apprtc.board;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.appspot.apprtc.R;
import org.appspot.apprtc.webRTC.ConnectActivity;

/**
 * Created by kippe_000 on 2017-10-07.
 */

public class Main extends Fragment {
    Context context;
    Button btn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.board_main, container, false);

        context = view.getContext();
        btn = (Button)view.findViewById(R.id.web_btn);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context.getApplicationContext(), ConnectActivity.class));
            }
        });
    }
}
