package ua.com.imalur.authographeer;

import java.util.Stack;

import ua.com.imalur.authographeer.utils.PhotoFileHelper;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

//������� �����, �������������� �� View 
public class AuthographView extends View {
	
	public static int DEF_COLOR = Color.WHITE;
	public static float DEF_WIDTH = 5f;
	
	private final int DISTANCE_THRESHOLD = 5; // 10
	private float prevX;
	private float prevY;
	
	private Stack<PaintPathPair> paintPathList = new Stack<PaintPathPair>();	// ��������� �����
	private Paint currentPaint;	// ����� � �����
	private Path currentPath;	// ������� ���� - ��� ������� �����������
	
	private boolean saved = true;	// ���� ���������
	private String photoPath;		// ���� � �������� �����������
	private Bitmap photo;
	private Bitmap background;
	private int w;
	private int h;
	
	// ����������� �������������� ����������� � ������� ���������
    public AuthographView(Context context, AttributeSet attrs) {
        super(context, attrs);
        currentPaint = getDefaultPaint();
    }
    /*
     * ���������� ���� � �������� �����������
     */
    public void setBackgroundPath(String photoPath){
    	this.photoPath = photoPath;
    }
    
    /*
     * ��� ��������������� ������� ������� �������������� ������� �������
     * @see android.view.View#onSizeChanged(int, int, int, int)
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    	super.onSizeChanged(w, h, oldw, oldh);
    	this.w = w;
    	this.h = h;
    	background = PhotoFileHelper.getScaledBitmapFromResource(
    			getResources(), R.drawable.old_paper_texture, w, h);
    	photo = PhotoFileHelper.getScaledBitmapFromFile(photoPath, w, h);     	 
//    	photo = BitmapHelper.getScaledBitmapFromFile(photoPath, w, h); 
    }
    
    /*
     * ���������� �������
     * @see android.view.View#onTouchEvent(android.view.MotionEvent)
     */	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();	// ��������
		// ���������� ����� �������
		float x = event.getX();
		float y = event.getY();
		// ������ �������
		if (action == MotionEvent.ACTION_DOWN){
			// ������� ����� path, ������������� � ����� ������� � ��������� � ���������
			currentPath = new Path();			
			currentPath.moveTo(x, y);
			
			paintPathList.add(new PaintPathPair(currentPath, clonePaint(currentPaint)));			
		}
		// ����������� ������
		if (action == MotionEvent.ACTION_MOVE){
			float distanceSquared = (x - prevX)*(x - prevX) +  (y - prevY)*(y - prevY);
			// ������������ - �������� ������������			
			// ������� ����������, ����� ������� ���������� ���� ������
			if (distanceSquared > DISTANCE_THRESHOLD)
				currentPath.quadTo(prevX, prevY, (x + prevX)/2, (y + prevY)/2);
		}
		// ������� �����
		if (action == MotionEvent.ACTION_UP){							
			currentPath = null;	
			saved = false;
		}
		// ��������� ���������� ���������� �����
		prevX = x;
		prevY = y;
		invalidate();	// �������������� - ����� onDraw
		return true;
	}
	
	/*
	 *  ���������
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		// ���
		if (background != null){
			canvas.drawBitmap(background, 0, 0, null);
		}
		else{
			canvas.drawColor(Color.GRAY);
		}
		// ���� - ������������ �� ������
		if (photo != null){
			int x = (w - photo.getWidth())/2;
			int y = (h - photo.getHeight())/2;
			canvas.drawBitmap(photo, x, y, null);
		}
		// ��������� ���� �������
		for(PaintPathPair p : paintPathList){
			p.draw(canvas);
		}			
	}	
	
	/*
	 * Paint �� ���������
	 */
	private Paint getDefaultPaint(){		
        Paint paint = new Paint();
        paint.setAntiAlias(true);				// �����������
        paint.setColor(DEF_COLOR);				// ����
        paint.setStrokeCap(Paint.Cap.ROUND);	// ������������ ����
        paint.setStrokeWidth(DEF_WIDTH);		// �������
        paint.setStyle(Paint.Style.STROKE);		// ������ ������
        return paint;
	}
	/*
	 * ������ ����� ������ Paint
	 */
	private Paint clonePaint(Paint paint){		
		Paint newpaint = new Paint();
        newpaint.setAntiAlias(true);				
        newpaint.setStrokeCap(Paint.Cap.ROUND);	
        newpaint.setStyle(Paint.Style.STROKE);
        newpaint.setColor(paint.getColor());
        newpaint.setStrokeWidth(paint.getStrokeWidth());
        return newpaint;
	}
	
	public void setCurrentPaintParams(int color, float width){
		this.currentPaint.setColor(color);
		this.currentPaint.setStrokeWidth(width);
	}
	
	public Paint getCurrentPaint(){
		return this.currentPaint;
	}

	/*
	 * �������� ���� �������
	 */
	public void clear(){
		paintPathList.clear();
		invalidate();
	}
	
	/*
	 * ������� ��������� �������
	 */
	public void undo(){
		if (paintPathList.size() > 0){
			paintPathList.pop();
			invalidate();
		}
	}
	

	public boolean isSaved(){
		return saved;
	}
	
	public void setSaved(boolean saved){
		this.saved = saved;
	}
	
}
