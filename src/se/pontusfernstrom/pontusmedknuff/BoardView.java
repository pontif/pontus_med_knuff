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
	
	private final int N_STEPS = 61;
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
	private int mSelectedPieceStep;
	private Player mPlayer;
	private int mColor;
	private Dice mDice;
	private int mNextStep;
	
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
		this.mSelectedPieceStep = ILLEGAL_STEP;
		this.mPlayer = Player.BLUE;
		this.mDice = new Dice();
		this.mColor = Color.BLUE;
	}
	
	
	private void resetBoardView() {
		int i;
		
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
		mSteps[57] = new Step(9.5 * mStepBox, 2.5 * mStepBox, 3 * mStepRadius, Color.BLUE);
		mSteps[58] = new Step(9.5 * mStepBox, 9.5 * mStepBox, 3 * mStepRadius, Color.GREEN);
		mSteps[59] = new Step(2.5 * mStepBox, 9.5 * mStepBox, 3 * mStepRadius, Color.RED);
		mSteps[60] = new Step(2.5 * mStepBox, 2.5 * mStepBox, 3 * mStepRadius, Color.YELLOW);
		
		for (i = 0; i < N_PIECES_PER_PLAYER; i++)
		{
			mPieces[i] = new Piece(57, Color.BLUE);
			mSteps[57].putPiece(mPieces[i]);
			mPieces[N_PIECES_PER_PLAYER + i] = new Piece(58, Color.GREEN);
			mSteps[58].putPiece(mPieces[N_PIECES_PER_PLAYER + i]);
			mPieces[2 * N_PIECES_PER_PLAYER + i] = new Piece(59, Color.RED);
			mSteps[59].putPiece(mPieces[2 * N_PIECES_PER_PLAYER + i]);
			mPieces[3 * N_PIECES_PER_PLAYER + i] = new Piece(60, Color.YELLOW);
			mSteps[60].putPiece(mPieces[3 * N_PIECES_PER_PLAYER + i]);
		}
	}
	
	private void drawBackground(Canvas canvas) {
		int step;
		Paint paint = new Paint();
		canvas.drawColor(Color.MAGENTA);
		BoardView view = (BoardView)findViewById(R.id.boardview_content);
		int width = view.getWidth();
		
		for (step = 0; step < mSteps.length; step++)
		{
			mSteps[step].draw(canvas);
		}
		
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
		
		/*
	    Toast message = Toast.makeText(this.getContext(), "view.getWidth(): " + view.getWidth(), Toast.LENGTH_SHORT);       
	    message.show();  
		message = Toast.makeText(this.getContext(), "view.getHeight(): " + view.getHeight(), Toast.LENGTH_SHORT);       
	    message.show();*/
	    
	}

	private void drawPieces(Canvas canvas) {
		int step;
		
		for (step = 0; step < mSteps.length; step++)
		{
			mSteps[step].drawPieces(canvas);
		}
	}
	private void drawDice(Canvas canvas) {
		mDice.draw(canvas);
	}
	
	private class Piece {
		private int step;
		private int color;
		private boolean selected;
		
		public Piece(int step, int color)
		{
			this.color = color;
			this.step = step;
			this.selected = false;
		}
		
		public int getColor()
		{
			return this.color;
		}
		
		public void select()
		{
			this.selected = true;
		}

		public void deselect() 
		{
			this.selected = false;
		}
		
		public boolean getSelected()
		{
			return this.selected;
		}
	}
	
	private class BoardTile {
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
		
		public boolean pointInStep(double x, double y) {
			double xDist = this.x - x;
			double yDist = this.y - y;
			return Math.sqrt(xDist * xDist + yDist * yDist) < this.radius;
		}
	}
	
	private class Nest extends BoardTile {
		
		public Nest(double x, double y, double radius, int color) 
		{
			super(x, y, radius, color);
		}
	}
	
	private class Step extends BoardTile {
		private int edgeColor;
		private Piece pieces[] = new Piece[N_PIECES_PER_PLAYER];
		private int nPieces;
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
			int i;
			this.edgeColor = edgeColor;
			this.mNextStep = false;
			for (i = 0; i < N_PIECES_PER_PLAYER; i++) pieces[i] = null;
		}
		
		public void putPiece(Piece piece)
		{
			assert(nPieces < N_PIECES_PER_PLAYER);
			this.pieces[this.nPieces++] = piece;
		}
		
		public boolean removePiece(int color)
		{
			int pieceIdx = 0;
			while (this.pieces[pieceIdx].getColor() != color) pieceIdx++;
			if (pieceIdx >= nPieces) {
				return false;
			}
			while (pieceIdx < nPieces) this.pieces[pieceIdx] = this.pieces[++pieceIdx];
			this.pieces[pieceIdx] = null;
			this.nPieces--;
			return true;
		}
		
		public Piece pickPiece(int color)
		{
			Piece piece;
			int pieceIdx = 0;
			while (pieceIdx < this.nPieces &&
					this.pieces[pieceIdx].getColor() != color)
			{
				pieceIdx++;
			}
			if (pieceIdx >= nPieces) {
				return null;
			} else {
				piece = this.pieces[pieceIdx];
			}
			while (pieceIdx < nPieces - 1) 
			{
				this.pieces[pieceIdx] = this.pieces[++pieceIdx];
			}
			this.pieces[pieceIdx] = null;
			this.nPieces--;
			return piece;
		}
		
		public void draw(Canvas canvas) {
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
		
		public void drawPieces(Canvas canvas) {
			Paint paint;
			Piece piece;
			int pieceIdx;
			double x;
			double y;
			double edgeRadius;
			double centerRadius;
			double selectMultiplier;
			
			if (this.nPieces > 0)
			{
				paint = new Paint();
				for (pieceIdx = 0; pieceIdx < nPieces; pieceIdx++)
				{
					piece = this.pieces[pieceIdx];
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

		public boolean queryColor(int color) {
			int pieceIdx;
			for (pieceIdx = 0; pieceIdx < nPieces; pieceIdx++)
			{
				if (this.pieces[pieceIdx].getColor() == color)
				{
					return true;
				}
			}
			return false;
		}

		public void selectColor(int color) {
			int pieceIdx;
			for (pieceIdx = 0; pieceIdx < nPieces; pieceIdx++)
			{
				if (this.pieces[pieceIdx].getColor() == color)
				{
					this.pieces[pieceIdx].select();
					return;
				}
			}
		}

		public void deselect() {
			int pieceIdx = 0;
			for (pieceIdx = 0; pieceIdx < nPieces; pieceIdx++)
			{
				if (this.pieces[pieceIdx].getSelected())
				{
					this.pieces[pieceIdx].deselect();
					return;
				}
			}
		}

		public boolean free() {
			return this.nPieces < this.pieces.length;
		}

		public void setNextStep() {
			this.mNextStep = true;
		}

		public void unsetNextStep() {
			this.mNextStep = false;
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
		
		public void reset() {
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

		public boolean pointInDice(float x, float y) {
			return mRect.contains(x, y);
		}

		public void touched() {
			this.rollDice();
		}

		public int getValue() {
			return mValue;
		}
	}
	
	private void registerClick(int stepIdx)
	{
		Piece piece;
		Step newStep = mSteps[stepIdx];
		Step oldStep;
		int nextStep = Integer.MAX_VALUE;
		//Toast message = Toast.makeText(getContext(), "stepIdx: " + stepIdx, Toast.LENGTH_SHORT);
		//message.show();
		switch(this.mState) {
		case NONE_SELECTED:
			if (newStep.queryColor(this.mColor))
			{
				newStep.selectColor(this.mColor);
				this.mSelectedPieceStep = stepIdx;
				this.mState = State.ONE_SELECTED;
				nextStep = this.findNextStep();
				//Toast message2 = Toast.makeText(getContext(), "State " + this.state + ", Selected " + step, Toast.LENGTH_SHORT);
				//message2.show();
			}
			break;
		case ONE_SELECTED:
			oldStep = mSteps[mSelectedPieceStep];
			if (newStep.queryColor(this.mColor))
			{
				oldStep.deselect();
				newStep.selectColor(this.mColor);
				mSelectedPieceStep = stepIdx;
				nextStep = this.findNextStep();
			}
			else if (newStep.free())
			{
				oldStep.deselect();
				piece = oldStep.pickPiece(this.mColor);
				newStep.putPiece(piece);
				piece.step = stepIdx;
				//Toast message2 = Toast.makeText(getContext(), "Moving piece from " + selectedPieceStep + " to " + step, Toast.LENGTH_SHORT);
				this.mSelectedPieceStep = ILLEGAL_STEP;
				this.mState = State.NONE_SELECTED;
				this.switchPlayer();
				//message2.show();
			}
			break;
		case BOTH_SELECTED:
			assert(false);
		}

		if (nextStep != Integer.MAX_VALUE)
		{
			mSteps[mNextStep].unsetNextStep();
			this.mNextStep = nextStep;
			mSteps[mNextStep].setNextStep();
		}
		//this.invalidate();
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
		return nextStep;
	}


	private void switchPlayer() {
		switch (mPlayer) {
		case BLUE:
			mPlayer = Player.GREEN;
			mColor = Color.GREEN;
			break;
		case GREEN:
			//mPlayer = Player.GREEN;
			//mColor = Color.GREEN;
			mPlayer = Player.RED;
			mColor = Color.RED;
			break;
		case RED:
			mPlayer = Player.YELLOW;
			mColor = Color.YELLOW;
			break;
		case YELLOW:
			mPlayer = Player.BLUE;
			mColor = Color.BLUE;
			break;
		}
		mDice.setPlayer(mPlayer);
	}

	@Override
	public boolean onTouch(View view, MotionEvent event)
	{
		int step = 0;
		float x;
		float y;
		boolean found = false;
		int a = event.getAction();
		
		if (a == MotionEvent.ACTION_DOWN)
		{
			x = event.getX();
			y = event.getY();
			found = mSteps[step].pointInStep(x, y);
			while (!found && step < mSteps.length - 1)
			{
				found = mSteps[++step].pointInStep(x, y);
			}
			if (found)
			{
				registerClick(step);
			}
			
			if (mDice.pointInDice(x,y))
			{
				mDice.touched();
			}
			
			this.invalidate();
			//Toast message = Toast.makeText(getContext(), "Wohoo event.getAction(): " + a + "\nevent.getX(): " + event.getX() + "\nevent.getY(): " + event.getX() + "\nstep: "+ step, Toast.LENGTH_SHORT);       
			//message.show();
		}
		
		return found;
	}
}

