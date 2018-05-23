package com.smxcwz.frame.base;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.smxcwz.frame.R;
import com.smxcwz.frame.utils.CommonUtils;

/**
 * Author:  smxcwz
 * Email:   smxcwz@163.com
 * Date:    2018/5/22
 * Description:
 */
abstract class BaseAppCompatActivity extends AppCompatActivity {

	/**
	 * Log tag
	 */
	protected static String TAG_LOG = null;

	/**
	 * Screen information
	 */
	protected int mScreenWidth = 0;
	protected int mScreenHeight = 0;
	protected float mScreenDensity = 0.0f;

	/**
	 * context
	 */
	protected Context mContext = null;
	/**
	 *
	 */

	protected ViewDataBinding mViewDataBinding = null;

	/**
	 * overridePendingTransition mode
	 */
	public enum TransitionMode {
		LEFT, RIGHT, TOP, BOTTOM, SCALE, FADE
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (toggleOverridePendingTransition()) {
			switch (getOverridePendingTransitionMode()) {
				case LEFT:
					overridePendingTransition(R.anim.left_in, R.anim.left_out);
					break;
				case RIGHT:
					overridePendingTransition(R.anim.right_in, R.anim.right_out);
					break;
				case TOP:
					overridePendingTransition(R.anim.top_in, R.anim.top_out);
					break;
				case BOTTOM:
					overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
					break;
				case SCALE:
					overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
					break;
				case FADE:
					overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
					break;
				default:
					break;
			}
		}
		super.onCreate(savedInstanceState);

		mContext = this;
		TAG_LOG = this.getClass().getSimpleName();

		if (getContentViewLayoutID() != 0) {
			mViewDataBinding = DataBindingUtil.setContentView(
					this, getContentViewLayoutID());
		} else {
			throw new IllegalArgumentException("You must return a right contentView layout resource Id");
		}
		//Screen param
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

		mScreenDensity = displayMetrics.density;
		mScreenHeight = displayMetrics.heightPixels;
		mScreenWidth = displayMetrics.widthPixels;

		Bundle extras = getIntent().getExtras();
		if (null != extras) {
			getBundleExtras(extras);
		}

	}

	/**
	 * startActivity
	 *
	 * @param clazz
	 */
	protected void goToActivity(Class<?> clazz) {
		Intent intent = new Intent(this, clazz);
		startActivity(intent);
	}

	/**
	 * startActivity with bundle
	 *
	 * @param clazz
	 * @param bundle
	 */
	protected void goToActivity(Class<?> clazz, Bundle bundle) {
		Intent intent = new Intent(this, clazz);
		if (null != bundle) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}

	/**
	 * startActivity then finish
	 *
	 * @param clazz
	 */
	protected void goToActivityThenFinsh(Class<?> clazz) {
		Intent intent = new Intent(this, clazz);
		startActivity(intent);
		finish();
	}

	/**
	 * startActivity with bundle then finish
	 *
	 * @param clazz
	 * @param bundle
	 */
	protected void goToActivityThenFinsh(Class<?> clazz, Bundle bundle) {
		Intent intent = new Intent(this, clazz);
		if (null != bundle) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
		finish();
	}

	/**
	 * startActivityForResult
	 *
	 * @param clazz
	 * @param requestCode
	 */
	protected void goToActivityForResult(Class<?> clazz, int requestCode) {
		Intent intent = new Intent(this, clazz);
		startActivityForResult(intent, requestCode);
	}

	/**
	 * startActivityForResult with bundle
	 *
	 * @param clazz
	 * @param requestCode
	 * @param bundle
	 */
	protected void goToActivityForResult(Class<?> clazz, int requestCode, Bundle bundle) {
		Intent intent = new Intent(this, clazz);
		if (null != bundle) {
			intent.putExtras(bundle);
		}
		startActivityForResult(intent, requestCode);
	}

	/**
	 * show toast
	 *
	 * @param msg
	 */
	protected void showToast(String msg) {
		//防止遮盖虚拟按键
		if (null != msg && !CommonUtils.isEmpty(msg)) {
//			Snackbar.make(getLoadingTargetView(), msg, Snackbar.LENGTH_SHORT).show();
		}
	}

	@Override
	public void finish() {
		super.finish();
		BaseAppManager.getInstance().removeActivity(this);
		if (toggleOverridePendingTransition()) {
			switch (getOverridePendingTransitionMode()) {
				case LEFT:
					overridePendingTransition(R.anim.left_in, R.anim.left_out);
					break;
				case RIGHT:
					overridePendingTransition(R.anim.right_in, R.anim.right_out);
					break;
				case TOP:
					overridePendingTransition(R.anim.top_in, R.anim.top_out);
					break;
				case BOTTOM:
					overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
					break;
				case SCALE:
					overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
					break;
				case FADE:
					overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
					break;
				default:
					break;
			}
		}
	}

	/**
	 * toggle overridePendingTransition
	 *
	 * @return
	 */
	protected abstract boolean toggleOverridePendingTransition();

	/**
	 * get the overridePendingTransition mode
	 */
	protected abstract TransitionMode getOverridePendingTransitionMode();

	/**
	 * get bundle data
	 *
	 * @param extras
	 */
	protected abstract void getBundleExtras(Bundle extras);

	/**
	 * bind layout resource file
	 *
	 * @return id of layout resource
	 */
	protected abstract int getContentViewLayoutID();

}
