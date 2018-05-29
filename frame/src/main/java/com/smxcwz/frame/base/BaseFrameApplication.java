package com.smxcwz.frame.base;


import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.smxcwz.frame.BuildConfig;

import me.yokeyword.fragmentation.Fragmentation;
import me.yokeyword.fragmentation.helper.ExceptionHandler;

/**
 * Author:  smxcwz
 * Email:   smxcwz@163.com
 * Date:    2018/5/23
 * Description:
 */
public class BaseFrameApplication extends MultiDexApplication {
	protected Application mApplication;

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		mApplication = BaseFrameApplication.this;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		initFragmentation();
	}

	private void initFragmentation() {
		Fragmentation.builder()
				// 设置 栈视图 模式为 悬浮球模式   SHAKE: 摇一摇唤出   NONE：隐藏
				.stackViewMode(Fragmentation.BUBBLE)
				.debug(BuildConfig.DEBUG)
				// 在遇到After onSaveInstanceState时，不会抛出异常，会回调到下面的ExceptionHandler
				.handleException(new ExceptionHandler() {
					@Override
					public void onException(Exception e) {
						// 建议在该回调处上传至我们的Crash监测服务器
						// 以Bugtags为例子: 手动把捕获到的 Exception 传到 Bugtags 后台。
						// Bugtags.sendException(e);
					}
				})
				.install();
	}
}
