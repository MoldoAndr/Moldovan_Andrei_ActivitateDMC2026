package labs.mta.lab11;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class BarChart extends View {
    private float[] data;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public BarChart(Context context) {
        super(context);
    }

    public BarChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setData(float[] data) {
        this.data = data;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (data == null || data.length == 0) return;

        float maxValue = data[0];
        for (float value : data) {
            if (value > maxValue) {
                maxValue = value;
            }
        }

        if (maxValue <= 0) return;

        float availableWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        float availableHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        float spacing = 20f;
        float barHeight = (availableHeight - 10f - spacing * (data.length + 1)) / data.length;

        for (int i = 0; i < data.length; i++) {
            float top = getPaddingTop() + spacing + i * (barHeight + spacing);
            float scaledWidth = (data[i] / maxValue) * Math.max(availableWidth - 20f, 0f);

            paint.setColor(Color.rgb(((i+2)*23)%256,(i*79)%256,(i*157)%256));            canvas.drawRect(
                    getPaddingLeft(),
                    top,
                    getPaddingLeft() + scaledWidth,
                    top + barHeight,
                    paint
            );
        }
    }
}
