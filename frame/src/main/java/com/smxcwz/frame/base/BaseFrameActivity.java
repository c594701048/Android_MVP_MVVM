package com.smxcwz.frame.base;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.smxcwz.frame.R;
import com.smxcwz.frame.eventbus.EventCenter;
import com.smxcwz.frame.loading.VaryViewHelperController;
import com.smxcwz.frame.netstatus.NetChangeObserver;
import com.smxcwz.frame.netstatus.NetStateReceiver;
import com.smxcwz.frame.netstatus.NetUtils;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import me.yokeyword.fragmentation.ExtraTransaction;
import me.yokeyword.fragmentation.ISupportActivity;
import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportActivityDelegate;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation.SupportHelper;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Author:  smxcwz
 * Email:   smxcwz@163.com
 * Date:    2018/5/22
 * Description:
 */
public abstract class BaseFrameActivity extends BaseAppCompatActivity implements View.OnClickListener, ISupportActivity {
	final SupportActivityDelegate mDelegate = new SupportActivityDelegate(this);
	/**
	 * network status
	 */
	private NetChangeObserver mNetChangeObserver = null;

	/**
	 * loading view controller
	 */
	private VaryViewHelperController mVaryViewHelperController = null;

	private ImmersionBar mImmersionBar;
	private boolean isFullScreen;

	private TextView mTvBaseLeft;
	private TextView mTvBaseRight;
	private TextView mTvBaseTitle;

	@Override
	public SupportActivityDelegate getSupportDelegate() {
		return mDelegate;
	}

	/**
	 * Perform some extra transactions.
	 * 额外的事务：自定义Tag，添加SharedElement动画，操作非回退栈Fragment
	 */
	@Override
	public ExtraTransaction extraTransaction() {
		return mDelegate.extraTransaction();
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDelegate.onCreate(savedInstanceState);
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

		initImmersionBar();
		//	init Toolbar
		initActionbar();
		initViewsAndEvents();
	}

	@Override
	protected void onPostCreate(@Nullable Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDelegate.onPostCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		mDelegate.onDestroy();
		NetStateReceiver.removeRegisterObserver(mNetChangeObserver);
		if (isBindEventBusHere()) {
			EventBus.getDefault().unregister(this);
		}
		super.onDestroy();
	}

	/**
	 * Note： return mDelegate.dispatchTouchEvent(ev) || super.dispatchTouchEvent(ev);
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return mDelegate.dispatchTouchEvent(ev) || super.dispatchTouchEvent(ev);
	}

	/**
	 * 不建议复写该方法,请使用 {@link #onBackPressedSupport} 代替
	 */
	@Override
	final public void onBackPressed() {
		mDelegate.onBackPressed();
	}

	/**
	 * 该方法回调时机为,Activity回退栈内Fragment的数量 小于等于1 时,默认finish Activity
	 * 请尽量复写该方法,避免复写onBackPress(),以保证SupportFragment内的onBackPressedSupport()回退事件正常执行
	 */
	@Override
	public void onBackPressedSupport() {
		mDelegate.onBackPressedSupport();
	}

	/**
	 * 获取设置的全局动画 copy
	 *
	 * @return FragmentAnimator
	 */
	@Override
	public FragmentAnimator getFragmentAnimator() {
		return mDelegate.getFragmentAnimator();
	}

	/**
	 * Set all fragments animation.
	 * 设置Fragment内的全局动画
	 */
	@Override
	public void setFragmentAnimator(FragmentAnimator fragmentAnimator) {
		mDelegate.setFragmentAnimator(fragmentAnimator);
	}

	/**
	 * Set all fragments animation.
	 * 构建Fragment转场动画
	 * <p/>
	 * 如果是在Activity内实现,则构建的是Activity内所有Fragment的转场动画,
	 * 如果是在Fragment内实现,则构建的是该Fragment的转场动画,此时优先级 > Activity的onCreateFragmentAnimator()
	 *
	 * @return FragmentAnimator对象
	 */
	@Override
	public FragmentAnimator onCreateFragmentAnimator() {
		return mDelegate.onCreateFragmentAnimator();
	}

	/**
	 * Causes the Runnable r to be added to the action queue.
	 * <p>
	 * The runnable will be run after all the previous action has been run.
	 * <p>
	 * 前面的事务全部执行后 执行该Action
	 */
	@Override
	public void post(Runnable runnable) {
		mDelegate.post(runnable);
	}

	/****************************************以下为可选方法(Optional methods)******************************************************/

	// 选择性拓展其他方法
	public void loadRootFragment(int containerId, @NonNull ISupportFragment toFragment) {
		mDelegate.loadRootFragment(containerId, toFragment);
	}

	public void start(ISupportFragment toFragment) {
		mDelegate.start(toFragment);
	}

	/**
	 * @param launchMode Same as Activity's LaunchMode.
	 */
	public void start(ISupportFragment toFragment, @ISupportFragment.LaunchMode int launchMode) {
		mDelegate.start(toFragment, launchMode);
	}

	/**
	 * It is recommended to use {@link SupportFragment#startWithPopTo(ISupportFragment, Class, boolean)}.
	 *
	 * @see #popTo(Class, boolean)
	 * +
	 * @see #start(ISupportFragment)
	 */
	public void startWithPopTo(ISupportFragment toFragment, Class<?> targetFragmentClass, boolean includeTargetFragment) {
		mDelegate.startWithPopTo(toFragment, targetFragmentClass, includeTargetFragment);
	}

	/**
	 * Pop the fragment.
	 */
	public void pop() {
		mDelegate.pop();
	}

	/**
	 * Pop the last fragment transition from the manager's fragment
	 * back stack.
	 */
	public void popTo(Class<?> targetFragmentClass, boolean includeTargetFragment) {
		mDelegate.popTo(targetFragmentClass, includeTargetFragment);
	}

	/**
	 * If you want to begin another FragmentTransaction immediately after popTo(), use this method.
	 * 如果你想在出栈后, 立刻进行FragmentTransaction操作，请使用该方法
	 */
	public void popTo(Class<?> targetFragmentClass, boolean includeTargetFragment, Runnable afterPopTransactionRunnable) {
		mDelegate.popTo(targetFragmentClass, includeTargetFragment, afterPopTransactionRunnable);
	}

	public void popTo(Class<?> targetFragmentClass, boolean includeTargetFragment, Runnable afterPopTransactionRunnable, int popAnim) {
		mDelegate.popTo(targetFragmentClass, includeTargetFragment, afterPopTransactionRunnable, popAnim);
	}

	/**
	 * 得到位于栈顶Fragment
	 */
	public ISupportFragment getTopFragment() {
		return SupportHelper.getTopFragment(getSupportFragmentManager());
	}

	/**
	 * 获取栈内的fragment对象
	 */
	public <T extends ISupportFragment> T findFragment(Class<T> fragmentClass) {
		return SupportHelper.findFragment(getSupportFragmentManager(), fragmentClass);
	}


	private void initImmersionBar() {
		mImmersionBar = ImmersionBar.with(this);
		mImmersionBar.statusBarColor(R.color.sr_color_primary_dark)
				.fitsSystemWindows(!isFullScreen)
				.keyboardEnable(true, WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
				.init();
	}

	private void initActionbar() {
		mTvBaseLeft = ButterKnife.findById(this, R.id.tv_base_left);
		mTvBaseRight = ButterKnife.findById(this, R.id.tv_base_right);
		mTvBaseTitle = ButterKnife.findById(this, R.id.tv_base_title);
		mTvBaseLeft.setOnClickListener(this);
		mTvBaseRight.setOnClickListener(this);
		mTvBaseTitle.setOnClickListener(this);
	}

	protected void setTitleVisibility(boolean isShow) {
		mTvBaseTitle.setVisibility(isShow ? View.VISIBLE : View.GONE);
	}

	protected void setTitleText(String tieltStr) {
		mTvBaseTitle.setText(tieltStr);
	}

	protected void setTitleText(@StringRes int resId) {
		mTvBaseTitle.setText(resId);
	}

	protected void setTitleIcon(@Nullable Drawable left, @Nullable Drawable right) {
		if (left != null) {
			left.setBounds(0, 0, left.getMinimumWidth(), left.getMinimumHeight());
		}
		if (right != null) {
			right.setBounds(0, 0, right.getIntrinsicWidth(), right.getMinimumHeight());
		}
		mTvBaseTitle.setCompoundDrawables(left, null, right, null);
	}


	protected void setRightTextVisibility(boolean isShow) {
		mTvBaseRight.setVisibility(isShow ? View.VISIBLE : View.GONE);
	}

	protected void setRightText(@Nullable String text) {
		mTvBaseRight.setText(text);
	}

	protected void setRightText(@StringRes int resId) {
		mTvBaseRight.setText(resId);
	}

	protected void setTitleIcon(@DrawableRes int resIdLeft, @DrawableRes int resIdRight) {
		setTitleIcon(getResources().getDrawable(resIdLeft), getResources().getDrawable(resIdRight));
	}

	protected void setTitleIcon(@Nullable Drawable right) {
		setTitleIcon(null, right);
	}

	protected void setTitleIcon(@DrawableRes int resId) {
		setTitleIcon(getResources().getDrawable(resId));
	}

	protected void setRightIcon(@DrawableRes int resIdLeft, @DrawableRes int resIdRight) {
		setRightIcon(getResources().getDrawable(resIdLeft), getResources().getDrawable(resIdRight));
	}

	protected void setRightIcon(@Nullable Drawable left) {
		setRightIcon(left, null);
	}

	protected void setRightIcon(@DrawableRes int resId) {
		setRightIcon(getResources().getDrawable(resId));
	}

	protected void setLeftIcon(@DrawableRes int resIdLeft, @DrawableRes int resIdRight) {
		setLeftIcon(getResources().getDrawable(resIdLeft), getResources().getDrawable(resIdRight));
	}

	protected void setLeftIcon(@Nullable Drawable left) {
		setLeftIcon(left, null);
	}

	protected void setLeftIcon(@DrawableRes int resId) {
		setLeftIcon(getResources().getDrawable(resId));
	}

	protected void setRightIcon(@Nullable Drawable left, @Nullable Drawable right) {
		if (left != null) {
			left.setBounds(0, 0, left.getMinimumWidth(), left.getMinimumHeight());
		}
		if (right != null) {
			right.setBounds(0, 0, right.getIntrinsicWidth(), right.getMinimumHeight());
		}
		mTvBaseRight.setCompoundDrawables(left, null, right, null);
	}

	protected void setLeftTextVisibility(boolean isShow) {
		mTvBaseLeft.setVisibility(isShow ? View.VISIBLE : View.GONE);
	}

	protected void setLeftText(@Nullable String text) {
		mTvBaseLeft.setText(text);
	}

	protected void setLeftText(@StringRes int resId) {
		mTvBaseLeft.setText(resId);
	}

	protected void setLeftIcon(@Nullable Drawable left, @Nullable Drawable right) {
		if (left != null) {
			left.setBounds(0, 0, left.getMinimumWidth(), left.getMinimumHeight());
		}
		if (right != null) {
			right.setBounds(0, 0, right.getIntrinsicWidth(), right.getMinimumHeight());
		}
		mTvBaseLeft.setCompoundDrawables(left, null, right, null);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.tv_base_left) {
//			onBackPressed();
			onBackPressedSupport();
		}
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


}
