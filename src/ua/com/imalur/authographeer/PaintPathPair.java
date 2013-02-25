package ua.com.imalur.authographeer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class PaintPathPair {
	private Paint paint;
	private Path path;
	
	public PaintPathPair(){		
	}	
	
	public PaintPathPair(Path path, Paint paint){
		this.path = path;
		this.paint = paint;
	}	
	
	public Paint getPaint() {
		return paint;
	}

	public void setPaint(Paint paint) {
		this.paint = paint;
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

	public void draw(Canvas canvas){
		canvas.drawPath(this.path, this.paint);
	}
	
}
