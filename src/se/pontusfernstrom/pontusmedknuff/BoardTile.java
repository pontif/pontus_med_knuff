package se.pontusfernstrom.pontusmedknuff;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class BoardTile {
	double x;
	double y;
	double radius;
	int color;
	
	public BoardTile(double x, double y, double radius, int color)
	{
		initBoardTile(x, y, radius, color);
	}

	private void initBoardTile(double x, double y, double radius, int color)
	{
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.color = color;
	}
	
	public void draw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(color);
		canvas.drawCircle((float)this.x, (float)this.y, (float)this.radius, paint);
	}
	
	public void setRadius(double r)
	{
		this.radius = r;
	}
	
	public boolean pointInside(double x, double y) {
		double xDist = this.x - x;
		double yDist = this.y - y;
		return Math.sqrt(xDist * xDist + yDist * yDist) < this.radius;
	}
	
	public Step findNextStep(int value) {
		return null;
	}

	public abstract Piece selectPiece(Player player);
	public abstract Piece removeSelectedPiece(Player player) throws Exception;
	public abstract void remove(Piece piece) throws Exception;
	public abstract void deselect() throws Exception;
	public abstract boolean free();
}