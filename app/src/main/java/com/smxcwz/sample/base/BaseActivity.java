/*
 * Copyright (c) 2015 [1076559197@qq.com | tchen0707@gmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smxcwz.sample.base;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.smxcwz.frame.base.BaseFrameActivity;

/**
 * Author:  Tau.Chen
 * Email:   1076559197@qq.com | tauchen1990@gmail.com
 * Date:    15/7/21
 * Description:
 */
public abstract class BaseActivity extends BaseFrameActivity  {

    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
//        mToolbar = ButterKnife.findById(this, R.id.common_toolbar);
//        if (null != mToolbar) {
//            setSupportActionBar(mToolbar);
//            getSupportActionBar().setHomeButtonEnabled(true);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
    }

//    protected MyApplication getBaseApplication() {
//        return (MyApplication) getApplication();
//    }



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



    protected void setLeftIconVisibility(boolean isShow) {

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

}
