package com.pinu.jetpackcomposemodularprojectdemo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pinu.jetpackcomposemodularprojectdemo.ui.theme.JetpackComposeModularProjectDemoTheme
import com.pinu.jetpackcomposemodularprojectdemo.ui.theme.Pink
import com.pinu.jetpackcomposemodularprojectdemo.ui.theme.Pink40
import com.pinu.jetpackcomposemodularprojectdemo.ui.theme.Pink80
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetpackComposeModularProjectDemoTheme {
                SplashScreen()
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun SplashScreen() {

        val alpha = remember {
            Animatable(0f)
        }

        LaunchedEffect(key1 = true) {
            alpha.animateTo(1f, tween(1500, delayMillis = 500))
            delay(5000)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }

        Box(contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize().background(color = Pink80)) {
            Image(
                painter = painterResource(id = R.drawable.books),
                contentDescription = "Books",
                modifier = Modifier
                    .size(150.dp)
                    .alpha(alpha.value)
                    .clip(CircleShape)
                    .border(
                        4.dp, color = Pink40, shape = CircleShape
                    ),
            )
        }

    }
}