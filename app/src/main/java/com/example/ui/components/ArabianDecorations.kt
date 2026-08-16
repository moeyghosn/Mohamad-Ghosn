package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ArabianMidnight
import com.example.ui.theme.ArabianNightBorder
import com.example.ui.theme.ArabianNightCard
import com.example.ui.theme.ArabianNightSurface
import com.example.ui.theme.DesertAmber
import com.example.ui.theme.HeritageGoldLight
import com.example.ui.theme.HeritageGoldPrimary
import com.example.ui.theme.ParchmentMuted
import com.example.ui.theme.ParchmentWhite

@Composable
fun ArabianHeritageScaffoldBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "stars_glow")
    val alphaAnim by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ambient_glow"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        ArabianMidnight,
                        ArabianNightSurface,
                        Color(0xFF140D22)
                    )
                )
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Star points across the Arabian night sky
            val stars = listOf(
                Offset(width * 0.15f, height * 0.08f),
                Offset(width * 0.82f, height * 0.05f),
                Offset(width * 0.45f, height * 0.12f),
                Offset(width * 0.90f, height * 0.18f),
                Offset(width * 0.28f, height * 0.22f),
                Offset(width * 0.72f, height * 0.35f),
                Offset(width * 0.10f, height * 0.42f),
                Offset(width * 0.88f, height * 0.58f),
                Offset(width * 0.35f, height * 0.70f),
                Offset(width * 0.65f, height * 0.85f),
                Offset(width * 0.18f, height * 0.92f)
            )

            for (star in stars) {
                drawCircle(
                    color = HeritageGoldPrimary.copy(alpha = alphaAnim * 0.45f),
                    radius = 2.dp.toPx(),
                    center = star
                )
                drawCircle(
                    color = HeritageGoldLight.copy(alpha = alphaAnim * 0.8f),
                    radius = 1.dp.toPx(),
                    center = star
                )
            }

            // Subtle top Islamic 8-point geometric corner lines
            val cornerSize = 40.dp.toPx()
            val strokeWidth = 1.dp.toPx()
            val goldColor = HeritageGoldPrimary.copy(alpha = 0.25f)

            // Top right corner
            drawLine(goldColor, Offset(width - cornerSize, 0f), Offset(width, cornerSize), strokeWidth)
            drawLine(goldColor, Offset(width - cornerSize * 0.5f, 0f), Offset(width, cornerSize * 0.5f), strokeWidth)

            // Top left corner
            drawLine(goldColor, Offset(cornerSize, 0f), Offset(0f, cornerSize), strokeWidth)
            drawLine(goldColor, Offset(cornerSize * 0.5f, 0f), Offset(0f, cornerSize * 0.5f), strokeWidth)
        }

        content()
    }
}

@Composable
fun ArabianDivider(
    modifier: Modifier = Modifier,
    ornament: String = "❖ ۞ ❖"
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color.Transparent, HeritageGoldPrimary.copy(alpha = 0.5f))
                    )
                )
        )
        Text(
            text = " $ornament ",
            color = HeritageGoldPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(HeritageGoldPrimary.copy(alpha = 0.5f), Color.Transparent)
                    )
                )
        )
    }
}

@Composable
fun ArabianOrnamentalCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = ArabianNightCard,
    borderColor: Color = ArabianNightBorder,
    shape: RoundedCornerShape = RoundedCornerShape(20.dp),
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor)
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        HeritageGoldPrimary.copy(alpha = 0.6f),
                        borderColor,
                        ArabianNightBorder.copy(alpha = 0.3f)
                    )
                ),
                shape = shape
            )
    ) {
        content()
    }
}

@Composable
fun ArabianPoetryBanner(
    firstHemistich: String,
    secondHemistich: String,
    modifier: Modifier = Modifier,
    poetName: String? = null
) {
    ArabianOrnamentalCard(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = Color(0xFF1E152E),
        borderColor = HeritageGoldPrimary.copy(alpha = 0.5f)
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            // Gold border accent mark
            Canvas(modifier = Modifier.fillMaxSize()) {
                val p = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(20f, 0f)
                    moveTo(0f, 0f)
                    lineTo(0f, 20f)
                }
                drawPath(p, HeritageGoldPrimary.copy(alpha = 0.7f), style = Stroke(width = 2.dp.toPx()))
            }

            androidx.compose.foundation.layout.Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "📜 بيت من الشعر الخالد",
                    fontSize = 12.sp,
                    color = HeritageGoldLight,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = firstHemistich,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = ParchmentWhite,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "··· $secondHemistich ···",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = HeritageGoldLight,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )
                if (poetName != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "— $poetName",
                        fontSize = 12.sp,
                        color = DesertAmber,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
