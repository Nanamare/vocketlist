package com.jmpergar.awesometext;
/*
 * Copyright (C) 2015 José Manuel Pereira García.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AwesomeTextHandler {

    private final static int DEFAULT_RENDER_APPLY_MODE = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE;
    private final static int UPPER_LEFT_X = 0;
    private final static int UPPER_LEFT_Y = 0;

    public interface ViewSpanRenderer {
        public abstract View getView(final String text, final Context context);
    }

    public interface ViewSpanClickListener {
        public void onClick(String text, Context context);
    }

    private TextView mView;
    private Context mContext;
    private Map<String, ViewSpanRenderer> mRenderers;

    public AwesomeTextHandler() {
        mRenderers = new HashMap<>();
    }

    public AwesomeTextHandler addViewSpanRenderer(String pattern, ViewSpanRenderer viewSpanRenderer) {
        mRenderers.put(pattern, viewSpanRenderer);
        if (mView != null) {
            applyRenderers();
        }
        return this;
    }

    public Map<String, ViewSpanRenderer> getViewSpanRenderers() {
        return mRenderers;
    }

    public void setView(TextView mView) {
        this.mView = mView;
        this.mContext = mView.getContext();
        applyRenderers();
    }

    public void setText(String text) {
        if (mView != null) {
            mView.setText(text);
            applyRenderers();
        } else {
            throw new IllegalStateException("View mustn't be null");
        }
    }

    public final Spannable renderer(Spannable spannableString, CharSequence string) {
        if (mRenderers == null) {
            return null;
        }

        Set<String> spanPatterns = mRenderers.keySet();
        for (String spanPattern : spanPatterns) {
            Pattern pattern = Pattern.compile(spanPattern);
            Matcher matcher = pattern.matcher(spannableString);
            while (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();

                ViewSpanRenderer renderer = mRenderers.get(spanPattern);
                String text = matcher.group(0);
                View spannableView = renderer.getView(text, mContext);

                BitmapDrawable bitmpaDrawable = (BitmapDrawable) ViewUtils.convertViewToDrawable(spannableView);
                bitmpaDrawable.setBounds(UPPER_LEFT_X, UPPER_LEFT_Y, bitmpaDrawable.getIntrinsicWidth(), bitmpaDrawable.getIntrinsicHeight());
                spannableString.setSpan(new ImageSpan(bitmpaDrawable), start, end, DEFAULT_RENDER_APPLY_MODE);

                if (renderer instanceof ViewSpanClickListener) {
                    enableClickEvents();
                    ClickableSpan clickableSpan = getClickableSpan(text, (ViewSpanClickListener) renderer);
                    spannableString.setSpan(clickableSpan, start, end, DEFAULT_RENDER_APPLY_MODE);
                }
            }
        }

        return spannableString;
    }

    private void applyRenderers() {
        if (mRenderers != null) {
            Spannable spannableString = renderer(new SpannableString(mView.getText()), mView.getText());
            mView.setText(spannableString);
        }
    }

    private void enableClickEvents() {
        mView.setMovementMethod(LinkMovementMethod.getInstance());
        mView.setHighlightColor(mContext.getResources().getColor(android.R.color.transparent));
    }

    private ClickableSpan getClickableSpan(final String text, final ViewSpanClickListener listener) {
        ClickableSpan clickableSpan = new ClickableSpanWithoutFormat() {
            @Override
            public void onClick(View view) {
                listener.onClick(text, mContext);
            }
        };
        return clickableSpan;
    }
}

