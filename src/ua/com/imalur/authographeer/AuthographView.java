package ua.com.imalur.authographeer;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

//������� �����, �������������� �� View 
public class AuthographView extends View {
	
	private final int DISTANCE_THRESHOLD = 10;
	private float prevX;
	private float prevY;
	
	private List<Path> pathlist = new ArrayList<Path>();	// ��������� �����
	private Paint paintLine;	// ����� � �����
	private Path currentPath;	// ������� ���� - ��� ������� �����������
	
	private boolean savedChanges = false;
	
	// ����������� �������������� ����������� � ������� ���������
    public AuthographView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // ��������� Paint ��� ������� ����� 
        paintLine = new Paint();
        paintLine.setAntiAlias(true);				// �����������
        paintLine.setColor(Color.GREEN);
        paintLine.setStrokeCap(Paint.Cap.ROUND);	// ������������ ����
        paintLine.setStrokeWidth(5);				// �������
        paintLine.setStyle(Paint.Style.STROKE);		// ������ ������
    }

	// ���������� �������
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
			pathlist.add(currentPath);			
//			Log.d("", "ACTION_DOWN");
		}
		// ����������� ������
		if (action == MotionEvent.ACTION_MOVE){
			float distanceSquared = (x - prevX)*(x - prevX) +  (y - prevY)*(y - prevY);
			// ������������ - �������� ������������			
			// ������� ����������, ����� ������� ���������� ���� ������

			if (distanceSquared > DISTANCE_THRESHOLD)
				currentPath.quadTo(prevX, prevY, (x + prevX)/2, (y + prevY)/2);
//			Log.d("", "ACTION_MOVE");			
		}
		// ������� �����
		if (action == MotionEvent.ACTION_UP){							
			Log.d("", "ACTION_UP");
			currentPath = null;			
		}
		// ��������� ���������� ���������� �����
		prevX = x;
		prevY = y;
		invalidate();	// �������������� - ����� onDraw
		return true;
	}
	
	// 6. ���������
	@Override
	protected void onDraw(Canvas canvas) {
		// ��������� ���� �������
		for(Path p : pathlist){
			canvas.drawPath(p, paintLine);
		}
			
	}

}
