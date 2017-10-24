package org.appspot.apprtc.collection;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.appspot.apprtc.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by woongbishin on 2017. 10. 24..
 */

public class TestActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);

        Button capture = (Button)findViewById(R.id.capture);
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });


    }


    private static void screenshot(View view) {

        view.buildDrawingCache();

        Bitmap bm;
        bm = view.getDrawingCache();
        try {
            String file_path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/recommendation/collection/";

            File path = new File(file_path);

            if(! path.isDirectory()) {
                path.mkdirs();
            }

            file_path = file_path + "파일명";

            file_path = file_path + ".jpg";

            FileOutputStream out = new FileOutputStream(file_path);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);

        } catch (FileNotFoundException e) {
            Log.d("FileNotFoundException:", e.getMessage());
        }
    }


}
