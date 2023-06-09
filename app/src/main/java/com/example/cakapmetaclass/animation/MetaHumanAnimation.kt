package com.example.cakapmetaclass.animation

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.cakapmetaclass.R
import kotlinx.coroutines.delay

@SuppressLint("UnrememberedMutableState")
@Composable
fun SpeakingMetahuman() {
    var mouthImage by mutableStateOf(R.drawable.one_close)
    LaunchedEffect(Unit) {
        val frames = listOf(
            R.drawable.one_close,
            R.drawable.two_close,
            R.drawable.three_close,
            R.drawable.four_close,
            R.drawable.five_close

        )





        for (frame in frames) {
            mouthImage = frame
            // Menunggu selama 200 milidetik sebelum beralih ke frame berikutnya
            delay(200)
        }
    }






    Image(
        painter = painterResource(mouthImage),
        contentDescription = "Metahuman Mouth",
        modifier = Modifier
            .drawBehind {
                // Contoh: Menggambar kotak di sekitar mulut untuk menunjukkan animasi
                drawRect(
                    color = Color.Transparent,
                    style = Stroke(width = 2.dp.toPx()),
                    topLeft = Offset(size.width / 4, size.height / 2),
                    size = Size(size.width / 2, size.height / 4)
                )
            }
    )
}


@Composable
fun MetahumanSpeakingAnimation() {

    val countdownSeconds = remember { mutableStateOf(5) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(200L) // Wait for 1 second
            countdownSeconds.value -= 1
            if (countdownSeconds.value == 0) {
                countdownSeconds.value = 5 // Reset the countdown to 5
            }
        }
    }

    val animatedCountdownSeconds by animateIntAsState(
        targetValue = countdownSeconds.value,
        animationSpec = tween(durationMillis = 200)
    )
    val currentFrameResId = getFrameResourceId(animatedCountdownSeconds)

    Image(
        painter = painterResource(currentFrameResId),
        contentDescription = "Metahuman Speaking Animation",
        modifier = Modifier.wrapContentHeight()
    )
}


private fun getFrameResourceId(frameIndex: Int): Int {
    // Return the resource ID of the frame drawable based on the frame index
    return when (frameIndex) {
        5 ->  R.drawable.one_close
        4 -> R.drawable.two_close
        3 -> R.drawable.three_close
        2 -> R.drawable.four_close
        1 -> R.drawable.five_close
        // Add more cases for additional frames
        else -> R.drawable.five_close // Default to the first frame
    }
}





@Composable
fun BlinkingRedDot(sizeDot: Dp = 9.dp) {
    val isBlinking = remember { mutableStateOf(true) }

    val blinkAnimation = rememberInfiniteTransition()
    val blinkAlpha = blinkAnimation.animateFloat(
        initialValue = 1f,
        targetValue = if (isBlinking.value) 0f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    Canvas(
        modifier = Modifier.size(sizeDot)
    ) {
        val dotRadius = size.minDimension / 2f
        drawCircle(
            color = Color.Blue.copy(alpha = blinkAlpha.value),
            radius = dotRadius,
            center = center
        )
    }
}