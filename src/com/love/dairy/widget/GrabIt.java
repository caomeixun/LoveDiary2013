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
package com.love.dairy.widget;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

public class GrabIt {
	private GrabIt() {
	}

	public static Bitmap takeScreenshot(View view) {
		Bitmap bitmap = null;
		try{
		assert view.getWidth() > 0 && view.getHeight() > 0;
		Bitmap.Config config = Bitmap.Config.ARGB_8888;
		bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
				config);
		Canvas canvas = new Canvas(bitmap);
		view.draw(canvas);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return bitmap;
	}
}
