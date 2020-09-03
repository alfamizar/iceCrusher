package com.complementarycode.icecrusher;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static com.complementarycode.icecrusher.GameView.screenRatioX;
import static com.complementarycode.icecrusher.GameView.screenRatioY;

public class Torpedo extends GameElement{

    private Bitmap torpedo;

    Torpedo(Resources res) {

        torpedo = BitmapFactory.decodeResource(res, R.drawable.torpedo);

        width = torpedo.getWidth();
        height = torpedo.getHeight();

        width /= 4;
        height /= 4;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        torpedo = Bitmap.createScaledBitmap(torpedo, width, height, false);
    }

    Bitmap getGameElement() {
        return torpedo;
    }
}
