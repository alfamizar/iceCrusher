package com.complementarycode.icecrusher;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static com.complementarycode.icecrusher.GameView.screenRatioX;
import static com.complementarycode.icecrusher.GameView.screenRatioY;

public class Boat extends GameElement {

    boolean isGoingUp = false;
    Bitmap boat;
    private GameView gameView;

    Boat(GameView gameView, int screenY, Resources res) {

        this.gameView = gameView;

        boat = BitmapFactory.decodeResource(res, R.drawable.boat);

        width = boat.getWidth();
        height = boat.getHeight();

        width /= 6;
        height /= 6;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        boat = Bitmap.createScaledBitmap(boat, width, height, false);

        y = screenY / 2;
        x = (int) (64 * screenRatioX);
    }

    Bitmap getGameElement() {
        return boat;
    }
}
