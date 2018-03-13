package com.tanny.infiniteviewpager.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

import com.tanny.infiniteviewpager.R;

/**
 * Created by tanny on 2018/3/13.
 * <p>
 * <p>Round dot indicator for {@link android.support.v4.view.ViewPager}, support for setting the dot size, color, etc.
 * in the layout file or in the code.</p>
 */
public class RoundDotIndicator extends View implements Indicator {

    private int radius = 4;
    private int spacing = 4;
    private final Paint selectedPaint;
    private final Paint unselectedPaint;
    private int dotCount;
    private int selectedColor = Color.RED;
    private int unselectedColor = Color.DKGRAY;
    private int currentSelected;

    public RoundDotIndicator(Context context) {
        this(context, null);
    }

    public RoundDotIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        // // get custom attrs
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundDotIndicator);

        // Retrieve the pointer colors of selected and unselected state.
        selectedColor = a.getColor(R.styleable.RoundDotIndicator_selectedColor, selectedColor);
        unselectedColor = a.getColor(R.styleable.RoundDotIndicator_unselectedColor, unselectedColor);
        // Retrieve the round dot radius
        radius = (int) a.getDimension(R.styleable.RoundDotIndicator_radius, radius);
        // Retrieve Spacing between dots
        spacing = (int) a.getDimension(R.styleable.RoundDotIndicator_spacing, spacing);

        a.recycle();

        selectedPaint = new Paint();
        selectedPaint.setAntiAlias(true);
        selectedPaint.setColor(selectedColor);
        selectedPaint.setStyle(Style.FILL);

        unselectedPaint = new Paint();
        unselectedPaint.setAntiAlias(true);
        unselectedPaint.setColor(unselectedColor);
        unselectedPaint.setStyle(Style.FILL);
    }

    /**
     * Set the position of the selected dot
     *
     * @param pos the selected position for dots
     */
    @Override
    public void setSelectedPosition(int pos) {
        this.currentSelected = pos;
        invalidate();
    }

    /**
     * Set dot count
     *
     * @param count dot count
     */
    @Override
    public void setCount(int count) {
        this.dotCount = count;
        invalidate();
    }

    /**
     * Set the color of the selected point
     *
     * @param color selected color
     */
    public void setSelectedColor(int color) {
        selectedColor = color;
        selectedPaint.setColor(selectedColor);
        invalidate();
    }

    /**
     * Set the color of the unselected point
     *
     * @param color selected color
     */
    public void setUnselectedColor(int color) {
        unselectedColor = color;
        unselectedPaint.setColor(unselectedColor);
        invalidate();
    }

    /**
     * Set spacing between dots
     *
     * @param spacing spacing between dots
     */
    public void setSpacing(int spacing) {
        this.spacing = spacing;
        invalidate();
    }

    /**
     * Set radius of round dot
     *
     * @param radius radius of round dot
     */
    public void setRadius(int radius) {
        this.radius = radius;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw all dots
        Paint paint;
        for (int i = 0; i < dotCount; i++) {
            paint = (i == currentSelected) ? selectedPaint : unselectedPaint;
            canvas.drawCircle(getPaddingLeft() + radius
                    + (i * (2 * radius + spacing)), getPaddingTop()
                    + radius, radius, paint);
        }
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        // We were told how big to be
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else { // Calculate the width according the dot count
            result = getPaddingLeft() + getPaddingRight() + (dotCount * 2 * radius) + (dotCount - 1) * spacing;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        // We were told how big to be
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else { // Calculate the height according the dot radius
            result = 2 * radius + getPaddingTop() + getPaddingBottom();
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

}
