package br.com.desafiomercantil.core.uuid

import android.content.Context
import android.util.Base64
import br.com.desafiomercantil.core.cacheprovider.CacheProvider
import br.com.desafiomercantil.core.device.getAccelerometerAndLightSensorInfo
import br.com.desafiomercantil.core.device.getHardwareInfo
import java.security.MessageDigest

/**
 * -  Essa classe gera uma cave unica baseada em dois sensores do dispositivo
 * - O método getUUID retorna um ID unico baseado no accelerometer sensor e o light sensor.
 * caso exista uma chave ja gerada e salva no preference, eu retorno ela para não ficar consumindo recurso desnecessário
 * - O SHA-256 transforma os dados gerados em uma impressão digital única de 64 caracteres.
 * Referencias usada:
 * https://developer.android.com/reference/android/hardware/Sensor
 * https://developer.android.com/develop/sensors-and-location/sensors/sensors_motion?hl=pt-br
 */
class MercantilUUIDImpl(private val context: Context, private val cacheProvider: CacheProvider) :
    MercantilUUID {
    companion object {
        const val KEY_UUID = "sensor_uuid"
        private const val ALGORITHM = "SHA-256"
    }

    override fun getUUID(): String {
        cacheProvider.getString(KEY_UUID)?.let { return it }

        val sensorInfo = getAccelerometerAndLightSensorInfo(context)
        val hardwareInfo = getHardwareInfo()

        val rawData = sensorInfo + hardwareInfo
        val uuid = generateSha256(rawData)

        cacheProvider.saveString(uuid, KEY_UUID)

        return uuid
    }

    private fun generateSha256(input: String): String {
        val digest = MessageDigest.getInstance(ALGORITHM)
        val bytes = digest.digest(input.toByteArray())
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }
}