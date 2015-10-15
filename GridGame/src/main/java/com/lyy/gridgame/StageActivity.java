package com.lyy.gridgame;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class StageActivity extends BaseActivity {

	private GridView mGridView;
	private CommonAdapter<String> mAdapter;
	private List<String> mList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stages);

		mGridView = (GridView) findViewById(R.id.gridView);

		mList = new ArrayList<String>();
		for (int i = 0; i < 20; i++) {
			mList.add(null);
		}

		mAdapter = new CommonAdapter<String>(this, mList, R.layout.gitem_stage) {

			@Override
			public void convert(ViewHolder helper, String item) {
				// TODO Auto-generated method stub

			}
		};

		mGridView.setAdapter(mAdapter);

		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				startActivity(new Intent(StageActivity.this, GameActivity.class));
			}
		});

	}

}
