package se.pontusfernstrom.pontusmedknuff;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class BoardView extends View implements OnTouchListener {
	
	private static enum State
	{
		NONE_SELECTED,
		ONE_SELECTED,
		BOTH_SELECTED
	}
	
	private static enum Player
	{
		BLUE,
		GREEN,
		RED,
		YELLOW
	}
	
	private final int N_STEPS = 57;
	private final int N_NESTS = 4;
	private final int ILLEGAL_STEP = Integer.MAX_VALUE;
	private final int N_PIECES = 16;
	private final int N_STEPS_PER_SIDE = 11;
	private final int N_PLAYERS = 4;
	private final int N_PIECES_PER_PLAYER = 4;
	private final int MAX_N_PIECES_PER_STEP = 4;
	private final double EDGE_WIDTH = 0.2;
	
	private int mWidth;
	private int mHeight;
	private double mStepBox;
	private double mStepRadius;
	private double mPieceRadius;
	
	private State mState;
	//private int mSelectedPieceStep;
	private BoardTile mSelectedTile;
	private Step mNextStepGlobal;
	private Player mPlayer;
	private int boardViewColor;
	private Dice mDice;
	
	private Bitmap  mBitmap;
	
	private Step[] mSteps = new Step[N_STEPS];
	private Nest[] mNests = new Nest[N_NESTS];
	private Piece[] mPieces = new Piece[N_PIECES];
	
	public BoardView(Context context) {
		super(context);
		
		initBoardView();
	}
	
	public BoardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initBoardView();
	}
	
	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		if (w == 0 || h == 0) return;
		mWidth = w;
		mHeight = h;
		mStepBox = mWidth / (N_STEPS_PER_SIDE + 1);
		mStepRadius = mStepBox * 0.45;
		mPieceRadius = mStepRadius * .4;
		resetBoardView();
	}
	
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		drawBackground(canvas);
		
		drawPieces(canvas);
		
		drawDice(canvas);
	}
	
	private void initBoardView() {
		this.mState = State.NONE_SELECTED;
		this.mNextStepGlobal = null;
		this.mPlayer = Player.BLUE;
		this.mDice = new Dice();
		this.boardViewColor = Color.BLUE;
	}
	
	
	private void resetBoardView() {
		mDice.reset();
		
		// Perimeter steps
		this.mSteps[0] = new Step(6 * mStepBox, 1 * mStepBox, mStepRadius, Color.BLUE);
		this.mSteps[1] = new Step(7 * mStepBox, 1 * mStepBox, mStepRadius, Color.BLUE);
		mSteps[2] = new Step(7 * mStepBox, 2 * mStepBox, mStepRadius, Color.GREEN);
		mSteps[3] = new Step(7 * mStepBox, 3 * mStepBox, mStepRadius, Color.RED);
		mSteps[4] = new Step(7 * mStepBox, 4 * mStepBox, mStepRadius, Color.YELLOW);
		mSteps[5] = new Step(7 * mStepBox, 5 * mStepBox, mStepRadius, Color.BLUE);
		mSteps[6] = new Step(8 * mStepBox, 5 * mStepBox, mStepRadius, Color.GREEN);
		mSteps[7] = new Step(9 * mStepBox, 5 * mStepBox, mStepRadius, Color.RED);
		mSteps[8] = new Step(10 * mStepBox, 5 * mStepBox, mStepRadius, Color.YELLOW);
		mSteps[9] = new Step(11 * mStepBox, 5 * mStepBox, mStepRadius, Color.BLUE);
		mSteps[10] = new Step(11 * mStepBox, 6 * mStepBox, mStepRadius, Color.GREEN);
		mSteps[11] = new Step(11 * mStepBox, 7 * mStepBox, mStepRadius, Color.GREEN);
		mSteps[12] = new Step(10 * mStepBox, 7 * mStepBox, mStepRadius, Color.RED);
		mSteps[13] = new Step(9 * mStepBox, 7 * mStepBox, mStepRadius, Color.YELLOW);
		mSteps[14] = new Step(8 * mStepBox, 7 * mStepBox, mStepRadius, Color.BLUE);
		mSteps[15] = new Step(7 * mStepBox, 7 * mStepBox, mStepRadius, Color.GREEN);
		mSteps[16] = new Step(7 * mStepBox, 8 * mStepBox, mStepRadius, Color.RED);
		mSteps[17] = new Step(7 * mStepBox, 9 * mStepBox, mStepRadius, Color.YELLOW);
		mSteps[18] = new Step(7 * mStepBox, 10 * mStepBox, mStepRadius, Color.BLUE);
		mSteps[19] = new Step(7 * mStepBox, 11 * mStepBox, mStepRadius, Color.GREEN);
		mSteps[20] = new Step(6 * mStepBox, 11 * mStepBox, mStepRadius, Color.RED);
		mSteps[21] = new Step(5 * mStepBox, 11 * mStepBox, mStepRadius, Color.RED);
		mSteps[22] = new Step(5 * mStepBox, 10 * mStepBox, mStepRadius, Color.YELLOW);
		mSteps[23] = new Step(5 * mStepBox, 9 * mStepBox, mStepRadius, Color.BLUE);
		mSteps[24] = new Step(5 * mStepBox, 8 * mStepBox, mStepRadius, Color.GREEN);
		mSteps[25] = new Step(5 * mStepBox, 7 * mStepBox, mStepRadius, Color.RED);
		mSteps[26] = new Step(4 * mStepBox, 7 * mStepBox, mStepRadius, Color.YELLOW);
		mSteps[27] = new Step(3 * mStepBox, 7 * mStepBox, mStepRadius, Color.BLUE);
		mSteps[28] = new Step(2 * mStepBox, 7 * mStepBox, mStepRadius, Color.GREEN);
		mSteps[29] = new Step(1 * mStepBox, 7 * mStepBox, mStepRadius, Color.RED);
		mSteps[30] = new Step(1 * mStepBox, 6 * mStepBox, mStepRadius, Color.YELLOW);
		mSteps[31] = new Step(1 * mStepBox, 5 * mStepBox, mStepRadius, Color.YELLOW);
		mSteps[32] = new Step(2 * mStepBox, 5 * mStepBox, mStepRadius, Color.BLUE);
		mSteps[33] = new Step(3 * mStepBox, 5 * mStepBox, mStepRadius, Color.GREEN);
		mSteps[34] = new Step(4 * mStepBox, 5 * mStepBox, mStepRadius, Color.RED);
		mSteps[35] = new Step(5 * mStepBox, 5 * mStepBox, mStepRadius, Color.YELLOW);
		mSteps[36] = new Step(5 * mStepBox, 4 * mStepBox, mStepRadius, Color.BLUE);
		mSteps[37] = new Step(5 * mStepBox, 3 * mStepBox, mStepRadius, Color.GREEN);
		mSteps[38] = new Step(5 * mStepBox, 2 * mStepBox, mStepRadius, Color.RED);
		mSteps[39] = new Step(5 * mStepBox, 1 * mStepBox, mStepRadius, Color.YELLOW);
		// Exit path steps
		mSteps[40] = new Step(6 * mStepBox, 2 * mStepBox, mStepRadius, Color.WHITE, Color.BLUE);
		mSteps[41] = new Step(6 * mStepBox, 3 * mStepBox, mStepRadius, Color.WHITE, Color.BLUE);
		mSteps[42] = new Step(6 * mStepBox, 4 * mStepBox, mStepRadius, Color.WHITE, Color.BLUE);
		mSteps[43] = new Step(6 * mStepBox, 5 * mStepBox, mStepRadius, Color.WHITE, Color.BLUE);
		mSteps[44] = new Step(10 * mStepBox, 6 * mStepBox, mStepRadius, Color.WHITE, Color.GREEN);
		mSteps[45] = new Step(9 * mStepBox, 6 * mStepBox, mStepRadius, Color.WHITE, Color.GREEN);
		mSteps[46] = new Step(8 * mStepBox, 6 * mStepBox, mStepRadius, Color.WHITE, Color.GREEN);
		mSteps[47] = new Step(7 * mStepBox, 6 * mStepBox, mStepRadius, Color.WHITE, Color.GREEN);
		mSteps[48] = new Step(6 * mStepBox, 10 * mStepBox, mStepRadius, Color.WHITE, Color.RED);
		mSteps[49] = new Step(6 * mStepBox, 9 * mStepBox, mStepRadius, Color.WHITE, Color.RED);
		mSteps[50] = new Step(6 * mStepBox, 8 * mStepBox, mStepRadius, Color.WHITE, Color.RED);
		mSteps[51] = new Step(6 * mStepBox, 7 * mStepBox, mStepRadius, Color.WHITE, Color.RED);
		mSteps[52] = new Step(2 * mStepBox, 6 * mStepBox, mStepRadius, Color.WHITE, Color.YELLOW);
		mSteps[53] = new Step(3 * mStepBox, 6 * mStepBox, mStepRadius, Color.WHITE, Color.YELLOW);
		mSteps[54] = new Step(4 * mStepBox, 6 * mStepBox, mStepRadius, Color.WHITE, Color.YELLOW);
		mSteps[55] = new Step(5 * mStepBox, 6 * mStepBox, mStepRadius, Color.WHITE, Color.YELLOW);
		// Exit step
		mSteps[56] = new Step(6 * mStepBox, 6 * mStepBox, mStepRadius, Color.WHITE);
		
		// Nests
		/*mSteps[57] = new Step(9.5 * mStepBox, 2.5 * mStepBox, 3 * mStepRadius, Color.BLUE);
		mSteps[58] = new Step(9.5 * mStepBox, 9.5 * mStepBox, 3 * mStepRadius, Color.GREEN);
		mSteps[59] = new Step(2.5 * mStepBox, 9.5 * mStepBox, 3 * mStepRadius, Color.RED);
		mSteps[60] = new Step(2.5 * mStepBox, 2.5 * mStepBox, 3 * mStepRadius, Color.YELLOW);
		*/
		this.mNests[0]= new Nest(9.5 * mStepBox, 2.5 * mStepBox, 3 * mStepRadius, Color.BLUE);
		this.mNests[1]= new Nest(9.5 * mStepBox, 9.5 * mStepBox, 3 * mStepRadius, Color.GREEN);
		this.mNests[2]= new Nest(2.5 * mStepBox, 9.5 * mStepBox, 3 * mStepRadius, Color.RED);
		this.mNests[3]= new Nest(2.5 * mStepBox, 2.5 * mStepBox, 3 * mStepRadius, Color.YELLOW);
	}
	
	private void drawBackground(Canvas canvas) {
		int step;
		int nest;
		Paint paint = new Paint();
		canvas.drawColor(Color.MAGENTA);
		
		// Draw steps
		for (step = 0; step < mSteps.length; step++)
		{
			mSteps[step].draw(canvas);
		}
		
		// Draw nests
		for (nest = 0; nest < mNests.length; nest++)
		{
			mNests[nest].draw(canvas);
		}
		
		// Draw center
		paint.setColor(Color.BLUE);
		canvas.drawCircle((float)(6 * mStepBox), (float)(6 * mStepBox), (float)(mStepRadius * 0.8), paint);
		paint.setColor(Color.GREEN);
		canvas.drawCircle((float)(6 * mStepBox), (float)(6 * mStepBox), (float)(mStepRadius * 0.8 * 0.8), paint);
		paint.setColor(Color.RED);
		canvas.drawCircle((float)(6 * mStepBox), (float)(6 * mStepBox), (float)(mStepRadius * 0.8 * 0.6), paint);
		paint.setColor(Color.YELLOW);
		canvas.drawCircle((float)(6 * mStepBox), (float)(6 * mStepBox), (float)(mStepRadius * 0.8 * 0.4), paint);
		paint.setColor(Color.MAGENTA);
		canvas.drawCircle((float)(6 * mStepBox), (float)(6 * mStepBox), (float)(mStepRadius * 0.8 * 0.2), paint);
	}

	private void drawPieces(Canvas canvas) {
		int step;
		int nest;
		
		for (step = 0; step < mSteps.length; step++)
		{
			mSteps[step].drawPieces(canvas);
		}
		for (nest = 0; nest < mNests.length; nest++)
		{
			mNests[nest].drawPieces(canvas);
		}
	}
	
	private void drawDice(Canvas canvas) {
		mDice.draw(canvas);
	}
	
	private class Piece {
		private int mColor;
		private boolean mSelected;
		private BoardTile mBoardTile;
		private int mPosition;
		
		public Piece(BoardTile boardTile, int color, boolean selected, int position)
		{
			this.mColor = color;
			this.mBoardTile = boardTile;
			this.mSelected = selected;
			this.mPosition = position;
		}
		
		public Piece(BoardTile boardTile, int color)
		{
			this(boardTile, color, false, 0);
		}
		
		public Piece(Piece piece) {
			this(piece.mBoardTile, piece.mColor, piece.mSelected, piece.mPosition);
		}

		public int getColor()
		{
			return this.mColor;
		}
		
		public void select()
		{
			this.mSelected = true;
		}

		public void deselect() throws Exception
		{
			if(this.mSelected == false) throw new Exception("Piece not selected, can't deselect");
			this.mSelected = false;
		}
		
		public boolean getSelected()
		{
			return this.mSelected;
		}

		public void setTile(BoardTile tile) 
		{
			this.mBoardTile = tile;
		}

		public void move(int diceValue) throws Exception
		{
			Step step;
			
			step = getNextStep(diceValue);
			this.mPosition += diceValue;
			this.mBoardTile.remove(this);
			step.putPiece(this);
			this.setTile(step);
		}

		public Step getNextStep(int diceValue) {
			int newIndex = this.mPosition;
			
			newIndex += diceValue;
			if (newIndex < 41)
			{
				newIndex = (newIndex + offset(this.mColor)) % 41;
				return mSteps[newIndex];
			}
			else //if (this.mPosition < 45)
			{
				newIndex = (newIndex + entranceOffset(this.mColor)) % 45;
				return mSteps[newIndex];
			}
		}
		
		private int offset(int color)
		{
			int offset = 0;
			switch (this.mColor)
			{
			case Color.YELLOW:
				offset += 10;
			case Color.RED:
				offset += 10;
			case Color.GREEN:
				offset += 10;
			}
			
			return offset;
		}
		
		private int entranceOffset(int color)
		{
			int offset = 0;
			switch (this.mColor)
			{
			case Color.YELLOW:
				offset += 4;
			case Color.RED:
				offset += 4;
			case Color.GREEN:
				offset += 4;
			}
			
			return offset;
		}
	}
	
	private abstract class BoardTile {
		private double x;
		private double y;
		private double radius;
		private int color;
		
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

		public abstract Piece selectPiece(int color);
		public abstract Piece removeSelectedPiece(int color) throws Exception;
		public abstract void remove(Piece piece) throws Exception;
		public abstract void deselect() throws Exception;
		public abstract boolean free();
	}
	
	private class Nest extends BoardTile {
		private Piece mPieces[] = new Piece[N_PIECES_PER_PLAYER];
		private int mNPieces;
		
		public Nest(double x, double y, double radius, int color) 
		{
			super(x, y, radius, color);
			
			initNest(color);
		}

		private void initNest(int color)
		{
			int piece;
			for (piece = 0; piece < mPieces.length; piece++)
			{
				mPieces[piece] = new Piece(this, color);
				mNPieces++;
			}
		}

		@Override
		public Piece selectPiece(int color) {
			Piece piece;
			int pieceIdx;
			for (pieceIdx = 0; pieceIdx < this.mNPieces; pieceIdx++)
			{
				piece = mPieces[pieceIdx];
				if (piece.getColor() == color) 
				{
					piece.select();
					return piece;
				}
			}
			return null;
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
		public Piece removeSelectedPiece(int color) {
			Piece piece;
			int pieceIdx = 0;
			while (pieceIdx < this.mNPieces &&
					this.mPieces[pieceIdx].getColor() != color)
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
		
		public void drawPieces(Canvas canvas) {
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
					edgeRadius = mPieceRadius * selectMultiplier;
					centerRadius = edgeRadius - EDGE_WIDTH * mPieceRadius;
					paint.setColor(Color.WHITE);
					canvas.drawCircle((float)x, (float)y, (float)edgeRadius, paint);
					paint.setColor(piece.getColor());
					canvas.drawCircle((float)x, (float)y, (float)centerRadius, paint);
				}
			}
		}
	}
	
	private class Step extends BoardTile {
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
	
	private class Dice {
		int mValue;
		Random mRand;
		RectF mRect;

		public Dice()
		{
			initDice();
		}

		public void reset() 
		{
			setPlayer(mPlayer);
			mValue = 0;
		}

		private void initDice()
		{
			mRect = new RectF();
			setPlayer(mPlayer);
			mRand = new Random();
			this.rollDice();
		}

		public void rollDice()
		{
			mValue = mRand.nextInt(6) + 1;
		}

		public void setPlayer(Player player)
		{
			mValue = 0;

			switch (player) {
			case BLUE:
				mRect.set((float)(10.5 * mStepBox), (float)(0.5 * mStepBox), (float)(11.5 * mStepBox), (float)(1.5 * mStepBox));
				break;
			case GREEN:
				mRect.set((float)(10.5 * mStepBox), (float)(10.5 * mStepBox), (float)(11.5 * mStepBox), (float)(11.5 * mStepBox));
				break;
			case RED:
				mRect.set((float)(0.5 * mStepBox), (float)(10.5 * mStepBox), (float)(1.5 * mStepBox), (float)(11.5 * mStepBox));
				break;
			case YELLOW:
				mRect.set((float)(0.5 * mStepBox), (float)(0.5 * mStepBox), (float)(1.5 * mStepBox), (float)(1.5 * mStepBox));
				break;
			default:
				mRect.set((float)(10.5 * mStepBox), (float)(0.5 * mStepBox), (float)(11.5 * mStepBox), (float)(1.5 * mStepBox));
			}
		}

		public void draw(Canvas canvas)
		{
			Paint paint = new Paint();
			paint.setColor(Color.WHITE);
			float rx = (float)(mStepBox * 0.2);
			float ry = (float)(mStepBox * 0.2);

			canvas.drawRoundRect(mRect, rx, ry, paint);
			paint.setColor(Color.BLACK);

			switch (mValue) {
			case 1:
				canvas.drawCircle(mRect.centerX(), mRect.centerY(), rx / 2, paint);
				break;
			case 2:
				canvas.drawCircle(mRect.centerX() - mRect.width() / 5, mRect.centerY() - mRect.width() / 5, rx / 2, paint);
				canvas.drawCircle(mRect.centerX() + mRect.width() / 5, mRect.centerY() + mRect.width() / 5, rx / 2, paint);
				break;
			case 3:
				canvas.drawCircle(mRect.centerX(), mRect.centerY(), rx / 2, paint);
				canvas.drawCircle(mRect.centerX() - mRect.width() / 4, mRect.centerY() - mRect.width() / 4, rx / 2, paint);
				canvas.drawCircle(mRect.centerX() + mRect.width() / 4, mRect.centerY() + mRect.width() / 4, rx / 2, paint);
				break;
			case 4:
				canvas.drawCircle(mRect.centerX() - mRect.width() / 5, mRect.centerY() - mRect.width() / 5, rx / 2, paint);
				canvas.drawCircle(mRect.centerX() + mRect.width() / 5, mRect.centerY() + mRect.width() / 5, rx / 2, paint);
				canvas.drawCircle(mRect.centerX() + mRect.width() / 5, mRect.centerY() - mRect.width() / 5, rx / 2, paint);
				canvas.drawCircle(mRect.centerX() - mRect.width() / 5, mRect.centerY() + mRect.width() / 5, rx / 2, paint);
				break;
			case 5:
				canvas.drawCircle(mRect.centerX(), mRect.centerY(), rx / 2, paint);
				canvas.drawCircle(mRect.centerX() - mRect.width() / 4, mRect.centerY() - mRect.width() / 4, rx / 2, paint);
				canvas.drawCircle(mRect.centerX() + mRect.width() / 4, mRect.centerY() + mRect.width() / 4, rx / 2, paint);
				canvas.drawCircle(mRect.centerX() + mRect.width() / 4, mRect.centerY() - mRect.width() / 4, rx / 2, paint);
				canvas.drawCircle(mRect.centerX() - mRect.width() / 4, mRect.centerY() + mRect.width() / 4, rx / 2, paint);
				break;
			case 6:
				canvas.drawCircle(mRect.centerX(), mRect.centerY() - mRect.width() / 5, rx / 2, paint);
				canvas.drawCircle(mRect.centerX(), mRect.centerY() + mRect.width() / 5, rx / 2, paint);
				canvas.drawCircle(mRect.centerX() - mRect.width() / 4, mRect.centerY() - mRect.width() / 5, rx / 2, paint);
				canvas.drawCircle(mRect.centerX() + mRect.width() / 4, mRect.centerY() - mRect.width() / 5, rx / 2, paint);
				canvas.drawCircle(mRect.centerX() - mRect.width() / 4, mRect.centerY() + mRect.width() / 5, rx / 2, paint);
				canvas.drawCircle(mRect.centerX() + mRect.width() / 4, mRect.centerY() + mRect.width() / 5, rx / 2, paint);
				break;
			default:
				break;
			}
		}

		/*		public class Player {
			int mColor;
			public Player() {
				mColor = Color.BLUE;
			}

			switch
		}*/

		public boolean pointInDice(float x, float y) 
		{
			return mRect.contains(x, y);
		}

		public void clicked() 
		{
			this.rollDice();
		}

		public int getValue() 
		{
			return mValue;
		}
	}
	
	private void registerClick(Nest nest) throws Exception
	{
		Piece piece;
		Step nextStep = null;
		
		piece = nest.selectPiece(this.boardViewColor);
		if (piece == null) return;

		switch (this.mState) 
		{
		case NONE_SELECTED:
				this.mSelectedTile = nest;
				this.mState = State.ONE_SELECTED;
				nextStep = nest.findNextStep(this.mDice.getValue());
			break;
		case ONE_SELECTED:
				this.mSelectedTile.deselect();
				nest.selectPiece(this.boardViewColor);
				this.mSelectedTile = nest;
				nextStep = nest.findNextStep(this.mDice.getValue());
			break;
		case BOTH_SELECTED:
			throw new Exception("WTF, selected two Pieces? Give it a break!");
		}

		if (nextStep != null)
		{
			this.mNextStepGlobal.unsetNextStep();
			this.mNextStepGlobal = nextStep;
			this.mNextStepGlobal.setNextStep();
		}
	}
	
	private void registerClick(Step newStep) throws Exception
	{
		Piece piece;
		Step nextStep = null;

		piece = newStep.selectPiece(this.boardViewColor);
		
		switch (this.mState) 
		{
		case NONE_SELECTED:
			if (piece != null)
			{
				this.mSelectedTile = newStep;
				this.mState = State.ONE_SELECTED;
				nextStep = piece.getNextStep(mDice.getValue());// newStep.findNextStep(this.mDice.getValue());
			}
			break;
		case ONE_SELECTED:
			if (piece != null)
			{
				this.mSelectedTile.deselect();
				newStep.selectPiece(this.boardViewColor);
				this.mSelectedTile = newStep;
				nextStep = piece.getNextStep(mDice.getValue());//= newStep.findNextStep(this.mDice.getValue());
			}
			else if (newStep.free())
			{
				piece = this.mSelectedTile.selectPiece(this.boardViewColor);
				if (piece.getNextStep(mDice.getValue()).equals(newStep))
				{
					piece.move(mDice.getValue());
					piece.deselect();
					this.mSelectedTile = null;
					this.mState = State.NONE_SELECTED;
					this.switchPlayer();
				}
			}
			break;
		case BOTH_SELECTED:
			throw new Exception("WTF, selected two Pieces? Give it a break!");
		}

		if (nextStep != null)
		{
			this.mNextStepGlobal.unsetNextStep();
			this.mNextStepGlobal = nextStep;
			this.mNextStepGlobal.setNextStep();
		}
	}

	private int findNextStep() {
		int offset = 0;
		int nestBase = 0;
		int nest = 0;
		int currentStep;
		int nextStep = 0;
		int roll = mDice.getValue();
		switch (mPlayer) {
		case BLUE:
			offset = 1;
			nestBase = 40;
			nest = 57;
			break;
		case GREEN:
			offset = 11;
			nestBase = 44;
			nest = 58;
			break;
		case RED:
			offset = 21;
			nestBase = 48;
			nest = 59;
			break;
		case YELLOW:
			offset = 31;
			nestBase = 52;
			nest = 60;
			break;
		}
		Toast toast = Toast.makeText(getContext(), 
				"offset: " + offset +
				"\nnestBase: " + nestBase +
				"\nnest: " + nest, Toast.LENGTH_SHORT);
		toast.show();
		/*
		if (mSelectedPieceStep == nest)
		{
			nextStep = roll + offset - 1;
			return nextStep;
		} 
		else if (mSelectedPieceStep == offset - 1)
		{
			if (roll == 5)
			{
				nextStep = 56;
				return nextStep;
			}
			else if (roll == 6)
			{
				nextStep = nestBase + 3;
				return nextStep;
			}
			else
			{
				nextStep = nestBase - 1 + roll;
				return nextStep;
			}
		}
		else if ((mSelectedPieceStep == offset - 2 &&
				 roll == 6) ||
				 (offset == 0 &&
				 mSelectedPieceStep == 39))
		{
			nextStep = 56;
			return nextStep;
		}
		else if (mSelectedPieceStep >= nestBase &&
				 mSelectedPieceStep < nestBase + 4)
		{
			if (mSelectedPieceStep == nestBase + 3 &&
				roll == 6)
			{
				nextStep = offset;
				return nextStep;
			}
			else if (mSelectedPieceStep + roll == nestBase + 4)
			{
				nextStep = 56;
				return nextStep;
			}
			else
			{
				nextStep = nestBase + 4 - Math.abs(mSelectedPieceStep + roll - (nestBase + 4));
				return nextStep;
			}
		}
				
		currentStep = (mSelectedPieceStep - offset) % 40;
		if (currentStep < 0)
		{
			currentStep += 40;
		}
		
		if (currentStep + roll < 40)
		{
			nextStep = (currentStep + roll + offset) % 40;
			if (nextStep < 0)
			{
				nextStep += 40;
			}
		}
		else
		{
			nextStep = nestBase + (currentStep + roll) - 40;
		}
		
		toast = Toast.makeText(getContext(), 
				"nextStep: " + nextStep +
				"\nnestBase: " + nestBase +
				"\ncurrentStep: " + currentStep, Toast.LENGTH_SHORT);
		toast.show();
		*/
		return nextStep;
	}


	private void switchPlayer() {
		switch (mPlayer) {
		case BLUE:
			mPlayer = Player.GREEN;
			boardViewColor = Color.GREEN;
			break;
		case GREEN:
			mPlayer = Player.RED;
			boardViewColor = Color.RED;
			break;
		case RED:
			mPlayer = Player.YELLOW;
			boardViewColor = Color.YELLOW;
			break;
		case YELLOW:
			mPlayer = Player.BLUE;
			boardViewColor = Color.BLUE;
			break;
		}
		mDice.setPlayer(mPlayer);
	}

	@Override
	public boolean onTouch(View view, MotionEvent event)
	{
		float x;
		float y;
		boolean found = false;
		Step step;
		Nest nest;
		int a = event.getAction();
		
		if (a == MotionEvent.ACTION_DOWN)
		{
			x = event.getX();
			y = event.getY();
			step = findStep(x, y);
			if (step != null)
			{
				try {
					registerClick(step);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				found = true;
			}
			else 
			{
				nest = findNest(x, y);
				if (nest != null)
				{
					try {
						registerClick(nest);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					found = true;
				}
				else if (mDice.pointInDice(x,y))
				{
					mDice.clicked();
					found = true;
				}
			}
			
			this.invalidate();
		}
		
		return found;
	}
	
	private Step findStep(float x, float y)
	{
		int step;
		
		for (step = 0; step < mSteps.length; step++)
		{
			if (mSteps[step].pointInside(x, y))
			{
				return mSteps[step];
			}
		}
		return null;
	}
	
	private Nest findNest(float x, float y)
	{
		int nest;
		
		for (nest = 0; nest < mNests.length; nest++)
		{
			if (mNests[nest].pointInside(x, y))
			{
				return mNests[nest];
			}
		}
		return null;
	}
}

