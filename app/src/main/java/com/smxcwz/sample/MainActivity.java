package com.smxcwz.sample;

import android.os.Bundle;
import android.view.View;

import com.smxcwz.frame.eventbus.EventCenter;
import com.smxcwz.frame.netstatus.NetUtils;
import com.smxcwz.sample.base.BaseSwipeBackActivity;

public class MainActivity extends BaseSwipeBackActivity {

	@Override
	protected void getBundleExtras(Bundle extras) {

	}

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.activity_main;
	}

	@Override
	protected void onEventComming(EventCenter eventCenter) {

	}

	@Override
	protected View getLoadingTargetView() {
		return null;
	}

	@Override
	protected void initViewsAndEvents() {
//		setTitleIcon(R.mipmap.ic_launcher);
//		setTitleText("title");
		setLeftIcon(R.mipmap.ic_launcher);
		setLeftText("left");
//		setRightIcon(R.mipmap.ic_launcher);
//		setRightText("right");

	}

	public void go(View v) {
		super.onClick(v);
		goToActivity(MainActivity.class);
	}

	@Override
	protected void onNetworkConnected(NetUtils.NetType type) {

	}

	@Override
	protected void onNetworkDisConnected() {

	}

	@Override
	protected boolean isApplyStatusBarTranslucency() {
		return false;
	}

	@Override
	protected boolean isBindEventBusHere() {
		return false;
	}

	@Override
	protected boolean toggleOverridePendingTransition() {
		return false;
	}

	@Override
	protected TransitionMode getOverridePendingTransitionMode() {
		return null;
	}
}
