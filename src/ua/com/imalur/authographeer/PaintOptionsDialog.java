package ua.com.imalur.authographeer;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class PaintOptionsDialog extends Dialog {

	private final ImageView iwPaintLine;
	
	private Paint paintLine;			// объект для рисования на главном холсте
	private final Paint paintSample; 	// объект для рисования образца
	
	private SeekBar sbSize ;
	private SeekBar sbAlpha ;
	private SeekBar sbRed ;
	private SeekBar sbGreen ;
	private SeekBar sbBlue ;
	
	private Button btnOk;
	private Button btnCancel;
	
	public PaintOptionsDialog(Context context, Paint paint) {
		super(context);
		setTitle(R.string.pencil_dialog_title);
		setContentView(R.layout.paint_dialog);
		setCancelable(true);
		// копируем данные главной кисти в образец
		this.paintLine = paint;		
		this.paintSample = new Paint();
		this.paintSample.setAntiAlias(true);
		this.paintSample.setStrokeCap(Paint.Cap.ROUND);	
		this.paintSample.setStyle(Paint.Style.STROKE);
		this.paintSample.setStrokeWidth( this.paintLine.getStrokeWidth());		
		this.paintSample.setColor( this.paintLine.getColor());
		
		iwPaintLine = (ImageView) findViewById(R.id.iwPaintLine);
		
		btnOk = (Button) findViewById(R.id.btnOk);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		
		btnOk.setOnClickListener(buttonListener);
		btnCancel.setOnClickListener(buttonListener);
		
		sbSize = (SeekBar) findViewById(R.id.sbSize);
		sbAlpha = (SeekBar) findViewById(R.id.sbAlpha);
		sbRed = (SeekBar) findViewById(R.id.sbRed);
		sbGreen = (SeekBar) findViewById(R.id.sbGreen);
		sbBlue = (SeekBar) findViewById(R.id.sbBlue);
		
		sbSize.setOnSeekBarChangeListener(colorListener);
		sbAlpha.setOnSeekBarChangeListener(colorListener);
		sbRed.setOnSeekBarChangeListener(colorListener);
		sbGreen.setOnSeekBarChangeListener(colorListener);
		sbBlue.setOnSeekBarChangeListener(colorListener);
		
		// выставляем ползунки по кисти главного холста
		sbSize.setProgress( (int) paint.getStrokeWidth());
		int color = paint.getColor();
		sbAlpha.setProgress( Color.alpha(color));
		sbRed.setProgress( Color.red(color));
		sbGreen.setProgress( Color.green(color));
		sbBlue.setProgress( Color.blue(color));
	}
	
	/**
	 * Обработчик движения ползунков
	 */
	OnSeekBarChangeListener colorListener = new OnSeekBarChangeListener() {
		Bitmap bitmap = Bitmap.createBitmap(400, 100, 
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) { }
		
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {	}
		
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			paintSample.setStrokeWidth( sbSize.getProgress());
			paintSample.setARGB(
					sbAlpha.getProgress(),
					sbRed.getProgress(),
					sbGreen.getProgress(),
					sbBlue.getProgress());
			drawSampleLine();
		}
		
		private void drawSampleLine(){
			bitmap.eraseColor(Color.GRAY);
			canvas.drawLine(30, 50, 370, 50, paintSample);
			iwPaintLine.setImageBitmap(bitmap);
		}
	};
	
	/**
	 * Обработчик нажатия кнопок
	 */
	android.view.View.OnClickListener buttonListener = 
			new android.view.View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					switch (v.getId()) {
					case R.id.btnOk:
						paintLine.setStrokeWidth( paintSample.getStrokeWidth());		
						paintLine.setColor( paintSample.getColor());
						dismiss();
						break;
					case R.id.btnCancel:
						dismiss();
						break;
					default:
						break;
					}
					
				}
			};

}
