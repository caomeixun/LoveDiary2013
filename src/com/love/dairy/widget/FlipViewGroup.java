package com.love.dairy.widget;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.love.dairy.main.MainActivity;

/*
 Copyright 2012 Aphid Mobile

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 */

public class FlipViewGroup extends ViewGroup {

	private static final int MSG_SURFACE_CREATED = 1;
	public MainActivity context = null;
	private LinkedList<MyView> flipViews = new LinkedList<MyView>();
	private Handler handler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			if (msg.what == MSG_SURFACE_CREATED) {
				width = 0;
				height = 0;
				requestLayout();
				return true;
			}
			return false;
		}
	});

	private GLSurfaceView surfaceView;
	private FlipRenderer renderer;

	private int width;
	private int height;

	private boolean flipping = false;

	public FlipViewGroup(MainActivity context) {
		super(context);
		this.context=context;
		setupSurfaceView();
	}
	public FlipViewGroup(Context context) {
		super(context);
	}

	private void setupSurfaceView() {
		surfaceView = new GLSurfaceView(getContext());

		renderer = new FlipRenderer(this);

		surfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		surfaceView.setZOrderOnTop(true);
		surfaceView.setRenderer(renderer);
		surfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		surfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

		addView(surfaceView);
	}

	public GLSurfaceView getSurfaceView() {
		return surfaceView;
	}

	public FlipRenderer getRenderer() {
		return renderer;
	}

	public void addFlipView(MyView v) {
		flipViews.add(v);
		addView(v);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		for (View child : flipViews){
			child.layout(0, 0, r - l, b - t);
		}
		if (changed || width == 0 ) {
			int w = r - l;
			int h = b - t;
			surfaceView.layout(0, 0, w, h);

			if (width != w || height != h) {
				width = w;
				height = h;

				if (flipViews.size() >= 2 && flipping ) {
					renderer.updateTexture(flipViews);
				}
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		for (MyView child : flipViews){
			child.measure(widthMeasureSpec, heightMeasureSpec);
		}
	}

	public void startFlipping() {
		flipping = true;
		renderer.updateTexture(flipViews);
	}

	public void onResume() {
		surfaceView.onResume();
	}

	public void onPause() {
//		if(renderer != null){
//			Log.e("TAG", "onPause"+renderer.getCards().photoPostion%2);
//			renderer.getCards().flipViews.get(renderer.getCards().photoPostion%2).bringToFront();
//			Log.e("TAG", "onPause-end");
//		}
		surfaceView.onPause();
	}

	public void reloadTexture() {
		handler.sendMessage(Message.obtain(handler, MSG_SURFACE_CREATED));
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return renderer.getCards().handleTouchEvent(event);
	}
}
