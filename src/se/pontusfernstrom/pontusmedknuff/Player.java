package se.pontusfernstrom.pontusmedknuff;

public class Player {
	int mIndex;
	int mPerimeterOffset;
	int mEntranceOffset;
	Nest mNest;
	
	/*public enum Name
	{
		PLAYER_1,
		PLAYER_2,
		PLAYER_3,
		PLAYER_4
	}
	
	private Name name;*/

	public Player(int index, Nest nest) {
		this.mIndex = index;
		this.mPerimeterOffset = index * 10;
		this.mEntranceOffset = index * 4;
		this.mNest = nest;
	}
	
	public int getIndex() {
		return mIndex;
	}
	
	public int getPerimeterOffset() {
		return mPerimeterOffset;
	}
	
	public int getEntranceOffset() {
		return mPerimeterOffset;
	}
	
	public Nest getNest() {
		return this.mNest;
	}
}
