package com.golfcaddieai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.golfcaddieai.ui.theme.GolfCaddieAITheme
import com.golfcaddieai.viewmodel.GolfViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GolfCaddieAITheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val viewModel: GolfViewModel = viewModel()
                    GolfCaddieAIApp(viewModel)
                }
            }
        }
    }
}
