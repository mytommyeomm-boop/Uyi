package com.vyra.studio.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vyra.studio.ui.theme.Neon

fun Modifier.pressScale(src: InteractionSource, target: Float = 0.96f): Modifier = composed {
    val pressed by src.collectIsPressedAsState()
    val s by animateFloatAsState(if (pressed) target else 1f, tween(120), label = "press")
    graphicsLayer { scaleX = s; scaleY = s }
}

/** Vyra logo: a gradient ring around a solid "record" core. */
@Composable
fun LogoMark(logoSize: Dp, animated: Boolean = false) {
    val rotation = if (animated) {
        val t = rememberInfiniteTransition(label = "logo")
        val r by t.animateFloat(
            0f, 360f,
            infiniteRepeatable(tween(6000, easing = LinearEasing), RepeatMode.Restart),
            label = "rot",
        )
        r
    } else 0f
    Canvas(Modifier.size(logoSize)) {
        val s = this.size.minDimension
        val stroke = s * 0.09f
        rotate(rotation) {
            drawArc(
                brush = Brush.sweepGradient(listOf(Neon.Blue, Neon.Violet, Neon.Cyan, Neon.Blue)),
                startAngle = 30f,
                sweepAngle = 300f,
                useCenter = false,
                topLeft = Offset(stroke / 2, stroke / 2),
                size = Size(s - stroke, s - stroke),
                style = Stroke(stroke, cap = StrokeCap.Round),
            )
        }
        drawCircle(
            brush = Brush.linearGradient(listOf(Neon.Blue, Neon.Violet), Offset(0f, 0f), Offset(s, s)),
            radius = s * 0.22f,
            center = center,
        )
    }
}

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit,
) {
    val src = remember { MutableInteractionSource() }
    val shape = RoundedCornerShape(24.dp)
    var m = modifier.pressScale(src, 0.97f).clip(shape).background(Neon.Surface).border(1.dp, Neon.Border, shape)
    if (onClick != null) m = m.clickable(interactionSource = src, indication = null, onClick = onClick)
    Column(m.padding(20.dp), horizontalAlignment = horizontalAlignment, content = content)
}

@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    height: Dp = 64.dp,
) {
    val src = remember { MutableInteractionSource() }
    val shape = RoundedCornerShape(22.dp)
    Row(
        modifier
            .fillMaxWidth()
            .height(height)
            .pressScale(src)
            .shadow(16.dp, shape, ambientColor = Neon.Violet, spotColor = Neon.Violet)
            .clip(shape)
            .background(Neon.Brand)
            .clickable(interactionSource = src, indication = null, onClick = onClick),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Icon(icon, null, tint = Color.White, modifier = Modifier.size(26.dp))
            Spacer(Modifier.width(10.dp))
        }
        Text(text, style = MaterialTheme.typography.titleMedium, color = Color.White)
    }
}

@Composable
fun SecondaryButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val src = remember { MutableInteractionSource() }
    val shape = RoundedCornerShape(18.dp)
    Box(
        modifier
            .fillMaxWidth()
            .height(54.dp)
            .pressScale(src)
            .clip(shape)
            .border(1.dp, Neon.Border, shape)
            .background(Neon.SurfaceHigh)
            .clickable(interactionSource = src, indication = null, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(text, style = MaterialTheme.typography.labelLarge, color = Neon.Text)
    }
}

@Composable
fun PulseDot(color: Color, active: Boolean = true) {
    val t = rememberInfiniteTransition(label = "pulse")
    val p by t.animateFloat(
        0f, 1f,
        infiniteRepeatable(tween(1400, easing = LinearEasing), RepeatMode.Restart),
        label = "p",
    )
    Box(Modifier.size(20.dp), contentAlignment = Alignment.Center) {
        if (active) {
            Box(
                Modifier
                    .size(20.dp)
                    .graphicsLayer { scaleX = 0.5f + 0.5f * p; scaleY = 0.5f + 0.5f * p; alpha = 1f - p }
                    .background(color.copy(alpha = 0.6f), CircleShape)
            )
        }
        Box(Modifier.size(10.dp).background(color, CircleShape))
    }
}

@Composable
fun InfoCell(icon: ImageVector, label: String, value: String, modifier: Modifier = Modifier) {
    Row(
        modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Neon.SurfaceHigh)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, null, tint = Neon.Cyan, modifier = Modifier.size(22.dp))
        Spacer(Modifier.width(10.dp))
        Column {
            Text(label, style = MaterialTheme.typography.labelMedium, color = Neon.TextDim)
            Text(value, style = MaterialTheme.typography.labelLarge, color = Neon.Text)
        }
    }
}

@Composable
fun EmptyState(icon: ImageVector, title: String, body: String, modifier: Modifier = Modifier) {
    Column(
        modifier.fillMaxWidth().padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            Modifier.size(64.dp).clip(CircleShape).background(Neon.SurfaceHigh),
            contentAlignment = Alignment.Center,
        ) { Icon(icon, null, tint = Neon.TextDim, modifier = Modifier.size(30.dp)) }
        Spacer(Modifier.height(14.dp))
        Text(title, style = MaterialTheme.typography.titleMedium, color = Neon.Text)
        Spacer(Modifier.height(4.dp))
        Text(body, style = MaterialTheme.typography.bodyMedium, color = Neon.TextDim, textAlign = TextAlign.Center)
    }
}

@Composable
fun OptionChip(label: String, selected: Boolean, onClick: () -> Unit) {
    val bg by animateColorAsState(if (selected) Neon.Blue else Neon.SurfaceHigh, tween(180), label = "chip")
    Box(
        Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(bg)
            .border(1.dp, if (selected) Neon.Blue else Neon.Border, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 11.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(label, style = MaterialTheme.typography.labelLarge, color = if (selected) Color.White else Neon.TextDim)
    }
}
