package se.pontusfernstrom.pontusmedknuff;

class GameState {
	
	public enum RoundState {
		NONE_SELECTED,
		ONE_SELECTED
	}
	
	private Player mPlayer;
	private RoundState mRoundState;
	
	public GameState(Player player) {
		initGameState(player, RoundState.NONE_SELECTED);
	}

	private void initGameState(Player player, RoundState roundState) {
		this.mPlayer = player;
		this.mRoundState = roundState;
	}
	
	public Player getPlayer() {
		return mPlayer;
	}
	
	public void setRoundState(RoundState roundState) {
		mRoundState = roundState;
	}
	
	public RoundState getRoundState() {
		return mRoundState;
	}
	
	public void setPlayer(Player player) {
		mPlayer = player;
	}
}