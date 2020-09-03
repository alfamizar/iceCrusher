package com.complementarycode.icecrusher;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static com.complementarycode.icecrusher.GameView.screenRatioX;
import static com.complementarycode.icecrusher.GameView.screenRatioY;

public class Iceberg extends GameElement {

    static int index = 0;
    Resources res;
    private int [] btmIcebergs = {R.drawable.iceberg1, R.drawable.iceberg2, R.drawable.iceberg3,
            R.drawable.iceberg4, R.drawable.iceberg5, R.drawable.iceberg6};
    public int speed = 5;
    public boolean wasShot = true;
    Bitmap iceberg1, iceberg2, iceberg3, iceberg4, iceberg5, iceberg6, iceberg;

    Iceberg(Resources res) {

        this.res = res;

        x = 0;

        processOneIceberg();

        y = -height;
    }

    public void processOneIceberg() {
        iceberg = BitmapFactory.decodeResource(res, btmIcebergs[index]);

        width = iceberg.getWidth();
        height = iceberg.getHeight();

        width /= 10;
        height /= 10;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        iceberg = Bitmap.createScaledBitmap(iceberg, width, height, false);
        index++;
        if (index > 5) {
            index = 0;
        }
    }

//    Bitmap getIceberg() {
//        return iceberg;
//    }

    Bitmap getGameElement() {
        return iceberg;
    }
}
