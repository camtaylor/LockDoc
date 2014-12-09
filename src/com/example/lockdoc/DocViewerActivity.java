package com.example.lockdoc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.rekap.lockdoc.R;

public class DocViewerActivity extends Activity {

	private ImageView iv;
	private Matrix matrix = new Matrix();
	private float scale = 1f;
	private ScaleGestureDetector GD;
	private GestureDetector detector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doc_viewer);
		Bundle extras = getIntent().getExtras();
		setTitle(extras.getString("name"));
		String path = extras.getString("path");
		setImage(path);
		// instantiate gesture detector with a listener
		GD = new ScaleGestureDetector(this, new ScaleListener());
		detector = new GestureDetector(this, new GestureListener());
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		iv.setScaleType(ScaleType.MATRIX);
		GD.onTouchEvent(ev);
		detector.onTouchEvent(ev);
		return true;
	}

	private class GestureListener extends
			GestureDetector.SimpleOnGestureListener {
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			if (!(iv.getLayoutParams() instanceof MarginLayoutParams)) {
				return false;
			}

			MarginLayoutParams marginLayoutParams = (MarginLayoutParams) iv
					.getLayoutParams();

			marginLayoutParams.leftMargin = (int) ((int) marginLayoutParams.leftMargin - distanceX);
			marginLayoutParams.topMargin = (int) ((int) marginLayoutParams.topMargin - distanceY);

			iv.requestLayout();

			return true;
		}
	}

	public void setImage(String path) {
		try {
			iv = (ImageView) findViewById(R.id.image_view);
			FileInputStream is;
			is = new FileInputStream(path);
			Bitmap b = BitmapFactory.decodeStream(is);
			iv.setImageBitmap(b);
			iv.setScaleType(ScaleType.CENTER_INSIDE);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	private class ScaleListener extends
			ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			scale *= detector.getScaleFactor();
			scale = Math.max(0.1f, Math.min(scale, 5.0f));
			matrix.setScale(scale, scale);
			iv.setImageMatrix(matrix);
			return true;
		}
	}

}
