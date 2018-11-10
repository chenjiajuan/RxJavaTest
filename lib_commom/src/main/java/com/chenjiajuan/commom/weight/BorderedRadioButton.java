package com.chenjiajuan.commom.weight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.chenjiajuan.commom.R;


/**
 * @author Francis
 * @date 2018/1/9
 */
public class BorderedRadioButton extends android.support.v7.widget.AppCompatRadioButton {

    private Paint paint = new Paint();

    private boolean left;
    private boolean top;
    private boolean right;
    private boolean bottom;

    public BorderedRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public BorderedRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BorderedRadioButton(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getResources().getColor(R.color.line_split));
        paint.setStrokeWidth(getResources().getDimension(R.dimen.dp_3));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (left) {
            canvas.drawLine(0, 0, 0, getHeight(), paint);
        }
        if (top) {
            canvas.drawLine(0, 0, getWidth(), 0, paint);
        }
        if (right) {
            canvas.drawLine(getWidth(), 0, getWidth(), getHeight(), paint);
        }
        if (bottom) {
            canvas.drawLine(0, getHeight(), getWidth(), getHeight(), paint);
        }
    }

    private void setBorders(boolean left, boolean top, boolean right, boolean bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public void setCheckedState(boolean checked) {
        if (checked) {
            setBorders(true, true, true, false);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) getLayoutParams();
            lp.topMargin = 0;
            setBackgroundColor(Color.parseColor("#FFFFFF"));
            setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            setBorders(true, true, true, true);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) getLayoutParams();
            lp.topMargin = (int) getResources().getDimension(R.dimen.dp_5);
            setBackgroundColor(Color.parseColor("#F4F4F4"));
            setTypeface(Typeface.DEFAULT);
        }
        requestLayout();
    }

}