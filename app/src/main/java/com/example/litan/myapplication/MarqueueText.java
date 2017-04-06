package com.example.litan.myapplication;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by litan on 2017/4/4.
 */

public class MarqueueText extends TextView{
    public MarqueueText(Context context) {
        super(context);

    }

    public MarqueueText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueueText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MarqueueText(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @Override
    public boolean isFocused(){
        return true;
    }
}
