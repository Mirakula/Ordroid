package org.comp.klase;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.Button;

public class MeniDugme extends Button{

    public MeniDugme(Context context) {
        super(context);
    }

    public MeniDugme(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.rotate(-90, getWidth()/2, getHeight()/2f);
        super.onDraw(canvas);
        canvas.restore();
    }

}