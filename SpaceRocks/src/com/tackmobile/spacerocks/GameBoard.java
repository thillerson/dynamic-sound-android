package com.tackmobile.spacerocks;

import java.util.HashSet;

import com.tackmobile.spacerocks.audio.PdInterface;
import com.tackmobile.spacerocks.models.Asteroid;
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
import android.view.MotionEvent;
import android.view.View;

public class GameBoard extends View {

	private static final int MISSILE_VELOCITY = 10;
	private static final int SHIP_VELOCITY = 20;
	private static final int ASTEROID_VELOCITY = 4;

	private Paint paint;
	private Point shipPoint;
	private Bitmap shipBitmap;
	private Rect shipBounds;
	private Rect collisionRect;
	Point tappedPoint;
	Rect shipMovementRect, fireMissleRect;
	HashSet<Missile> missiles, missilesToRemove;
	HashSet<Asteroid> asteroids, asteroidsToRemove;

	public GameBoard(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		paint = new Paint();
		shipPoint = new Point(50,50);
		shipBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.player_spaceship);
		shipBounds = new Rect(0,0, shipBitmap.getWidth(), shipBitmap.getHeight());
		collisionRect = new Rect();
		shipMovementRect = new Rect();
		fireMissleRect = new Rect();
		missiles = new HashSet<Missile>(20);
		missilesToRemove = new HashSet<Missile>(1);
		asteroids = new HashSet<Asteroid>(5);
		asteroidsToRemove = new HashSet<Asteroid>(1);
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
		
		for (Asteroid a : asteroids) {
			canvas.drawBitmap(a.bitmap, a.point.x, a.point.y, null);
		}
	}
	
	@Override public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			int x = (int)event.getX();
			int y  = (int)event.getY();
			if (shipMovementRect.contains(x, y)) {
				tappedPoint = new Point(x, y);
			} else if (fireMissleRect.contains(x, y)) {
				Missile newMissile = new Missile(getContext());
				newMissile.point = new Point(shipPoint.x + shipBitmap.getWidth(), shipPoint.y + shipBitmap.getHeight());
				missiles.add(newMissile);
				PdInterface.getInstance().fire();
			}
		}
		return true;
	}
	
	public void onFrame() {
		updatePositions();
		spawnAsteroids();
		detectCollisions();
		invalidate();
	}

	private void detectCollisions() {
		shipBounds.left = shipPoint.x;
		shipBounds.top = shipPoint.y;
		shipBounds.bottom = shipBounds.top + shipBitmap.getHeight();
		shipBounds.right = shipBounds.left + shipBitmap.getWidth();
		for (Asteroid a : asteroids) {
			collisionRect.left = a.point.x;
			collisionRect.top = a.point.y;
			collisionRect.bottom = collisionRect.top + a.bitmap.getHeight();
			collisionRect.right = collisionRect.left + a.bitmap.getWidth();
			for (Missile m : missiles) {
				if (collisionRect.contains(m.point.x, m.point.y)) {
					missilesToRemove.add(m);
					asteroidsToRemove.add(a);
					PdInterface.getInstance().asteroidHit();
				}
			}
			if (shipBounds.intersect(collisionRect)) {
				asteroidsToRemove.add(a);
				PdInterface.getInstance().shipHit();
			}
		}
		for (Missile m : missilesToRemove) {
			missiles.remove(m);
		}
		for (Asteroid a : asteroidsToRemove) {
			asteroids.remove(a);
		}
	}

	private void spawnAsteroids() {
		if (Math.random() > 0.98) {
			double percent = Math.random();
			Asteroid a = new Asteroid(getContext());
			a.point = new Point(getWidth(), (int)(percent * getHeight()));
			asteroids.add(a);
		}
	}

	private void updatePositions() {
		if (tappedPoint != null) {
			int distance = tappedPoint.y - shipPoint.y;
			if (Math.abs(distance) < SHIP_VELOCITY) {
				PdInterface.getInstance().thrusterOff();
				tappedPoint = null;
			} else {
				PdInterface.getInstance().thrusterOn();
				shipPoint.y += distance/SHIP_VELOCITY;
			}
		}
		for (Asteroid a : asteroids) {
			a.point.x -= ASTEROID_VELOCITY;
			if(a.point.x + a.bitmap.getWidth() < 0) {
				asteroidsToRemove.add(a);
			}
		}
		for (Asteroid a : asteroidsToRemove) {
			asteroids.remove(a);
		}
		asteroidsToRemove.clear();

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
	}

}
