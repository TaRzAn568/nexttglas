
package com.nexttglas.nexttglas.ui.screen

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexttglas.nexttglas.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onAnimationFinished: () -> Unit) {
    var logoAlpha by remember { mutableFloatStateOf(0f) }
    var logoScale by remember { mutableFloatStateOf(0.5f) }
    var bidAlpha by remember { mutableFloatStateOf(0f) }
    var bidScale by remember { mutableFloatStateOf(0.7f) }
    var learnAlpha by remember { mutableFloatStateOf(0f) }
    var learnScale by remember { mutableFloatStateOf(0.7f) }
    var prosperAlpha by remember { mutableFloatStateOf(0f) }
    var prosperScale by remember { mutableFloatStateOf(0.7f) }

    LaunchedEffect(key1 = true) {
        // Animate logo
        logoAlpha = 1f
        logoScale = 1f
        delay(800)

        // Show Bid
        bidAlpha = 1f
        bidScale = 1f
        delay(700)

        // Show Learn
        learnAlpha = 1f
        learnScale = 1f
        delay(700)

        // Show Prosper
        prosperAlpha = 1f
        prosperScale = 1f
        delay(1200)

        onAnimationFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo with animation - positioned towards top
            Spacer(modifier = Modifier.weight(0.8f))

            Image(
                painter = painterResource(id = R.drawable.nextglas_circular_logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(180.dp)
                    .scale(
                        animateFloatAsState(
                            targetValue = logoScale,
                            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
                            label = "logo_scale"
                        ).value
                    )
                    .alpha(
                        animateFloatAsState(
                            targetValue = logoAlpha,
                            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
                            label = "logo_alpha"
                        ).value
                    )
            )

            Spacer(modifier = Modifier.height(64.dp))

            // Three texts in vertical layout - each with independent animation
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Bid text
                Text(
                    text = "Bid",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 2.sp
                    ),
                    modifier = Modifier
                        .scale(
                            animateFloatAsState(
                                targetValue = bidScale,
                                animationSpec = tween(
                                    durationMillis = 700,
                                    easing = FastOutSlowInEasing
                                ),
                                label = "bid_scale"
                            ).value
                        )
                        .alpha(
                            animateFloatAsState(
                                targetValue = bidAlpha,
                                animationSpec = tween(
                                    durationMillis = 700,
                                    easing = FastOutSlowInEasing
                                ),
                                label = "bid_alpha"
                            ).value
                        )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Learn text
                Text(
                    text = "Learn",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 2.sp
                    ),
                    modifier = Modifier
                        .scale(
                            animateFloatAsState(
                                targetValue = learnScale,
                                animationSpec = tween(
                                    durationMillis = 700,
                                    easing = FastOutSlowInEasing
                                ),
                                label = "learn_scale"
                            ).value
                        )
                        .alpha(
                            animateFloatAsState(
                                targetValue = learnAlpha,
                                animationSpec = tween(
                                    durationMillis = 700,
                                    easing = FastOutSlowInEasing
                                ),
                                label = "learn_alpha"
                            ).value
                        )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Prosper text
                Text(
                    text = "Prosper",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 2.sp
                    ),
                    modifier = Modifier
                        .scale(
                            animateFloatAsState(
                                targetValue = prosperScale,
                                animationSpec = tween(
                                    durationMillis = 700,
                                    easing = FastOutSlowInEasing
                                ),
                                label = "prosper_scale"
                            ).value
                        )
                        .alpha(
                            animateFloatAsState(
                                targetValue = prosperAlpha,
                                animationSpec = tween(
                                    durationMillis = 700,
                                    easing = FastOutSlowInEasing
                                ),
                                label = "prosper_alpha"
                            ).value
                        )
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

