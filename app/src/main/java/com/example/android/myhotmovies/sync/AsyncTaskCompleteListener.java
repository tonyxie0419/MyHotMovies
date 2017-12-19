package com.example.android.myhotmovies.sync;

import java.util.ArrayList;

/**
 * Created by xie on 2017/11/7.
 */

public interface AsyncTaskCompleteListener {
    void onTaskComplete(ArrayList result, String type);
}
