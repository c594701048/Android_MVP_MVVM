package com.smxcwz.frame.base;

/**
 * Author:  smxcwz
 * Email:   smxcwz@163.com
 * Date:    2018/5/22
 * Description:
 */
public interface IFrameBaseView {

	/**
	 * show loading message
	 *
	 * @param msg
	 */
	void showLoading(String msg);

	/**
	 * hide loading
	 */
	void hideLoading();

	/**
	 * show error message
	 */
	void showError(String msg);

	/**
	 * show exception message
	 */
	void showException(String msg);

	/**
	 * show net error
	 */
	void showNetError();

}
