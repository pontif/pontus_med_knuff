package se.pontusfernstrom.pontusmedknuff;

import java.util.Random;

import se.pontusfernstrom.pontusmedknuff.GameState.RoundState;
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

public class BoardView extends View implements OnTouchListener {
	
	// Physical constants
	private final int N_STEPS = 57;
	private final int N_NESTS = 4;
	private final int N_STEPS_PER_SIDE = 11;
	private final float EDGE_WIDTH = 0.2f;
	private final float STEP_FRACTION_OF_BOX = 0.9f; 
	private final float PIECE_FRACTION_OF_STEP = 0.4f;
	
	// Game constants
	private final int MAX_N_PLAYERS = 4;
	
	// Physical dimensions
	private int mWidth;
	private int mHeight;
	private float mStepBox;
	private double mStepRadius;
	private double mPieceRadius;
	
	private BoardTile mSelectedTile;
	private Step mNextStepGlobal;
	private int boardViewColor;
	private Dice mDice;
	
	private Bitmap  mBitmap;
	
	// Data containers
	private Player[] mPlayers = new Player[MAX_N_PLAYERS];
	private Step[] mSteps = new Step[N_STEPS];
	private Nest[] mNests = new Nest[N_NESTS];

	// Game state machine
	private GameState mState;
	
	// User defined variables
	private int mNPlayers;
	
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
		//if (w != h) throw new Exception("Canvas has to be square");
			//try {
			//	throw new Exception("Canvas has to be square");
			//} catch (Exception e) {
			//	e.printStackTrace();
			//}
		mStepBox = mWidth / (N_STEPS_PER_SIDE + 1);
		mStepRadius = mStepBox * STEP_FRACTION_OF_BOX / 2;
		mPieceRadius = mStepRadius * PIECE_FRACTION_OF_STEP;
		resetBoardView();
		//throw new RuntimeException("test");
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		drawBackground(canvas);
		
		drawPieces(canvas);
		
		drawDice(canvas);
	}
	
	private void initBoardView() {
		//int i;
		this.mNextStepGlobal = null;
		this.mDice = new Dice();
		this.boardViewColor = Color.BLUE;
	}
	
	
	private void resetBoardView() {
		int i;
		//mDice.reset();
		
		// Perimeter steps
		mSteps[0] = new Step(6 * mStepBox, 1 * mStepBox, mStepRadius, Color.BLUE);
		mSteps[1] = new Step(7 * mStepBox, 1 * mStepBox, mStepRadius, Color.BLUE);
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
		this.mNests[0]= new Nest(9.5 * mStepBox, 2.5 * mStepBox, 3 * mStepRadius, Color.BLUE, null);
		this.mNests[1]= new Nest(9.5 * mStepBox, 9.5 * mStepBox, 3 * mStepRadius, Color.GREEN, null);
		this.mNests[2]= new Nest(2.5 * mStepBox, 9.5 * mStepBox, 3 * mStepRadius, Color.RED, null);
		this.mNests[3]= new Nest(2.5 * mStepBox, 2.5 * mStepBox, 3 * mStepRadius, Color.YELLOW, null);

		for (i = 0; i < mNPlayers; i++) {
			mPlayers[i] = new Player(i, mNests[i]);
			this.mNests[i].setPlayer(mPlayers[i]);
		}
		this.mState = new GameState(mPlayers[0]);
	}
	
	private void drawBackground(Canvas canvas) {
		int step;
		int nest;
		float center = 6 * mStepBox;
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
		canvas.drawCircle(center, center, (float)(mStepRadius * 0.8), paint);
		paint.setColor(Color.GREEN);
		canvas.drawCircle(center, center, (float)(mStepRadius * 0.8 * 0.8), paint);
		paint.setColor(Color.RED);
		canvas.drawCircle(center, center, (float)(mStepRadius * 0.8 * 0.6), paint);
		paint.setColor(Color.YELLOW);
		canvas.drawCircle(center, center, (float)(mStepRadius * 0.8 * 0.4), paint);
		paint.setColor(Color.MAGENTA);
		canvas.drawCircle(center, center, (float)(mStepRadius * 0.8 * 0.2), paint);
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
	
	public class Piece {
		private Player mPlayer;
		private boolean mSelected;
		private BoardTile mBoardTile;
		private int mPosition;
		
		public Piece(BoardTile boardTile, Player player, boolean selected, int position)
		{
			this.mPlayer = player;
			this.mBoardTile = boardTile;
			this.mSelected = selected;
			this.mPosition = position;
		}
		
		public Piece(Piece piece) {
			this(piece.mBoardTile, piece.mPlayer, piece.mSelected, piece.mPosition);
		}
		
		public Piece(BoardTile boardTile, Player player) {
			this(boardTile, player, false, 0);
		}

		public Player getPlayer()
		{
			return mPlayer;
		}
		
		public void select()
		{
			mSelected = true;
		}

		public void deselect() throws Exception
		{
			if(mSelected == false) throw new Exception("Piece not selected, can't deselect");
			mSelected = false;
		}
		
		public boolean getSelected()
		{
			return mSelected;
		}

		public void setTile(BoardTile tile) 
		{
			mBoardTile = tile;
		}

		public void move(int diceValue) throws Exception
		{
			Step step;
			
			step = this.getNextStep(diceValue);
			mPosition += diceValue;
			mBoardTile.remove(this);
			if (step.equals(mSteps[56])) {
				this.setTile(null);
			}
			else
			{
				step.putPiece(this);
				this.setTile(step);
			}
		}

		public Step getNextStep(int diceValue) {
			int newIndex = mPosition;
			
			newIndex += diceValue;
			if (newIndex < 41)
			{
				newIndex = (newIndex + mPlayer.getPerimeterOffset()) % 40;
				return mSteps[newIndex];
			}
			else if (newIndex < 45)
			{
				newIndex = newIndex + mPlayer.getEntranceOffset() - 1;
				return mSteps[newIndex];
			}
			else if (newIndex == 45)
			{
				return mSteps[56];
			}
			else return mSteps[mPosition];
		}
		
		public void moveToNest() throws Exception
		{
			Nest nest = mPlayer.getNest();
			this.mPosition = 0;
			this.mBoardTile.remove(this);
			nest.putPiece(this);
			this.setTile(nest);
			this.deselect();
		}
		/*
		private Nest getNest() throws Exception
		{
			switch (mPlayer.getName())
			{
			case PLAYER_1:
				return mNests[0];
			case PLAYER_2:
				return mNests[1];
			case PLAYER_3:
				return mNests[2];
			case PLAYER_4:
				return mNests[3];
			default:
				throw new Exception("Colour desn't exist");
			}
		}
		*/
	}
	
	public class Dice {
		int mValue;
		Random mRand;
		RectF mRect;

		public Dice()
		{
			initDice();
		}

		/*
		public void reset(Player player) 
		{
			setPlayer(player);
			mValue = 0;
		}
*/
		private void initDice()
		{
			mRect = new RectF();
			setPlayer(mState.getPlayer());
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

			switch (player.getIndex()) {
			case 1:
				mRect.set(10.5f * mStepBox,  0.5f * mStepBox, 11.5f * mStepBox,  1.5f * mStepBox);
				break;
			case 2:
				mRect.set(10.5f * mStepBox, 10.5f * mStepBox, 11.5f * mStepBox, 11.5f * mStepBox);
				break;
			case 3:
				mRect.set( 0.5f * mStepBox, 10.5f * mStepBox,  1.5f * mStepBox, 11.5f * mStepBox);
				break;
			case 4:
				mRect.set( 0.5f * mStepBox,  0.5f * mStepBox,  1.5f * mStepBox,  1.5f * mStepBox);
				break;
			default:
				mRect.set(10.5f * mStepBox,  0.5f * mStepBox, 11.5f * mStepBox,  1.5f * mStepBox);
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

		switch (mState.getRoundState()) 
		{
		case NONE_SELECTED:
			if (piece.getPlayer().equals(mState.getPlayer()));
			{
				this.mSelectedTile = nest;
				mState.setRoundState(RoundState.ONE_SELECTED);
				nextStep = piece.getNextStep(this.mDice.getValue());
			}
			break;
		case ONE_SELECTED:
				this.mSelectedTile.deselect();
				nest.selectPiece(this.boardViewColor);
				this.mSelectedTile = nest;
				nextStep = piece.getNextStep(this.mDice.getValue());
		}

		if (nextStep != null)
		{
			if (this.mNextStepGlobal != null) 
				this.mNextStepGlobal.unsetNextStep();
			this.mNextStepGlobal = nextStep;
			this.mNextStepGlobal.setNextStep();
		}
	}
	
	private void registerClick(Step newStep) throws Exception
	{
		Piece piece;
		Step nextStep = null;
		Player player = mState.getPlayer();

		piece = newStep.selectPiece(this.boardViewColor);
		
		switch (mState.getRoundState()) 
		{
		case NONE_SELECTED:
			
			if ((piece != null) && piece.getPlayer().equals(player))
			{
				this.mSelectedTile = newStep;
				mState.setRoundState(RoundState.ONE_SELECTED);
				nextStep = piece.getNextStep(mDice.getValue());// newStep.findNextStep(this.mDice.getValue());
			}
			break;
		case ONE_SELECTED:
			
			if (piece != null)
			{
				if (piece.getPlayer().equals(player))
				{
					this.mSelectedTile.deselect();
					newStep.selectPiece(this.boardViewColor);
					this.mSelectedTile = newStep;
					nextStep = piece.getNextStep(mDice.getValue());//= newStep.findNextStep(this.mDice.getValue());
				}
			}
			else if (newStep.free())
			{
				piece = this.mSelectedTile.selectPiece(this.boardViewColor);
				if (piece != null && piece.getNextStep(mDice.getValue()).equals(newStep))
				{
					piece.move(mDice.getValue());
					piece.deselect();
					this.mSelectedTile = null;
					mState.setRoundState(RoundState.NONE_SELECTED);
					this.switchPlayer();
				}
			}
			else
			{
				piece = newStep.selectAnyPiece(this.boardViewColor);

				if (piece != null)
				{
					if (this.mSelectedTile != null && this.mSelectedTile.selectPiece(this.boardViewColor).getNextStep(mDice.getValue()).equals(newStep))
					{
						this.mSelectedTile.deselect();
						piece.moveToNest();
						piece = this.mSelectedTile.selectPiece(this.boardViewColor);
						piece.move(mDice.getValue());
						piece.deselect();
						this.mSelectedTile = null;
						mState.setRoundState(RoundState.NONE_SELECTED);
						this.switchPlayer();
						nextStep = piece.getNextStep(mDice.getValue());//= newStep.findNextStep(this.mDice.getValue());
					}
					else
					{
						piece.deselect();
					}
				}
			}
		}

		if (nextStep != null)
		{
			if (this.mNextStepGlobal != null) 
				this.mNextStepGlobal.unsetNextStep();
			this.mNextStepGlobal = nextStep;
			this.mNextStepGlobal.setNextStep();
		}
	}

	private void switchPlayer() {
		mState.setPlayer(mPlayers[mState.getPlayer().getIndex() + 1]);
		/*
		switch (boardViewColor) {
		case Color.BLUE:
			//mPlayer = Player.GREEN;
			boardViewColor = Color.GREEN;
			break;
		case Color.GREEN:
			//mPlayer = Player.RED;
			boardViewColor = Color.RED;
			break;
		case Color.RED:
			//mPlayer = Player.YELLOW;
			boardViewColor = Color.YELLOW;
			break;
		case Color.YELLOW:
			//mPlayer = Player.BLUE;
			boardViewColor = Color.BLUE;
			break;
		}*/
		//mState.nextPlayer();
		mDice.setPlayer(mState.getPlayer());
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

