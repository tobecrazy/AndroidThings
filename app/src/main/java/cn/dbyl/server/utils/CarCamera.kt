package cn.dbyl.server.utils

import android.content.Context
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.hardware.camera2.CameraCaptureSession.CaptureCallback
import android.media.ImageReader
import android.media.ImageReader.OnImageAvailableListener
import android.os.Handler
import android.util.Log

/**
 * Create by Young on 12/24/2019
 **/


/**
 * Helper class to deal with methods to deal with images from the camera.
 */
class CarCamera  // Lazy-loaded singleton, so only one instance of the camera is created.
private constructor() {
    private var mCameraDevice: CameraDevice? = null
    private var mCaptureSession: CameraCaptureSession? = null
    /**
     * An [ImageReader] that handles still image capture.
     */
    private var mImageReader: ImageReader? = null

    private object InstanceHolder {
        val mCamera = CarCamera()
    }

    /**
     * Initialize the camera device
     */
    fun initializeCamera(
        context: Context,
        backgroundHandler: Handler?,
        imageAvailableListener: OnImageAvailableListener?
    ) { // Discover the camera instance
        val manager =
            context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        var camIds = arrayOf<String>()
        try {
            camIds = manager.cameraIdList
        } catch (e: CameraAccessException) {
            Log.d(TAG, "Cam access exception getting IDs", e)
        }
        if (camIds.isEmpty()) {
            Log.d(TAG, "No cameras found")
            return
        }
        val id = camIds[0]
        Log.d(TAG, "Using camera id $id")
        // Initialize the image processor
        mImageReader = ImageReader.newInstance(
            IMAGE_WIDTH, IMAGE_HEIGHT,
            ImageFormat.JPEG, MAX_IMAGES
        )
        mImageReader?.setOnImageAvailableListener(
            imageAvailableListener, backgroundHandler
        )
        // Open the camera resource
        try {
            manager.openCamera(id, mStateCallback, backgroundHandler)
        } catch (cae: CameraAccessException) {
            Log.d(TAG, "Camera access exception", cae)
        }
    }

    /**
     * Callback handling device state changes
     */
    private val mStateCallback: CameraDevice.StateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(cameraDevice: CameraDevice) {
            Log.d(TAG, "Opened camera.")
            mCameraDevice = cameraDevice
        }

        override fun onDisconnected(cameraDevice: CameraDevice) {
            Log.d(TAG, "Camera disconnected, closing.")
            cameraDevice.close()
        }

        override fun onError(cameraDevice: CameraDevice, i: Int) {
            Log.d(TAG, "Camera device error, closing.")
            cameraDevice.close()
        }

        override fun onClosed(cameraDevice: CameraDevice) {
            Log.d(TAG, "Closed camera, releasing")
            mCameraDevice = null
        }
    }

    /**
     * Begin a still image capture
     */
    fun takePicture() {
        if (mCameraDevice == null) {
            Log.w(
                TAG,
                "Cannot capture image. Camera not initialized."
            )
            return
        }
        // Here, we create a CameraCaptureSession for capturing still images.
        try {
            mCameraDevice!!.createCaptureSession(
                listOf(mImageReader!!.surface),
                mSessionCallback,
                null
            )
        } catch (cae: CameraAccessException) {
            Log.d(
                TAG,
                "access exception while preparing pic",
                cae
            )
        }
    }

    /**
     * Callback handling session state changes
     */
    private val mSessionCallback: CameraCaptureSession.StateCallback =
        object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(cameraCaptureSession: CameraCaptureSession) { // The camera is already closed
                if (mCameraDevice == null) {
                    return
                }
                // When the session is ready, we start capture.
                mCaptureSession = cameraCaptureSession
                triggerImageCapture()
            }

            override fun onConfigureFailed(cameraCaptureSession: CameraCaptureSession) {
                Log.w(TAG, "Failed to configure camera")
            }
        }

    /**
     * Execute a new capture request within the active session
     */
    private fun triggerImageCapture() {
        try {
            val captureBuilder =
                mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            captureBuilder.addTarget(mImageReader!!.surface)
            captureBuilder.set(
                CaptureRequest.CONTROL_AE_MODE,
                CaptureRequest.CONTROL_AE_MODE_ON
            )
            Log.d(TAG, "Session initialized.")
            mCaptureSession!!.capture(captureBuilder.build(), mCaptureCallback, null)
        } catch (cae: CameraAccessException) {
            Log.d(TAG, "camera capture exception")
        }
    }

    /**
     * Callback handling capture session events
     */
    private val mCaptureCallback: CaptureCallback = object : CaptureCallback() {
        override fun onCaptureProgressed(
            session: CameraCaptureSession,
            request: CaptureRequest,
            partialResult: CaptureResult
        ) {
            Log.d(TAG, "Partial result")
        }

        override fun onCaptureCompleted(
            session: CameraCaptureSession,
            request: CaptureRequest,
            result: TotalCaptureResult
        ) {
            session.close()
            mCaptureSession = null
            Log.d(TAG, "CaptureSession closed")
        }
    }

    /**
     * Close the camera resources
     */
    fun shutDown() {
        if (mCameraDevice != null) {
            mCameraDevice!!.close()
        }
    }

    companion object {
        private val TAG = CarCamera::class.java.simpleName
        private const val IMAGE_WIDTH = 1280
        private const val IMAGE_HEIGHT = 720
        private const val MAX_IMAGES = 1
        val instance: CarCamera
            get() = InstanceHolder.mCamera

        /**
         * Helpful debugging method:  Dump all supported camera formats to log.  You don't need to run
         * this for normal operation, but it's very helpful when porting this code to different
         * hardware.
         */
        fun dumpFormatInfo(context: Context) {
            val manager =
                context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            var camIds = arrayOf<String>()
            try {
                camIds = manager.cameraIdList
            } catch (e: CameraAccessException) {
                Log.d(TAG, "Cam access exception getting IDs")
            }
            if (camIds.isEmpty()) {
                Log.d(TAG, "No cameras found")
            } else {
                val id = camIds[0]
                Log.d(TAG, "Using camera id $id")
                try {
                    val characteristics =
                        manager.getCameraCharacteristics(id)
                    val configs =
                        characteristics.get(
                            CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP
                        )
                    for (format in configs!!.outputFormats) {
                        Log.d(
                            TAG,
                            "Getting sizes for format: $format"
                        )
                        for (s in configs.getOutputSizes(format)) {
                            Log.d(TAG, "\t" + s.toString())
                        }
                    }
                    val effects =
                        characteristics.get(CameraCharacteristics.CONTROL_AVAILABLE_EFFECTS)
                    for (effect in effects!!) {
                        Log.d(
                            TAG,
                            "Effect available: $effect"
                        )
                    }
                } catch (e: CameraAccessException) {
                    Log.d(
                        TAG,
                        "Cam access exception getting characteristics."
                    )
                }
            }
        }
    }
}
