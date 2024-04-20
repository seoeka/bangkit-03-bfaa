@file:Suppress("DEPRECATION")

package com.seoeka.githubuser.ui.mvvm.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.seoeka.githubuser.utils.SettingPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SettingViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var settingViewModel: SettingViewModel

    @Mock
    private lateinit var mockSettingPreferences: SettingPreferences

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @Before
    fun setup() {
        settingViewModel = SettingViewModel(mockSettingPreferences)
    }

    @After
    fun tearDown() {
        testScope.cleanupTestCoroutines()
    }

    @Test
    fun `getThemeSettings should return theme settings from preferences`() = testScope.runBlockingTest {
        // Given
        val expectedThemeSetting = true
        val themeSettingFlow = MutableStateFlow(expectedThemeSetting)
        Mockito.`when`(mockSettingPreferences.getThemeSetting()).thenReturn(themeSettingFlow)

        // When
        val themeSettingLiveData = settingViewModel.getThemeSettings()

        // Then
        val observer = Observer<Boolean> { themeSetting ->
            assert(themeSetting == expectedThemeSetting)
        }
        themeSettingLiveData.observeForever(observer)
    }

    @Test
    fun `saveThemeSetting should save theme setting to preferences`() = testScope.runBlockingTest {
        // Given
        val isDarkModeActive = true

        // When
        settingViewModel.saveThemeSetting(isDarkModeActive)

        // Then
        Mockito.verify(mockSettingPreferences).saveThemeSetting(isDarkModeActive)
    }

    // Set up Dispatchers.Main for testing
    @Before
    fun setupDispatchers() {
        kotlinx.coroutines.Dispatchers.setMain(testDispatcher)
    }

    // Reset Dispatchers.Main after testing
    @After
    fun resetDispatchers() {
        kotlinx.coroutines.Dispatchers.resetMain()
    }
}
