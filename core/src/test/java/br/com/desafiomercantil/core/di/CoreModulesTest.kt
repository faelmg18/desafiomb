package br.com.desafiomercantil.core.di

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import br.com.desafiomercantil.core.cacheprovider.CacheProvider
import br.com.desafiomercantil.core.cacheprovider.CacheProviderImpl
import br.com.desafiomercantil.core.uuid.MercantilUUID
import br.com.desafiomercantil.core.uuid.MercantilUUIDImpl
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.security.KeyStore

@Config(sdk = [24])
@RunWith(RobolectricTestRunner::class)
class CoreModulesTest : KoinTest {

    private val sharedPreferences: SharedPreferences = mockk(relaxed = true)
    private val context: Context = mockk(relaxed = true)

    val injectedSharedPreferences: SharedPreferences by inject()
    val injectedCacheProvider: CacheProvider by inject()
    val injectedUUID: MercantilUUID by inject()


    @Before
    fun setUp() {

        mockkStatic(EncryptedSharedPreferences::class)
        mockkStatic("java.security.KeyStore")

        val mockKeyStore = mockk<KeyStore>()
        val mockKeyGenerator = mockk<javax.crypto.KeyGenerator>()

        mockkStatic("java.security.KeyStore")
        every { KeyStore.getInstance("AndroidKeyStore") } returns mockKeyStore

        every { mockKeyStore.load(null) } just Runs

        every { mockKeyStore.containsAlias("secure_key") } returns true

        mockkStatic("javax.crypto.KeyGenerator")
        every {
            javax.crypto.KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                "AndroidKeyStore"
            )
        } returns mockKeyGenerator

        every { mockKeyGenerator.init(any<java.security.spec.AlgorithmParameterSpec>()) } just Runs
        every { mockKeyGenerator.generateKey() } returns mockk()

        every {
            EncryptedSharedPreferences.create(
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns sharedPreferences

        every { context.deleteSharedPreferences(any()) } returns true

        startKoin {
            androidContext(context)
            modules(coreModules)
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `sharedPreferences should be provided by Koin`() {
        assertNotNull(injectedSharedPreferences)
    }

    @Test
    fun `cacheProvider should be provided and be instance of CacheProviderImpl`() {
        assertNotNull(injectedCacheProvider)
        assert(injectedCacheProvider is CacheProviderImpl)
    }

    @Test
    fun `uuid should be provided and be instance of MercantilUUIDImpl`() {
        assertNotNull(injectedUUID)
        assert(injectedUUID is MercantilUUIDImpl)
    }
}
