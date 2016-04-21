/*
 * Copyright (C) 2016 Source Allies, Inc.
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

package com.sourceallies.android.zonebeacon.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import com.sourceallies.android.zonebeacon.R;

import java.util.ArrayList;

/**
 * A simple view which creates a background animation of expanding circles moving out from the
 * center of the view.
 */
public class NearbyBackgroundLayout extends RelativeLayout {

    private static final int NUMBER_CIRCLES = 3;
    private static final int ANIMATION_DURATION = 4000;

    private ArrayList<Integer> circleRadius;
    private int centerX;
    private int centerY;
    private Paint circlePaint;

    public NearbyBackgroundLayout(Context context) {
        this(context, null);
    }

    public NearbyBackgroundLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NearbyBackgroundLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        circleRadius = new ArrayList<Integer>();

        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        int maxRadius = (int) (Math.sqrt(width * width + height * height) / 2);

        centerX = width / 2;
        centerY = height / 2;

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getContext().getTheme();
        theme.resolveAttribute(android.R.attr.dividerHeight, typedValue, true);
        int dividerSize = typedValue.data;

        theme.resolveAttribute(R.attr.nearby_circle_color, typedValue, true);
        int color = typedValue.data;

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(color);
        circlePaint.setStrokeWidth(dividerSize);
        circlePaint.setStyle(Paint.Style.STROKE);

        for (int i = 0; i < NUMBER_CIRCLES; i++) {
            addCircle(i, maxRadius);
        }
    }

    private void addCircle(final int index, final int maxRadius) {
        circleRadius.add(0);

        ValueAnimator animator = ValueAnimator.ofInt(0, maxRadius);
        animator.setDuration(ANIMATION_DURATION);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setStartDelay((index + 1) * (ANIMATION_DURATION / NUMBER_CIRCLES));
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @SuppressLint("NewApi")
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                circleRadius.set(index, (Integer) animation.getAnimatedValue());
                postInvalidate();
            }
        });
        animator.start();
    }

    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        centerX = (right - left) / 2;
        centerY = (bottom - top) / 2;
    }

    @Override
    public void onDraw(Canvas canvas) {
        for (int i = 0; i < circleRadius.size(); i++) {
            canvas.drawCircle(centerX, centerY, circleRadius.get(i).floatValue(), circlePaint);
        }

        super.onDraw(canvas);
    }

}
