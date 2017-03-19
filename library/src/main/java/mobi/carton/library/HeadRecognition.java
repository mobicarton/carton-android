package mobi.carton.library;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.LinkedList;

/**
 * This helper class is about using sensor fusion to get the direction of the phone, and
 * also using this direction to recognize different kind of head gesture
 */
public class HeadRecognition
        implements
        SensorEventListener {


    /**
     * Used for receiving notifications from the HeadRecognition when
     * gesture such as tilt, nod or shake have been detected.
     */
    public interface OnHeadGestureListener {

        void onTilt(int direction);
        void onNod(int direction);
        void onShake();
    }


    /**
     * Used for receiving notifications from the HeadRecognition when
     * direction values (kind of x, y, z) have changed.
     */
    public interface OnHeadTrackingListener {

        void onDirectionChanged(int azimuth, int pitch, int roll);
    }

    /*
    CONSTANTS
     */
    public static final int TILT_RIGHT = CartonSdk.RIGHT;
    public static final int TILT_LEFT = CartonSdk.LEFT;
    public static final int NOD_UP = CartonSdk.UP;
    public static final int NOD_DOWN = CartonSdk.DOWN;


    /**
     * Maximum delay between detecting tilt > {@link #TILT_THRESHOLD}
     * and tilt back to 0° (around)
     */
    private static final int TILT_TIME = 1000;


    /**
     * Tilt in degree needed to start measuring time (until return to straight - 0°)
     */
    private static final int TILT_THRESHOLD = 15;


    /**
     * Maximum delay between detecting nod > {@link #NOD_THRESHOLD}
     * and nod back to 0° (around)
     */
    private static final int NOD_TIME = 1000;


    /**
     * Nod in degree needed to start measuring time (until return to straight - 0°)
     */
    private static final int NOD_THRESHOLD = 15;


    /**
     * Arbitrary maximum angle to detect shaking : 180° (above it is quiet uncommon)
     */
    private static final int SHAKING_MAX_ANGLE = 180;


    /**
     * Number of changing direction needed to recognize a shaking gesture
     */
    private static final int SHAKING_COUNT = 5;


    /**
     * Delay maximum in milliseconds to realize the number of changing direction
     * needed to recognize a shaking gesture
     */
    private static final int SHAKING_DELAY = 1000;


    /**
     * Angle maximum allow to the delta used to calibrate the nod gesture
     */
    private static final int DELTA_NOD_MAX_ANGLE = 45;


    /*
    VARIABLES
     */
    private SensorManager mSensorManager;


    /**
     * Use to save the lastAzimuth value in order to detect
     * the direction (right/left) of the moving head
     */
    private int lastAzimuth;


    /**
     * Use to save the last direction, in order to detect each changing direction
     * True for the right and False for left
     */
    private boolean directionToRight;


    private LinkedList<Long> changingDirectionQueue;


    /**
     * Listener used to dispatch OnHeadGesture event
     */
    private OnHeadGestureListener mOnHeadGestureListener;


    /**
     * Listener used to dispatch OnHeadTracking event
     */
    private OnHeadTrackingListener mOnHeadTrackingListener;

    private float[] orientation = new float[3];
    private float[] rotationMatrix = new float[9];


    private long tiltTime = -1;
    private int tiltSide = -1;


    private long nodTime = -1;
    private int nodSide = -1;


    /**
     * Use to calibrate nod gesture when the mobile phone is not exactly horizontal
     */
    private static int DELTA_NOD;


    /**
     * Use to save the current (last) roll angle needed for auto calibrate deltaNod
     */
    private int mRoll;


    /**
     * Save context and use it for saving deltaNod in preferences
     */
    private Context mContext;



    /**
     * Constructor that is called when instantiate a HeadRecognition object.
     *
     * @param context The context the HeadRecognition is running, through which it
     *                can access the sensor service.
     */
    public HeadRecognition(Context context) {
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        changingDirectionQueue = new LinkedList<>();
        HeadRecognition.DELTA_NOD = CartonPrefs.getDeltaNod(context);
        mContext = context;
    }


    /**
     * Start HeadRecognition by registered a listener to the sensor service
     */
    public void start() {
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
                SensorManager.SENSOR_DELAY_UI);
    }


    /**
     * Stop HeadRecognition by unregistered listener to the sensor service
     */
    public void stop() {
        mSensorManager.unregisterListener(this);
    }


    /**
     * Register a callback to be invoked when a head gesture (tilt, nod) is detected
     * @param l The callback that will run
     */
    public void setOnHeadGestureListener(OnHeadGestureListener l) {
        mOnHeadGestureListener = l;
    }


    /**
     * Register a callback to be invoked when head direction has changed
     * @param l The callback that will run
     */
    public void setOnHeadTrackingListener(OnHeadTrackingListener l) {
        mOnHeadTrackingListener = l;
    }


    /**
     * Set a delta (to maximum 45°) used to virtually make the mobile horizontal
     * and then detect properly the nod gesture
     * @param deltaNod new delta value
     */
    public void setDeltaNod(int deltaNod) {
        if (deltaNod < -DELTA_NOD_MAX_ANGLE)
            deltaNod = -DELTA_NOD_MAX_ANGLE;
        if (deltaNod > DELTA_NOD_MAX_ANGLE) {
            deltaNod = DELTA_NOD_MAX_ANGLE;
        }

        HeadRecognition.DELTA_NOD = deltaNod;
        CartonPrefs.setDeltaNod(mContext, deltaNod);
    }


    /**
     * Calibrate the deltaNod based on the current roll angle (limited to 45° maximum)
     */
    public void autoCalibrateDeltaNod() {
        setDeltaNod(-mRoll);
    }


    /**
     * Store each new changing direction and trigger shaking detection if there are more than
     * {@link #SHAKING_COUNT} changing direction under {@link #SHAKING_DELAY} milliseconds
     */
    private void changingDirection() {
        directionToRight = !directionToRight;

        changingDirectionQueue.addFirst(System.currentTimeMillis());

        if (changingDirectionQueue.size() > SHAKING_COUNT) {
            changingDirectionQueue.removeLast();

            long difference = changingDirectionQueue.getFirst() - changingDirectionQueue.getLast();
            if (difference < SHAKING_DELAY) {
                changingDirectionQueue.clear();
                if (mOnHeadGestureListener != null) {
                    mOnHeadGestureListener.onShake();
                }
            }
        }
    }


    /*
    IMPLEMENTS
     */


    // SensorEventListener
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {

            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);

            int azimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rotationMatrix, orientation)[0]) + 360) % 360;
            int pitch = (int) (Math.toDegrees(SensorManager.getOrientation(rotationMatrix, orientation)[1]));
            mRoll = (int) Math.toDegrees(SensorManager.getOrientation(rotationMatrix, orientation)[2]);
            mRoll = mRoll < 0 ? mRoll + 180 : mRoll - 180;

            if (mOnHeadTrackingListener != null)
                mOnHeadTrackingListener.onDirectionChanged(azimuth, pitch, mRoll + HeadRecognition.DELTA_NOD);

            // TODO: use directly radian instead of degree to avoid some useless computing
            if (tiltTime == -1) { // if head was straight
                if (pitch >= TILT_THRESHOLD) { // then if tilt detected for one way
                    tiltTime = System.currentTimeMillis(); // start measuring time
                    tiltSide = TILT_RIGHT; // precise which direction
                } else if (pitch <= -TILT_THRESHOLD) { // then if tilt detected for the other way
                    tiltTime = System.currentTimeMillis(); // start measuring time
                    tiltSide = TILT_LEFT; // precise which direction
                }
            }
            if (tiltTime != -1 && (pitch <= 5 && pitch >= -5)) { // if time started and head almost straight
                long delay = System.currentTimeMillis() - tiltTime; // measure delay
                if (delay <= TILT_TIME) { // if delay < time allowed
                    if (mOnHeadGestureListener != null)
                        mOnHeadGestureListener.onTilt(tiltSide); // detect a head gesture (tilt)
                }
                tiltTime = -1; // back to straight
            }

            // TODO : the same as Tilt, should create a method
            
            if (nodTime == -1) {
                if ((mRoll + HeadRecognition.DELTA_NOD) >= NOD_THRESHOLD) {
                    nodTime = System.currentTimeMillis();
                    nodSide = NOD_UP;
                } else if ((mRoll + HeadRecognition.DELTA_NOD) <= -NOD_THRESHOLD) {
                    nodTime = System.currentTimeMillis();
                    nodSide = NOD_DOWN;
                }
            }
            if (nodTime != -1 && ((mRoll + HeadRecognition.DELTA_NOD) <= 5 && (mRoll + HeadRecognition.DELTA_NOD) >= -5)) {
                long delay = System.currentTimeMillis() - nodTime;
                if (delay <= NOD_TIME) {
                    if (mOnHeadGestureListener != null) {
                        mOnHeadGestureListener.onNod(nodSide);
                    }
                }
                nodTime = -1;
            }

            int difference = azimuth - lastAzimuth;

            // moving right
            if ((difference > 0 && difference < SHAKING_MAX_ANGLE) || difference < -SHAKING_MAX_ANGLE) {
                if (!directionToRight) {
                    changingDirection();
                }
            }

            // moving left
            if ((difference < 0 && difference > -SHAKING_MAX_ANGLE) || difference > SHAKING_MAX_ANGLE) {
                if (directionToRight) {
                    changingDirection();
                }
            }

            // update lastAzimuth for next event
            lastAzimuth = azimuth;
        }
    }


    // SensorEventListener
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
