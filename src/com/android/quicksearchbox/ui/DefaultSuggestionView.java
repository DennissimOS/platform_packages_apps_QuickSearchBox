/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.quicksearchbox.ui;

import com.android.quicksearchbox.R;
import com.android.quicksearchbox.SuggestionCursor;
import com.android.quicksearchbox.SuggestionPosition;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * View for the items in the suggestions list. This includes promoted suggestions,
 * sources, and suggestions under each source.
 *
 */
public class DefaultSuggestionView extends RelativeLayout implements SuggestionView {

    private static final boolean DBG = false;
    private static final String TAG = "QSB.SuggestionView";

    /**
     * The cursor that contains the current suggestion.
     */
    private SuggestionCursor mCursor;
    /**
     * The position within the cursor of the current suggestion.
     */
    private int mPos;

    private TextView mText1;
    private TextView mText2;
    private ImageView mIcon1;
    private ImageView mIcon2;

    public DefaultSuggestionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public DefaultSuggestionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DefaultSuggestionView(Context context) {
        super(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mText1 = (TextView) findViewById(R.id.text1);
        mText2 = (TextView) findViewById(R.id.text2);
        mIcon1 = (ImageView) findViewById(R.id.icon1);
        mIcon2 = (ImageView) findViewById(R.id.icon2);
    }

    /**
     * Gets the suggestion that this view is showing.
     */
    public SuggestionPosition getSuggestionPosition() {
        if (mCursor == null) {
            throw new IllegalStateException("No cursor in SuggestionView");
        }
        return new SuggestionPosition(mCursor, mPos);
    }

    public void bindAsSuggestion(SuggestionCursor suggestion) {
        mCursor = suggestion;
        mPos = suggestion.getPosition();
        CharSequence text1 = suggestion.getSuggestionFormattedText1();
        CharSequence text2 = suggestion.getSuggestionFormattedText2();
        Drawable icon1 = suggestion.getSuggestionDrawableIcon1();
        Drawable icon2 = suggestion.getSuggestionDrawableIcon2();
        if (DBG) {
            Log.d(TAG, "bindAsSuggestion(), text1=" + text1 + ",text2=" + text2
                    + ",icon1=" + icon1 + ",icon2=" + icon2);
        }
        setText1(text1);
        setText2(text2);
        setIcon1(icon1);
        setIcon2(icon2);
    }

    /**
     * Sets the first text line.
     */
    private void setText1(CharSequence text) {
        mText1.setText(text);
    }

    /**
     * Sets the second text line.
     */
    private void setText2(CharSequence text) {
        mText2.setText(text);
        if (TextUtils.isEmpty(text)) {
            mText2.setVisibility(GONE);
        } else {
            mText2.setVisibility(VISIBLE);
        }
    }

    /**
     * Sets the left-hand-side icon.
     */
    private void setIcon1(Drawable icon) {
        setViewDrawable(mIcon1, icon);
    }

    /**
     * Sets the right-hand-side icon.
     */
    private void setIcon2(Drawable icon) {
        setViewDrawable(mIcon2, icon);
    }

    /**
     * Sets the drawable in an image view, makes sure the view is only visible if there
     * is a drawable.
     */
    private static void setViewDrawable(ImageView v, Drawable drawable) {
        // Set the icon even if the drawable is null, since we need to clear any
        // previous icon.
        v.setImageDrawable(drawable);

        if (drawable == null) {
            v.setVisibility(View.GONE);
        } else {
            v.setVisibility(View.VISIBLE);

            // This is a hack to get any animated drawables (like a 'working' spinner)
            // to animate. You have to setVisible true on an AnimationDrawable to get
            // it to start animating, but it must first have been false or else the
            // call to setVisible will be ineffective. We need to clear up the story
            // about animated drawables in the future, see http://b/1878430.
            drawable.setVisible(false, false);
            drawable.setVisible(true, false);
        }
    }

}