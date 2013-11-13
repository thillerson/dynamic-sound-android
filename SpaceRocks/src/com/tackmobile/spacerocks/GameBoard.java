package com.tackmobile.spacerocks;

import java.util.ArrayList;
import java.util.Vector;

import com.tackmobile.spacerocks.models.Missile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class GameBoard extends View {

	private static final int MISSILE_VELOCITY = 10;
	private static final int SHIP_VELOCITY = 10;

	private Paint paint;
	private Point shipPoint;
	private Bitmap shipBitmap;
	private Rect shipBounds;
	Point tappedPoint;
	Rect shipMovementRect, fireMissleRect;
	ArrayList<Missile> missiles, missilesToRemove;

	public GameBoard(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		paint = new Paint();
		shipPoint = new Point(50,50);
		shipBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.player_spaceship);
		shipBounds = new Rect(0,0, shipBitmap.getWidth(), shipBitmap.getHeight());
		shipMovementRect = new Rect();
		fireMissleRect = new Rect();
		missiles = new ArrayList<Missile>(20);
		missilesToRemove = new ArrayList<Missile>(1);
	}
	
	@Override public void layout(int l, int t, int r, int b) {
		super.layout(l, t, r, b);
		shipMovementRect.right = getWidth()/2;
		shipMovementRect.bottom = getHeight();
		fireMissleRect.left = getWidth()/2;
		fireMissleRect.right = getWidth();
		fireMissleRect.bottom = getHeight();
	}

	@Override synchronized public void onDraw(Canvas canvas) {
		paint.setColor(Color.BLACK);
		paint.setAlpha(255);
	    paint.setStrokeWidth(1);
		canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
		
		if (shipPoint.x>=0) {
			canvas.drawBitmap(shipBitmap, shipPoint.x, shipPoint.y, null);
		}

		for (Missile m : missiles) {
			canvas.drawBitmap(m.bitmap, m.point.x, m.point.y, null);
		}
	}
	
	@Override public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			int x = (int)event.getX();
			int y  = (int)event.getY();
			if (shipMovementRect.contains(x, y)) {
				tappedPoint = new Point(x, y);
			} else if (fireMissleRect.contains(x, y)) {
				Missile newMissile = new Missile();
				newMissile.point = new Point(shipPoint.x, shipPoint.y);
				newMissile.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.player_missile);
				missiles.add(newMissile);
			}
		}
		return true;
	}
	
	public void updatePositions() {
		if (tappedPoint != null) {
			int distance = tappedPoint.y - shipPoint.y;
			if (Math.abs(distance) < SHIP_VELOCITY) {
				tappedPoint = null;
			} else {
				shipPoint.y += distance/SHIP_VELOCITY;
				Log.d("Game", "The Math" + distance/SHIP_VELOCITY);
			}
		}
		for (Missile m : missiles) {
			m.point.x += MISSILE_VELOCITY;
			if(m.point.x + m.bitmap.getWidth() > getWidth()) {
				missilesToRemove.add(m);
			}
		}
		for (Missile m : missilesToRemove) {
			missiles.remove(m);
		}
		missilesToRemove.clear();
		invalidate();
	}

}
