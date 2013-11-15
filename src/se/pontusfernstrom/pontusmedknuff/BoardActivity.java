package se.pontusfernstrom.pontusmedknuff;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class BoardActivity extends StartScreen {
	private BoardView mBoardView;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.board_activity);
		
		mBoardView = (BoardView)findViewById(R.id.boardview_content);
		
		mBoardView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event)
			{
				return mBoardView.onTouch(view, event);
			}
		});
		
	}
}
