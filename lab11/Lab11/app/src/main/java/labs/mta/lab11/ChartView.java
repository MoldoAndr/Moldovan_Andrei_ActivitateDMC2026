package labs.mta.lab11;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ChartView extends View {
    private float[] data;
    private Paint paint;
    private final RectF rect = new RectF();

    private final int[] colors = {Color.RED,
            Color.GREEN, Color.BLUE,
            Color.YELLOW, Color.CYAN,
            Color.MAGENTA, Color.GRAY,
            Color.DKGRAY, Color.LTGRAY,
            Color.BLACK};

    public ChartView(Context context) {
        super(context);
        init();
    }

    public ChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
    }

    public void setData(float[] data) {
        this.data = data;
        invalidate();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        if (data == null || data.length == 0) return;

        float total = 0;
        for (float val : data) {
            total += val;
        }

        if (total == 0) return;

        float startAngle = 0;
        int width = getWidth();
        int height = getHeight();
        int minDim = Math.min(width, height) - 100;
        
        rect.set(
                width / 2f - minDim / 2f,
                height / 2f - minDim / 2f,
                width / 2f + minDim / 2f,
                height / 2f + minDim / 2f
        );

        for (int i = 0; i < data.length; i++) {
            paint.setColor(colors[i % colors.length]);
            float sweepAngle = (data[i] / total) * 360;
            canvas.drawArc(rect, startAngle, sweepAngle, true, paint);
            startAngle += sweepAngle;
        }
    }
}