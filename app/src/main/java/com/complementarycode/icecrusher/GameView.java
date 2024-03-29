package com.complementarycode.icecrusher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.complementarycode.icecrusher.MainActivity.prefs;

public class GameView extends SurfaceView implements Runnable {

    // used to determine whether user moved a finger enough to draw again
    private static final float TOUCH_TOLERANCE = 10;

    private final SoundPool soundPool;
    private Thread thread;
    private boolean isPlaying, isGameOver = false;
    private int screenX, screenY, score = 0;
    public static float screenRatioX, screenRatioY;
    private Paint paint;
    private Iceberg[] icebergs;
    private Animal[] animals;
    private int numberOfIcebergs = 6, numberOfAnimals = 4; int missed = 0;
    private Random random;
    int sound;
    private List<Torpedo> torpedoes;
    private Boat boat;
    private Health health;
    private GameActivity activity;
    private Background background1, background2;
    private static final int MAX_FRAME_RATE = (int) (1000.0 / 60.0);
    long frameTime = MAX_FRAME_RATE;

    // Maps of current Paths being drawn and Points in those Paths
    private final Map<Integer, Path> pathMap = new HashMap<>();
    private final Map<Integer, Point> previousPointMap =  new HashMap<>();

    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);

        this.activity = activity;

        this.screenX = screenX;
        this.screenY = screenY;
        screenRatioX = screenX / 1920f;
        screenRatioY = screenY / 1080f;

        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());
        background2.x = screenX;

        boat = new Boat(this, screenY, getResources());

        torpedoes = new ArrayList<>();

        //background2.x = screenX;

        paint = new Paint();
        paint.setTextSize(64);
        paint.setColor(Color.WHITE);

        icebergs = new Iceberg[numberOfIcebergs];
        animals = new Animal[numberOfAnimals];

        for (int i = 0; i < numberOfIcebergs; i++) {

            Iceberg iceberg = new Iceberg(getResources());
            icebergs[i] = iceberg;

        }
        for (int i = 0; i < numberOfAnimals; i++) {

            Animal animal = new Animal(getResources());
            animals[i] = animal;

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .build();

        } else
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        sound = soundPool.load(activity, R.raw.shoot, 1);

        random = new Random();
    }

    @Override
    public void run() {
        while (isPlaying) {
            long frameStartTime;
            frameStartTime = System.nanoTime();
            update ();
            draw ();
            frameTime = (System.nanoTime() - frameStartTime) / 1000000;
            if (frameTime < MAX_FRAME_RATE) {
                sleep(MAX_FRAME_RATE - frameTime);
            }
        }
    }

    private void update () {

        background1.x = (int) (background1.x - (7.5 * screenRatioX));

        if(background1.x < background2.x) {
            background2.x = background1.x + background1.background.getWidth();
        }
        else {
            background2.x = background1.x - background1.background.getWidth();
        }

        if (background1.x + background1.background.getWidth() < 0) {
            background1.x = background2.x + background2.background.getWidth();
        }
        if (background2.x + background2.background.getWidth() < 0) {
            background2.x = background1.x + background1.background.getWidth();
        }

        if (boat.isGoingUp && !boat.isGoingDown)
            boat.y -= 15 * screenRatioY;
        else if (!boat.isGoingUp && boat.isGoingDown)
            boat.y += 15 * screenRatioY;

        if (boat.y < 0)
            boat.y = 0;

        if (boat.y >= screenY - boat.height)
            boat.y = screenY - boat.height;

        List<Torpedo> trash = new ArrayList<>();

        for (int i = 0; i < torpedoes.size(); i++) {

            Torpedo torpedo = torpedoes.get(i);

            if (torpedo.x > screenX)
                trash.add(torpedo);

            torpedo.x += 50 * screenRatioX;

            for (Iceberg iceberg : icebergs)
            {
                if (Rect.intersects(iceberg.getCollisionShape(),
                        torpedo.getCollisionShape())) {
                    if (CollisionUtil.isCollisionDetected(iceberg.getGameElement(), iceberg.x, iceberg.y,
                            torpedo.getGameElement(), torpedo.x, torpedo.y)) {

                        score++;
                        iceberg.x = -500;
                        torpedo.x = screenX + 500;
                        iceberg.wasShot = true;
                    }
                }
            }
            for (Animal animal : animals)
            {
                if (Rect.intersects(animal.getCollisionShape(),
                        torpedo.getCollisionShape())) {
                    if (CollisionUtil.isCollisionDetected(animal.getGameElement(), animal.x, animal.y,
                            torpedo.getGameElement(), torpedo.x, torpedo.y)) {

                        isGameOver = true;
                        return;
                    }
                }
            }
        }

        for (int i = 0; i < trash.size(); i++) {
            torpedoes.remove(trash.get(i));
        }

        //for (int i = 0; i < numberOfIcebergs; i++)

        for (Iceberg iceberg : icebergs){

            iceberg.x -= iceberg.speed;

            if (iceberg.x + iceberg.width < 0) {

                if (!iceberg.wasShot) {
                    missed++;
                }

                int bound = (int) (15 * screenRatioX);
                iceberg.speed = random.nextInt(bound);

                if (iceberg.speed < 5 * screenRatioX)
                    iceberg.speed = (int) (5 * screenRatioX);

                iceberg.x = screenX;
                iceberg.y = random.nextInt(screenY - iceberg.height);

                iceberg.wasShot = false;
            }



            if (Rect.intersects(iceberg.getCollisionShape(), boat.getCollisionShape())) {
                if (CollisionUtil.isCollisionDetected(iceberg.getGameElement(), iceberg.x, iceberg.y,
                        boat.getGameElement(), boat.x, boat.y)) {
                    //TODO
//                    boat.reduceHealth();
//                    if (boat.getHealth() < 0){
//                        isGameOver = true;
//                        return;
//                    }
                    isGameOver = true;
                    return;
                }
            }

        }

        for (Animal animal : animals){

            animal.x -= animal.speed;

            if (animal.x + animal.width < 0) {

                int bound = (int) (5 * screenRatioX);
                animal.speed = random.nextInt(bound);

                if (animal.speed < 1 * screenRatioX)
                    animal.speed = (int) (5 / 2f * screenRatioX);

                animal.x = screenX;
                animal.y = random.nextInt(screenY - animal.height);

                //animal.wasShot = false;
            }



            if (Rect.intersects(animal.getCollisionShape(), boat.getCollisionShape())) {
                if (CollisionUtil.isCollisionDetected(animal.getGameElement(), animal.x, animal.y,
                        boat.getGameElement(), boat.x, boat.y)) {
                    isGameOver = true;
                    return;
                }
            }
        }
    }

    private void draw () {

        if (getHolder().getSurface().isValid()) {

            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);

            for (int i = 0; i < numberOfIcebergs; i++) {
                Iceberg iceberg = icebergs[i];
                canvas.drawBitmap(iceberg.getGameElement(), iceberg.x, iceberg.y, paint );
            }
            for (int i = 0; i < numberOfAnimals; i++) {
                Animal animal = animals[i];
                canvas.drawBitmap(animal.getGameElement(), animal.x, animal.y, paint );
            }

            if (isGameOver) {
                isPlaying = false;
                canvas.drawBitmap(boat.getGameElement(), boat.x, boat.y, paint);
                getHolder().unlockCanvasAndPost(canvas);
                saveIfHighScore();
                waitBeforeExiting ();
                return;
            }

            canvas.drawBitmap(boat.getGameElement(), boat.x, boat.y, paint);

            for (int i = 0; i < torpedoes.size(); i++) {
                canvas.drawBitmap(torpedoes.get(i).getGameElement(), torpedoes.get(i).x, torpedoes.get(i).y, paint);
            }

            canvas.drawText(getResources().getString(R.string.crushed) + score, (screenX - 400), (int) (64 * screenRatioY), paint);
            canvas.drawText(getResources().getString(R.string.missed) + missed, (screenX - 400), (screenY - 16), paint);

            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    public void newTorpedo() {

        soundPool.play(sound, 1, 1, 0, 0, 1);

        Torpedo torpedo = new Torpedo(getResources());
        torpedo.x = boat.x + boat.width;
        torpedo.y = boat.y + (boat.height / 2);
        torpedoes.add(torpedo);
    }

    private void waitBeforeExiting() {

        try {
            Thread.sleep(1000);
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void saveIfHighScore() {

        if (prefs.getInt("highscore", 0) < score) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("highscore", score);
            editor.apply();
        }

    }

    private void sleep (long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume () {

        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause () {

        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // handle touch event
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked(); // event type
        int actionIndex = event.getActionIndex(); // pointer (i.e., finger)

        // determine whether touch started, ended or is moving
        if (action == MotionEvent.ACTION_DOWN ||
                action == MotionEvent.ACTION_POINTER_DOWN) {
            touchStarted(event.getX(actionIndex), event.getY(actionIndex),
                    event.getPointerId(actionIndex));
        }
        else if (action == MotionEvent.ACTION_UP ||
                action == MotionEvent.ACTION_POINTER_UP) {
            touchEnded(event.getPointerId(actionIndex));
        }
        else {
            touchMoved(event);
        }
        //invalidate(); // redraw
        return true;
    }

    // called when the user touches the screen
    private void touchStarted(float x, float y, int lineID) {
        Path path; // used to store the path for the given touch id
        Point point; // used to store the last point in path

        // if there is already a path for lineID
        if (pathMap.containsKey(lineID)) {
            path = pathMap.get(lineID); // get the Path
            path.reset(); // resets the Path because a new touch has started
            point = previousPointMap.get(lineID); // get Path's last point
        }
        else {
            path = new Path();
            pathMap.put(lineID, path); // add the Path to Map
            point = new Point(); // create a new Point
            previousPointMap.put(lineID, point); // add the Point to the Map
        }

        // move to the coordinates of the touch
        path.moveTo(x, y);
        if (point != null){
            point.x = (int) x;
            point.y = (int) y;
        }


        if (point != null){
            if (point.x < screenX / 2f && point.y > screenY / 2f) {
                //boat.y += 50 * screenRatioY;
                boat.isGoingUp = false;
                boat.isGoingDown = true;
            } else if (point.x < screenX / 2f && point.y < screenY / 2f) {
                //boat.y -= 50 * screenRatioY;
                boat.isGoingUp = true;
                boat.isGoingDown = false;
            }
            if (point.x > screenX / 2f) {
                newTorpedo();
            }
        }
    }

    // called when the user drags along the screen
    private void touchMoved(MotionEvent event) {
        // for each of the pointers in the given MotionEvent
        for (int i = 0; i < event.getPointerCount(); i++) {
            // get the pointer ID and pointer index
            int pointerID = event.getPointerId(i);
            int pointerIndex = event.findPointerIndex(pointerID);

            // if there is a path associated with the pointer
            if (pathMap.containsKey(pointerID)) {
                // get the new coordinates for the pointer
                float newX = event.getX(pointerIndex);
                float newY = event.getY(pointerIndex);

                // get the path and previous point associated with
                // this pointer
                Path path = pathMap.get(pointerID);
                Point point = previousPointMap.get(pointerID);

                // calculate how far the user moved from the last update
                float deltaX = Math.abs(newX - point.x);
                float deltaY = Math.abs(newY - point.y);

                // if the distance is significant enough to matter
                if (deltaX >= TOUCH_TOLERANCE || deltaY >= TOUCH_TOLERANCE) {
                    // move the path to the new location
                    path.quadTo(point.x, point.y, (newX + point.x) / 2,
                            (newY + point.y) / 2);

                    // store the new coordinates
                    point.x = (int) newX;
                    point.y = (int) newY;
                }
            }
        }
    }

    // called when the user finishes a touch
    private void touchEnded(int lineID) {
        Path path = pathMap.get(lineID); // get the corresponding Path
        //bitmapCanvas.drawPath(path, paintLine); // draw to bitmapCanvas
        boat.isGoingUp = false;
        boat.isGoingDown = false;
        path.reset(); // reset the Path
    }
}