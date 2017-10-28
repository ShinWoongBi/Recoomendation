package org.appspot.apprtc.collection;

import org.appspot.apprtc.R;

/**
 * Created by woongbishin on 2017. 10. 27..
 */

public class Data {
    int[][] location = {{R.id.data_top_left,R.id.data_top_center,R.id.data_top_right},
            {R.id.data_center_left,R.id.data_center,R.id.data_center_right},
            {R.id.data_bottom_left,R.id.data_bottom_center,R.id.data_bottom_right}};
    int x = 1;
    int y = 1;

    float size;
    int background;
    int font;

    String day;
    String month_year;
    String message;


}
