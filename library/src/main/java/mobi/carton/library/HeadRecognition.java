package mobi.carton.library;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class HeadRecognition
        implements
        SensorEventListener {


    /*
    INTERFACES
    */


    public interface OnHeadGestureListener {

        void onTilt(int direction);
        void onNod(int direction);
    }


    public interface OnHeadTrackingListener {

        void onDirectionChanged(int azimuth, int pitch, int roll);
    }


    public static final int TILT_RIGHT = 0;
    public static final int TILT_LEFT = 1;

    private static final int TILT_TIME = 1000; // millisecond
    private static final int TILT_THRESHOLD = 25;

    public static final int NOD_UP = 2;
    public static final int NOD_DOWN = 3;

    private static final int NOD_TIME = 1000;
    private static final int NOD_THRESHOLD = 25;


    private SensorManager mSensorManager;

    private OnHeadGestureListener mOnHeadGestureListener;
    private OnHeadTrackingListener mOnHeadTrackingListener;

    private float[] orientation = new float[3];
    private float[] rotationMatrix = new float[9];

    private int mAzimuth = 0;
    private int mPitch = 0;
    private int mRoll = 0;

    private long tiltTime = -1;
    private int tiltSide = -1;


    private long nodTime = -1;
    private int nodSide = -1;


    public HeadRecognition(Context context) {
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);

            mAzimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rotationMatrix, orientation)[0]) + 360) % 360;
            mPitch = (int) (Math.toDegrees(SensorManager.getOrientation(rotationMatrix, orientation)[1]));
            mRoll = (int) Math.toDegrees(SensorManager.getOrientation(rotationMatrix, orientation)[2]);
            mRoll = mRoll < 0 ? mRoll + 180 : mRoll - 180;

            if (mOnHeadTrackingListener != null)
                mOnHeadTrackingListener.onDirectionChanged(mAzimuth, mPitch, mRoll);

            // TODO: use directly radian instead of degree to avoid some useless computing
            if (tiltTime == -1) {
                if (mPitch >= TILT_THRESHOLD) {
                    tiltTime = System.currentTimeMillis();
                    tiltSide = TILT_RIGHT;
                } else if (mPitch <= -TILT_THRESHOLD) {
                    tiltTime = System.currentTimeMillis();
                    tiltSide = TILT_LEFT;
                }
            }
            if (tiltTime != -1 && (mPitch <= 5 && mPitch >= -5)) {
                long delay = System.currentTimeMillis() - tiltTime;
                if (delay <= TILT_TIME) {
                    if (mOnHeadGestureListener != null)
                        mOnHeadGestureListener.onTilt(tiltSide);
                }
                tiltTime = -1;
            }


            if (nodTime == -1) {
                if (mRoll >= NOD_THRESHOLD) {
                    nodTime = System.currentTimeMillis();
                    nodSide = NOD_UP;
                } else if (mRoll <= -NOD_THRESHOLD) {
                    nodTime = System.currentTimeMillis();
                    nodSide = NOD_DOWN;
                }
            }
            if (nodTime != -1 && (mRoll <= 5 && mRoll >= -5)) {
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


    public void start() {
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
                SensorManager.SENSOR_DELAY_UI);
    }


    public void stop() {
        mSensorManager.unregisterListener(this);
    }


    /*
    SETTERS
     */


    public void setOnHeadGestureListener(OnHeadGestureListener l) {
        mOnHeadGestureListener = l;
    }


    public void setOnHeadTrackingListener(OnHeadTrackingListener l) {
        mOnHeadTrackingListener = l;
    }
}
