package br.com.desafiomercantil.core.cacheprovider

import android.content.SharedPreferences
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CacheProviderImplTest {

    private val sharedPreferences: SharedPreferences = mockk()
    private val editor: SharedPreferences.Editor = mockk()
    private lateinit var cacheProvider: CacheProvider

    @Before
    fun setup() {
        every { sharedPreferences.edit() } returns editor
        every { editor.putString(any(), any()) } returns editor
        every { editor.remove(any()) } returns editor
        every { editor.clear() } returns editor
        every { editor.apply() } just Runs

        cacheProvider = CacheProviderImpl(sharedPreferences)
    }

    @Test
    fun `saveString should call putString and apply`() {
        val key = "key1"
        val value = "value1"

        cacheProvider.saveString(value, key)

        verifySequence {
            sharedPreferences.edit()
            editor.putString(key, value)
            editor.apply()
        }
    }

    @Test
    fun `getString should return stored value`() {
        val key = "key2"
        val expectedValue = "storedValue"

        every { sharedPreferences.getString(key, null) } returns expectedValue

        val result = cacheProvider.getString(key)

        assertEquals(expectedValue, result)
        verify { sharedPreferences.getString(key, null) }
    }

    @Test
    fun `getString should return null if no value stored`() {
        val key = "key3"

        every { sharedPreferences.getString(key, null) } returns null

        val result = cacheProvider.getString(key)

        assertNull(result)
        verify { sharedPreferences.getString(key, null) }
    }

    @Test
    fun `clear should call clear and apply`() {
        cacheProvider.clear()

        verifySequence {
            sharedPreferences.edit()
            editor.clear()
            editor.apply()
        }
    }

    @Test
    fun `clearByKey should call remove and apply`() {
        val key = "key4"

        cacheProvider.clearByKey(key)

        verifySequence {
            sharedPreferences.edit()
            editor.remove(key)
            editor.apply()
        }
    }
}
