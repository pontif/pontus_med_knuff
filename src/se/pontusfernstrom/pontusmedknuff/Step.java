package se.pontusfernstrom.pontusmedknuff;

import se.pontusfernstrom.pontusmedknuff.BoardView.Piece;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Step extends BoardTile {
	private int edgeColor;
	private Piece mPiece;
	private int mNPieces;
	private boolean mNextStep;

	public Step(double x, double y, double radius, int color)
	{
		super(x, y, radius, color);
		initializeStep(x, y, radius, color, color);
	}

	public Step(double x, double y, double radius, int color, int edgeColor)
	{
		super(x, y, radius, color);
		initializeStep(x, y, radius, color, edgeColor);
	}

	private void initializeStep(double x, double y, double radius, int color, int edgeColor)
	{
		this.edgeColor = edgeColor;
		this.mNextStep = false;
		this.mPiece = null;
		this.mNPieces = 0;
	}

	public void putPiece (Piece piece) throws Exception
	{
		if (mNPieces != 0) throw new Exception("Step not free!");
		this.mPiece = piece;
		this.mNPieces = 1;
	}

	public void putPieces(Piece piece1, Piece piece2) throws Exception
	{
		// This function assumes that the pieces are equal and 
		// stores only one of them.
		if (mNPieces == 0) throw new Exception("Step not free!");
		this.mPiece = piece1;
		// Verify that the pieces are actually identical
		if (!(piece1 == piece2 || (piece1!=null && piece1.equals(piece2))))
		{
			throw new Exception("Pieces not equal!");
		}
		this.mNPieces = 2;
	}

	public void draw(Canvas canvas) 
	{
		Paint paint = new Paint();
		if (this.mNextStep)
		{
			paint.setColor(Color.WHITE);
			canvas.drawCircle((float)super.x, (float)super.y, (float)(super.radius * 1.1), paint);
		}
		paint.setColor(edgeColor);
		canvas.drawCircle((float)super.x, (float)super.y, (float)super.radius, paint);
		paint.setColor(super.color);
		canvas.drawCircle((float)super.x, (float)super.y, (float)(super.radius * 0.8), paint);
	}

	public void drawPieces(Canvas canvas) 
	{
		Paint paint = new Paint();
		int pieceIdx;
		double x;
		double y;
		double edgeRadius;
		double centerRadius;
		double selectMultiplier;

		for (pieceIdx = 0; pieceIdx < mNPieces; pieceIdx++)
		{
			selectMultiplier = (mPiece.getSelected() && pieceIdx == 0) ? 1.5 : 1;
			// Temporarily set to center first and put second completely off
			x = super.x + super.radius * 0.4 * (0 + 2 * pieceIdx);
			y = super.y + super.radius * 0.4 * (0 + 2 * pieceIdx);
			edgeRadius = mPieceRadius * selectMultiplier;
			centerRadius = edgeRadius - EDGE_WIDTH * mPieceRadius;
			paint.setColor(Color.WHITE);
			canvas.drawCircle((float)x, (float)y, (float)edgeRadius, paint);
			paint.setColor(this.mPiece.getColor());
			canvas.drawCircle((float)x, (float)y, (float)centerRadius, paint);
		}
	}

	public boolean free() 
	{
		return this.mNPieces == 0;
	}

	public void setNextStep() 
	{
		this.mNextStep = true;
	}

	public void unsetNextStep() 
	{
		this.mNextStep = false;
	}

	@Override
	public Piece selectPiece(int color) 
	{
		if (this.mNPieces > 0 && this.mPiece.getColor() == color)
		{
			this.mPiece.select();
			return this.mPiece;
		}
		else return null;
	}

	public Piece selectAnyPiece(int boardViewColor) {
		if (this.mNPieces > 0)
		{
			this.mPiece.select();
			return this.mPiece;
		}
		else return null;
	}

	@Override
	public void deselect() throws Exception
	{
		if(this.mNPieces == 0) throw new Exception("No piece to deselect!");
		this.mPiece.deselect();
	}

	@Override
	public Piece removeSelectedPiece(int color) throws Exception
	{
		Piece piece;
		if(this.mPiece.getColor() != color) throw new Exception("Color mismatch between Piece and Step");
		switch (mNPieces)
		{
		case 2:
			this.mPiece.deselect();
			this.mNPieces = 1;
			return new Piece(this.mPiece);
		case 1:
			piece = this.mPiece;
			this.mPiece.deselect();
			this.mNPieces = 0;
			this.mPiece = null;
			return piece;
		default:
			throw new Exception("Neither 1 nor 2 Pieces on Step");				
		}
	}

	@Override
	public void remove(Piece piece) throws Exception 
	{
		if(!this.mPiece.equals(piece)) throw new Exception("Piece to remove not the same as piece on Step");
		switch (mNPieces)
		{
		case 2:
			//this.mPiece.deselect();
			this.mNPieces = 1;
			break;
		case 1:
			//this.mPiece.deselect();
			this.mNPieces = 0;
			this.mPiece = null;
			break;
		default:
			throw new Exception("Neither 1 nor 2 Pieces on Step");
		}
	}
}