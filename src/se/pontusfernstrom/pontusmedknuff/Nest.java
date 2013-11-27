package se.pontusfernstrom.pontusmedknuff;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Nest extends BoardTile {
	private final int N_PIECES_PER_PLAYER = 4;
	private Piece mPieces[] = new Piece[N_PIECES_PER_PLAYER];
	private int mNPieces;
	private Player mPlayer;
	
	public Nest(double x, double y, double radius, int color, Player player) 
	{
		super(x, y, radius, color);
	}

	public void putPiece (Piece piece) throws Exception
	{
		if (!free()) throw new Exception("Step not free!");
		this.mPieces[mNPieces++] = piece;
	}

	public void setPlayer(BoardView boardView, Player player)
	{
		int piece;
		
		this.mPlayer = player;
		
		for (piece = 0; piece < mPieces.length; piece++)
		{
			mPieces[piece] = new Piece(boardView, this, player);
			mNPieces++;
		}
	}

	@Override
	public Piece selectPiece(Player player) {
		Piece piece = null;
		if (mPlayer == player && mNPieces > 0)
		{
			piece = mPieces[this.mNPieces - 1];
			piece.select();
		}
		return piece;
	}

	@Override
	public void deselect() throws Exception
	{
		Piece piece;
		int pieceIdx;
		for (pieceIdx = 0; pieceIdx < this.mNPieces; pieceIdx++)
		{
			piece = mPieces[pieceIdx];
			if (piece.getSelected()) 
			{
				piece.deselect();
				return;
			}
		}
	}

	@Override
	public Piece removeSelectedPiece(Player player) {
		Piece piece;
		int pieceIdx = 0;
		while (pieceIdx < this.mNPieces &&
				this.mPieces[pieceIdx].getPlayer() != player)
		{
			pieceIdx++;
		}
		if (pieceIdx >= mNPieces) {
			return null;
		} else {
			piece = this.mPieces[pieceIdx];
		}
		while (pieceIdx < mNPieces - 1) 
		{
			this.mPieces[pieceIdx] = this.mPieces[++pieceIdx];
		}
		this.mPieces[pieceIdx] = null;
		this.mNPieces--;
		return piece;
	}

	@Override
	public void remove(Piece piece) throws Exception 
	{
		int pieceIdx = 0;
		while (!this.mPieces[pieceIdx].equals(piece) &&
				pieceIdx < this.mNPieces)
		{
			pieceIdx++;
		}
		if(pieceIdx >= mNPieces) throw new Exception("Piece to remove not found");
		while (pieceIdx < mNPieces - 1) 
		{
			this.mPieces[pieceIdx] = this.mPieces[++pieceIdx];
		}
		this.mPieces[pieceIdx] = null;
		this.mNPieces--;
	}

	@Override
	public boolean free() {
		return mNPieces < N_PIECES_PER_PLAYER;
	}
	
	public void drawPieces(Canvas canvas, float pieceRadius) {
		Paint paint;
		Piece piece;
		int pieceIdx;
		double x;
		double y;
		double edgeRadius;
		double centerRadius;
		double selectMultiplier;
		
		if (this.mNPieces > 0)
		{
			paint = new Paint();
			for (pieceIdx = 0; pieceIdx < mNPieces; pieceIdx++)
			{
				piece = this.mPieces[pieceIdx];
				selectMultiplier = piece.getSelected() ? 1.5 : 1;
				x = super.x + super.radius * 0.4 * (-1 + 2 * Math.floor(pieceIdx / 2));
				y = super.y + super.radius * 0.4 * (-1 + 2 * (pieceIdx % 2));
				edgeRadius = pieceRadius * selectMultiplier;
				centerRadius = edgeRadius - BoardView.EDGE_WIDTH * pieceRadius;
				paint.setColor(Color.WHITE);
				canvas.drawCircle((float)x, (float)y, (float)edgeRadius, paint);
				paint.setColor(this.color);
				canvas.drawCircle((float)x, (float)y, (float)centerRadius, paint);
			}
		}
	}
}