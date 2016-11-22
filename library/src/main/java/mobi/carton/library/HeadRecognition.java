package mobi.carton.library;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

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
        //void onShake();
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
    public static final int TILT_RIGHT = 0;
    public static final int TILT_LEFT = 1;
    public static final int NOD_UP = 2;
    public static final int NOD_DOWN = 3;

    /**
     * Number of changing direction needed to detect a shaking gesture
     */
    private static final int SHAKE_NB_CHANGING_DIRECTION = 5;


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


    private SensorManager mSensorManager;


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
     * Constructor that is called when instantiate a HeadRecognition object.
     *
     * @param context The context the HeadRecognition is running, through which it
     *                can access the sensor service.
     */
    public HeadRecognition(Context context) {
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {

            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);

            int azimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rotationMatrix, orientation)[0]) + 360) % 360;
            int pitch = (int) (Math.toDegrees(SensorManager.getOrientation(rotationMatrix, orientation)[1]));
            int roll = (int) Math.toDegrees(SensorManager.getOrientation(rotationMatrix, orientation)[2]);
            roll = roll < 0 ? roll + 180 : roll - 180;

            if (mOnHeadTrackingListener != null)
                mOnHeadTrackingListener.onDirectionChanged(azimuth, pitch, roll);

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
                if (roll >= NOD_THRESHOLD) {
                    nodTime = System.currentTimeMillis();
                    nodSide = NOD_UP;
                } else if (roll <= -NOD_THRESHOLD) {
                    nodTime = System.currentTimeMillis();
                    nodSide = NOD_DOWN;
                }
            }
            if (nodTime != -1 && (roll <= 5 && roll >= -5)) {
                long delay = System.currentTimeMillis() - nodTime;
                if (delay <= NOD_TIME) {
                    if (mOnHeadGestureListener != null)
                        mOnHeadGestureListener.onNod(nodSide);
                }
                nodTime = -1;
            }
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

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
}
