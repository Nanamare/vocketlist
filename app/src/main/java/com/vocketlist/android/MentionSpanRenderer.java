package com.vocketlist.android;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jmpergar.awesometext.AwesomeTextHandler;
import com.vongtome.android.common.util.ScreenUtils;


public class MentionSpanRenderer implements AwesomeTextHandler.ViewSpanRenderer, AwesomeTextHandler.ViewSpanClickListener {

    private final static int textSizeInDips = 18;
    private final static int backgroundResource = R.drawable.common_mentions_background;
    private final static int textColorResource = android.R.color.black;

    @Override
    public View getView(String text, Context context) {
        TextView view = new TextView(context);
        view.setText(text.substring(1));
        view.setTextSize(ScreenUtils.dipsToPixels(context, textSizeInDips));
        view.setBackgroundResource(backgroundResource);
        int textColor = context.getResources().getColor(textColorResource);
        view.setTextColor(textColor);
        return view;
    }

    @Override
    public void onClick(String text, Context context) {
        Toast.makeText(context, "Hello " + text, Toast.LENGTH_SHORT).show();
    }
}