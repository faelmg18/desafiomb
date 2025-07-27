package br.com.desafiomercantil.core.device

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import io.mockk.*
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.R])
class DeviceInfoTest {

    private val context: Context = mockk()
    private val sensorManager: SensorManager = mockk()
    private val accelerometer: Sensor = mockk()
    private val lightSensor: Sensor = mockk()

    @Before
    fun setup() {
        every { context.getSystemService(Context.SENSOR_SERVICE) } returns sensorManager
    }

    @After
    fun teardown() {
        unmockkAll()
    }

    @Test
    fun `getAccelerometerAndLightSensorInfo returns sensor info when sensors are available`() {
        every { sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) } returns accelerometer
        every { sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) } returns lightSensor

        every { accelerometer.vendor } returns "VendorA"
        every { accelerometer.version } returns 1
        every { accelerometer.maximumRange } returns 9.8f

        every { lightSensor.vendor } returns "VendorL"
        every { lightSensor.version } returns 2
        every { lightSensor.maximumRange } returns 1000f

        val result = getAccelerometerAndLightSensorInfo(context)

        assertTrue(result.contains("ACCEL:VendorA1" + "9.8"))
        assertTrue(result.contains("LIGHT:VendorL2" + "1000.0"))
    }

    @Test
    fun `getAccelerometerAndLightSensorInfo returns null fields when sensors are missing`() {
        every { sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) } returns null
        every { sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) } returns null

        val result = getAccelerometerAndLightSensorInfo(context)

        assertTrue(result.contains("ACCEL:nullnullnull"))
        assertTrue(result.contains("LIGHT:nullnullnull"))
    }

    @Test
    fun `getHardwareInfo should return non-empty hardware details`() {
        val result = getHardwareInfo()

        assertTrue(result.contains(Build.MANUFACTURER))
        assertTrue(result.contains(Build.MODEL))
        assertTrue(result.contains(Build.VERSION.SDK_INT.toString()))
        assertTrue(result.contains(Build.HARDWARE))
    }
}
