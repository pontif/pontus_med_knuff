package se.pontusfernstrom.pontusmedknuff;

import android.graphics.Color;

public class Player {
	int mIndex;
	int mPerimeterOffset;
	int mEntranceOffset;
	Nest mNest;
	int mColor;
	
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
		switch (index)
		{
		case 0:
			this.mColor = Color.BLUE;
			break;
		case 1:
			this.mColor = Color.GREEN;
			break;
		case 2:
			this.mColor = Color.RED;
			return;
		case 3:
			this.mColor = Color.YELLOW;
			return;
		}
	}
	
	public int getIndex() {
		return mIndex;
	}
	
	public int getPerimeterOffset() {
		return mPerimeterOffset;
	}
	
	public int getEntranceOffset() {
		return mEntranceOffset;
	}
	
	public Nest getNest() {
		return this.mNest;
	}
	
	public int getColor() {
		return this.mColor;
	}
}
