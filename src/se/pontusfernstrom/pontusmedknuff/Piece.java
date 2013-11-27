package se.pontusfernstrom.pontusmedknuff;

public class Piece {
	private Player mPlayer;
	private boolean mSelected;
	private BoardTile mBoardTile;
	private int mPosition;
	private BoardView mBoardView;
	
	public Piece(BoardView boardView, BoardTile boardTile, Player player, boolean selected, int position)
	{
		this.mBoardView = boardView;
		this.mPlayer = player;
		this.mBoardTile = boardTile;
		this.mSelected = selected;
		this.mPosition = position;
	}
	
	public Piece(Piece piece) {
		this(piece.mBoardView, piece.mBoardTile, piece.mPlayer, piece.mSelected, piece.mPosition);
	}
	
	public Piece(BoardView boardView, BoardTile boardTile, Player player) {
		this(boardView, boardTile, player, false, 0);
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
		if (step.equals(mBoardView.mSteps[56])) {
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
			return mBoardView.mSteps[newIndex];
		}
		else if (newIndex < 45)
		{
			newIndex = newIndex + mPlayer.getEntranceOffset() - 1;
			return mBoardView.mSteps[newIndex];
		}
		else if (newIndex == 45)
		{
			return mBoardView.mSteps[56];
		}
		else if (mPosition < 41)
		{
			return mBoardView.mSteps[(mPosition + mPlayer.getPerimeterOffset()) % 40];
		}
		else
		{
			return mBoardView.mSteps[mPosition + mPlayer.getEntranceOffset() - 1];
		}
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
}