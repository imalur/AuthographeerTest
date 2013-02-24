package ua.com.imalur.authographeer;

import java.util.ArrayList;
import java.util.List;

import ua.com.imalur.authographeer.utils.PhotoFileHelper;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

//—оздать класс, унаследованный от View 
public class AuthographView extends View {
	
	private final int DISTANCE_THRESHOLD = 5; // 10
	private float prevX;
	private float prevY;
	
	private List<Path> pathlist = new ArrayList<Path>();	// коллекци€ путей
	private Paint paintLine;	// стили и цвета
	private Paint paintWhite;	// бела€ кисть
	private Path currentPath;	// текущий путь - дл€ событи€ перемещени€
	
	private boolean savedChanges = false;
	private String photoPath;	// путь к фоновому изображению
	private Bitmap photo;
	private Bitmap background;
	private int w;
	private int h;
	
	// ќЅя«ј“≈Ћ№Ќќ переопредел€ть конструктор с набором атрибутов
    public AuthographView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // экземпл€р Paint дл€ свойств линий 
        paintLine = new Paint();
        paintLine.setAntiAlias(true);				// сглаживание
        paintLine.setColor(Color.WHITE);
        paintLine.setStrokeCap(Paint.Cap.ROUND);	// закругленные кра€
        paintLine.setStrokeWidth(5);				// толщина
        paintLine.setStyle(Paint.Style.STROKE);		// только контур
    }
    /*
     * ”становить путь к фоновому изображению
     */
    public void setBackgroundPath(String photoPath){
    	this.photoPath = photoPath;
    }
    
    /*
     * ѕри переопределении размера области масштабируетс€ фоновый рисунок
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
//    	phot = BitmapHelper.getScaledBitmapFromFile(photoPath, w, h); 
    }
    
    /*
     * обработчик касаний
     * @see android.view.View#onTouchEvent(android.view.MotionEvent)
     */	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();	// действие
		// координаты точки касани€
		float x = event.getX();
		float y = event.getY();
		// нажали пальцем
		if (action == MotionEvent.ACTION_DOWN){
			// создаем новый path, устанавливаем в точку касани€ и добавл€ем в коллекцию
			currentPath = new Path();			
			currentPath.moveTo(x, y);
			pathlist.add(currentPath);			
		}
		// перемещение пальца
		if (action == MotionEvent.ACTION_MOVE){
			float distanceSquared = (x - prevX)*(x - prevX) +  (y - prevY)*(y - prevY);
			// интерпол€ци€ - операци€ ресурсоемка€			
			// поэтому производим, когда квадрат рассто€ни€ выше порога
			if (distanceSquared > DISTANCE_THRESHOLD)
				currentPath.quadTo(prevX, prevY, (x + prevX)/2, (y + prevY)/2);
		}
		// подн€ли палец
		if (action == MotionEvent.ACTION_UP){							
			currentPath = null;			
		}
		// обновл€ем координаты предыдущей точки
		prevX = x;
		prevY = y;
		invalidate();	// перерисовываем - вызов onDraw
		return true;
	}
	
	/*
	 *  отрисовка
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		// фон
		if (background != null){
			canvas.drawBitmap(background, 0, 0, null);
		}
		else{
			canvas.drawColor(Color.GRAY);
		}
		// фото - центрируетс€ по холсту
		if (photo != null){
			int x = (w - photo.getWidth())/2;
			int y = (h - photo.getHeight())/2;
			canvas.drawBitmap(photo, x, y, null);
		}
		// отрисовка всех ломаных
		for(Path p : pathlist){
			canvas.drawPath(p, paintLine);
		}			
	}	
}
