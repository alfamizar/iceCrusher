package com.complementarycode.icecrusher;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static com.complementarycode.icecrusher.GameView.screenRatioX;
import static com.complementarycode.icecrusher.GameView.screenRatioY;

public class Health extends GameElement {
    private int healthLevel = 3;
    Bitmap health;

    Health( int screenY, Resources res) {

        health = BitmapFactory.decodeResource(res, R.drawable.healthheart);

        width = health.getWidth();
        height = health.getHeight();

        width /= 15;
        height /= 15;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        health = Bitmap.createScaledBitmap(health, width, height, false);

        y = (int) (32 * screenRatioY);
        x = (int) (screenY / 2 * screenRatioX - health.getWidth());
    }

    Bitmap getGameElement() {
        return health;
    }
}
