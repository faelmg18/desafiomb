package br.com.desafiomercantil.core.di.providers

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNotNull

class EncryptedSharedPreferencesProviderTest {

    private val context: Context = mockk()
    private val sharedPreferences: SharedPreferences = mockk()

    @Before
    fun setup() {
        clearAllMocks()
    }

    @After
    fun teardown() {
        unmockkAll()
    }

    @Test
    fun `should return encrypted shared preferences when no exception`() {
        mockkStatic(EncryptedSharedPreferences::class)
        mockkStatic("java.security.KeyStore") // Para mockar getInstance

        val mockKeyStore = mockk<java.security.KeyStore>()
        val mockKeyGenerator = mockk<javax.crypto.KeyGenerator>()

        mockkStatic("java.security.KeyStore")
        every { java.security.KeyStore.getInstance("AndroidKeyStore") } returns mockKeyStore

        every { mockKeyStore.load(null) } just Runs

        every { mockKeyStore.containsAlias("secure_key") } returns true

        mockkStatic("javax.crypto.KeyGenerator")
        every {
            javax.crypto.KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
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

        val result = provideEncryptedSharedPreferences(context)

        assertNotNull(result)
        verify(exactly = 0) { context.deleteSharedPreferences(any()) }

        unmockkStatic(EncryptedSharedPreferences::class)
        unmockkStatic("java.security.KeyStore")
        unmockkStatic("javax.crypto.KeyGenerator")
    }

    @Test
    fun `should delete shared preferences and recreate when exception thrown`() {
        mockkStatic(EncryptedSharedPreferences::class)

        mockkConstructor(java.security.KeyStore::class)
        every { anyConstructed<java.security.KeyStore>().load(null) } throws Exception("fail")

        every {
            EncryptedSharedPreferences.create(any(), any(), any(), any(), any())
        } returns sharedPreferences

        every { context.deleteSharedPreferences(any()) } returns true

        val result = provideEncryptedSharedPreferences(context)

        assertNotNull(result)
        verify { context.deleteSharedPreferences("secure_prefs") }

        unmockkStatic(EncryptedSharedPreferences::class)
        unmockkConstructor(java.security.KeyStore::class)
    }
}
