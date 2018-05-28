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

import android.os.Bundle;

import com.smxcwz.frame.base.BaseFrameActivity;


/**
 * Author:  Tau.Chen
 * Email:   1076559197@qq.com | tauchen1990@gmail.com
 * Date:    15/7/21
 * Description:
 */
public abstract class BaseActivity extends BaseFrameActivity {


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

}
