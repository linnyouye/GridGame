package com.lyy.gridgame;

import com.lyy.gridgame.R;

import android.os.Bundle;

public class GameActivity extends BaseActivity {

    private GameView mGameView;

    private int[][] state = new int[][]{
            {1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, -1, 1, 1}, {1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, -1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1},
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mGameView = (GameView) findViewById(R.id.gameView);
        mGameView.initStatus(state, 2, 2);
    }

}
