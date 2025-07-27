package br.com.desafiomercantil.core.device

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build

fun getAccelerometerAndLightSensorInfo(context: Context): String {
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

    val sensorInfo = buildString {
        append("ACCEL:")
        append(accelerometer?.vendor ?: "null")
        append(accelerometer?.version ?: "null")
        append(accelerometer?.maximumRange ?: "null")

        append("LIGHT:")
        append(lightSensor?.vendor ?: "null")
        append(lightSensor?.version ?: "null")
        append(lightSensor?.maximumRange ?: "null")
    }

    return sensorInfo
}

fun getHardwareInfo() = buildString {
    append(Build.MANUFACTURER)
    append(Build.MODEL)
    append(Build.VERSION.SDK_INT)
    append(Build.HARDWARE)
}