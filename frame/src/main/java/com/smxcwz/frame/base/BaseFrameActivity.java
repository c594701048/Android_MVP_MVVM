package com.smxcwz.frame.base;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.smxcwz.frame.eventbus.EventCenter;
import com.smxcwz.frame.loading.VaryViewHelperController;
import com.smxcwz.frame.netstatus.NetChangeObserver;
import com.smxcwz.frame.netstatus.NetStateReceiver;
import com.smxcwz.frame.netstatus.NetUtils;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Author:  smxcwz
 * Email:   smxcwz@163.com
 * Date:    2018/5/22
 * Description:
 */
public abstract class BaseFrameActivity extends BaseAppCompatActivity {

	/**
	 * network status
	 */
	protected NetChangeObserver mNetChangeObserver = null;

	/**
	 * loading view controller
	 */
	private VaryViewHelperController mVaryViewHelperController = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// base setup
		if (isBindEventBusHere()) {
			EventBus.getDefault().register(this);
		}
		BaseAppManager.getInstance().addActivity(this);
		mNetChangeObserver = new NetChangeObserver() {
			@Override
			public void onNetConnected(NetUtils.NetType type) {
				super.onNetConnected(type);
				onNetworkConnected(type);
			}

			@Override
			public void onNetDisConnect() {
				super.onNetDisConnect();
				onNetworkDisConnected();
			}
		};
		//注册了一个广播 防止网络变化对 对进度条的影响
		NetStateReceiver.registerObserver(mNetChangeObserver);
		initViewsAndEvents();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		ButterKnife.bind(this);
		if (null != getLoadingTargetView()) {
			mVaryViewHelperController = new VaryViewHelperController(getLoadingTargetView());
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		NetStateReceiver.removeRegisterObserver(mNetChangeObserver);
		if (isBindEventBusHere()) {
			EventBus.getDefault().unregister(this);
		}
	}

	/**
	 * when event comming
	 *
	 * @param eventCenter
	 */
	protected abstract void onEventComming(EventCenter eventCenter);

	/**
	 * get loading target view
	 */
	protected abstract View getLoadingTargetView();

	/**
	 * init all views and add events
	 */
	protected abstract void initViewsAndEvents();

	/**
	 * network connected
	 */
	protected abstract void onNetworkConnected(NetUtils.NetType type);

	/**
	 * network disconnected
	 */
	protected abstract void onNetworkDisConnected();

	/**
	 * is applyStatusBarTranslucency
	 *
	 * @return
	 */
	protected abstract boolean isApplyStatusBarTranslucency();

	/**
	 * is bind eventBus
	 *
	 * @return
	 */
	protected abstract boolean isBindEventBusHere();

	/**
	 * toggle show loading
	 *
	 * @param toggle
	 */
	protected void toggleShowLoading(boolean toggle, String msg) {
		if (null == mVaryViewHelperController) {
			throw new IllegalArgumentException("You must return a right target view for loading");
		}

		if (toggle) {
			mVaryViewHelperController.showLoading(msg);
		} else {
			mVaryViewHelperController.restore();
		}
	}

	/**
	 * toggle show empty
	 *
	 * @param toggle
	 */
	protected void toggleShowEmpty(boolean toggle, String msg, View.OnClickListener onClickListener) {
		if (null == mVaryViewHelperController) {
			throw new IllegalArgumentException("You must return a right target view for loading");
		}

		if (toggle) {
			mVaryViewHelperController.showEmpty(msg, onClickListener);
		} else {
			mVaryViewHelperController.restore();
		}
	}

	/**
	 * toggle show error
	 *
	 * @param toggle
	 */
	protected void toggleShowError(boolean toggle, String msg, View.OnClickListener onClickListener) {
		if (null == mVaryViewHelperController) {
			throw new IllegalArgumentException("You must return a right target view for loading");
		}

		if (toggle) {
			mVaryViewHelperController.showError(msg, onClickListener);
		} else {
			mVaryViewHelperController.restore();
		}
	}

	/**
	 * toggle show network error
	 *
	 * @param toggle
	 */
	protected void toggleNetworkError(boolean toggle, View.OnClickListener onClickListener) {
		if (null == mVaryViewHelperController) {
			throw new IllegalArgumentException("You must return a right target view for loading");
		}

		if (toggle) {
			mVaryViewHelperController.showNetworkError(onClickListener);
		} else {
			mVaryViewHelperController.restore();
		}
	}

	public void onEventMainThread(EventCenter eventCenter) {
		if (null != eventCenter) {
			onEventComming(eventCenter);
		}
	}

	/**
	 * use SytemBarTintManager
	 *
	 * @param tintDrawable
	 */
	protected void setSystemBarTintDrawable(Drawable tintDrawable) {
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//			SystemBarTintManager mTintManager = new SystemBarTintManager(this);
//			if (tintDrawable != null) {
//				mTintManager.setStatusBarTintEnabled(true);
//				mTintManager.setTintDrawable(tintDrawable);
//			} else {
//				mTintManager.setStatusBarTintEnabled(false);
//				mTintManager.setTintDrawable(null);
//			}
//		}

	}

	/**
	 * set status bar translucency
	 *
	 * @param on
	 */
	protected void setTranslucentStatus(boolean on) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window win = getWindow();
			WindowManager.LayoutParams winParams = win.getAttributes();
			final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
			if (on) {
				winParams.flags |= bits;
			} else {
				winParams.flags &= ~bits;
			}
			win.setAttributes(winParams);
		}
	}
}