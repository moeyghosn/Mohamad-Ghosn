package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.example.ui.theme.HakawatiTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleUnitTest {

  @Test
  fun testHistoricalStoriesLoaded() {
    val stories = com.example.data.model.HistoricalData.stories
    org.junit.Assert.assertTrue(stories.isNotEmpty())
    org.junit.Assert.assertEquals(4, stories.size)
  }
}
