package custom_components;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ImageView;

/**
 * Created by USER on 20-Jun-15.
 */
public class RoundCornerImageView extends ImageView{
    private int borderWidth = 4;
    private int viewWidth;
    private int viewHeight;
    private Bitmap image;
    private Paint paint;
    private Paint paintBorder;
    private BitmapShader shader;

    public RoundCornerImageView(Context context)
    {
        super(context);
        setup();
    }

    public RoundCornerImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setup();
    }

    public RoundCornerImageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        setup();
    }
    public RoundCornerImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setup();
    }

    private void setup()
    {
        // init paint
        paint = new Paint();
        paint.setAntiAlias(true);


        paintBorder = new Paint();
        //setBorderColor(Color.YELLOW);
        // setBorderColor(getResources().getColor(R.color._roundedImgeBoderColor));
        paintBorder.setAntiAlias(true);
       // this.setLayerType(LAYER_TYPE_SOFTWARE, paintBorder);

      //  paintBorder.setShadowLayer(4.0f, 0.0f, 2.0f, Color.RED);

    }

    public void setBorderWidth(int borderWidth)
    {
        this.borderWidth = borderWidth;
        this.invalidate();
    }

    public void setBorderColor(int borderColor)
    {
        if (paintBorder != null)
            paintBorder.setColor(borderColor);

        this.invalidate();
    }

    private void loadBitmap()
    {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) this.getDrawable();

        if (bitmapDrawable != null)
            image = bitmapDrawable.getBitmap();
    }

    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas canvas)
    {
        // load the bitmap
        loadBitmap();

        // init shader
        if (image != null)
        {
            shader = new BitmapShader(Bitmap.createScaledBitmap(image, canvas.getWidth(), canvas.getHeight(), false), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint.setShader(shader);




            Rect rect = new Rect(0, 0, getWidth(), getHeight());
            RectF rectF = new RectF(rect);
            DisplayMetrics metrics = new DisplayMetrics();
            Activity activity = (Activity) getContext();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            float density = metrics.density;
            int arcSize = Math.round(density * 5);
            canvas.drawRoundRect(rectF, arcSize, arcSize, paint);
            //canvas.drawRoundRect(rectF.left,rectF.top,rectF.right,rectF.bottom,arcSize,arcSize,paint);





        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec, widthMeasureSpec);

        viewWidth = width - (borderWidth * 2);
        viewHeight = height - (borderWidth * 2);

        setMeasuredDimension(width, height);
    }

    private int measureWidth(int measureSpec)
    {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY)
        {
            // We were told how big to be
            result = specSize;
        }
        else
        {
            // Measure the text
            result = viewWidth;
        }

        return result;
    }

    private int measureHeight(int measureSpecHeight, int measureSpecWidth)
    {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpecHeight);
        int specSize = MeasureSpec.getSize(measureSpecHeight);

        if (specMode == MeasureSpec.EXACTLY)
        {
            // We were told how big to be
            result = specSize;
        }
        else
        {
            // Measure the text (beware: ascent is a negative number)
            result = viewHeight;
        }

        return (result + 0);
    }


}


