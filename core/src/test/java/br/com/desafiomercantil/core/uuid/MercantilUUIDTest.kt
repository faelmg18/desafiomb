package br.com.desafiomercantil.core.uuid

import android.content.Context
import android.util.Base64
import br.com.desafiomercantil.core.cacheprovider.CacheProvider
import br.com.desafiomercantil.core.device.getAccelerometerAndLightSensorInfo
import br.com.desafiomercantil.core.device.getHardwareInfo
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class MercantilUUIDImplTest {

    private val context: Context = mockk(relaxed = true)
    private val cacheProvider: CacheProvider = mockk(relaxed = true)
    private lateinit var uuidGenerator: MercantilUUIDImpl

    @Before
    fun setUp() {
        uuidGenerator = MercantilUUIDImpl(context, cacheProvider)

        mockkStatic(::getAccelerometerAndLightSensorInfo)
        mockkStatic(::getHardwareInfo)
        mockkStatic(Base64::class)
    }

    @Test
    fun `getUUID should return cached UUID if available`() {
        val cachedUuid = "cached-uuid"
        every { cacheProvider.getString(MercantilUUIDImpl.KEY_UUID) } returns cachedUuid

        val result = uuidGenerator.getUUID()

        assertEquals(cachedUuid, result)
        verify(exactly = 1) { cacheProvider.getString(MercantilUUIDImpl.KEY_UUID) }
        verify(exactly = 0) { cacheProvider.saveString(any(), any()) }
    }
}
