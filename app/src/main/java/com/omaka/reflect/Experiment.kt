package com.omaka.reflect

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.intermediateLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.math.MathUtils
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun CustomLayout() {
    val maxDpHeight = 400.0f.dp
    val minDpHeight = 80.0f.dp
    val (minHeight, maxHeight) = with(LocalDensity.current) {
        Pair(minDpHeight.toPx(), maxDpHeight.toPx())
    }

    var height by remember {
        mutableStateOf(maxHeight)
    }
    var target = 0f
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    val nestedScrollConnection = object : NestedScrollConnection {
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            val newOffset = height + available.y
            height = MathUtils.clamp(newOffset, minHeight, maxHeight)
            val excess = newOffset - height

            return Offset(0f, available.y - excess)
        }

        override fun onPostScroll(
            consumed: Offset,
            available: Offset,
            source: NestedScrollSource
        ): Offset {
            return Offset.Zero
        }
    }
    LaunchedEffect(key1 = target ) {
        height = target
    }
    val interactionSouce = remember {
        MutableInteractionSource()
    }

    Layout(
        modifier = Modifier.nestedScroll(
            nestedScrollConnection,
        ),
        content = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(with(LocalDensity.current) {
                        height.toDp()
                    })
            ) {
                Header(
                    Modifier.animateConstraints(),
                    content = {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(200f.dp)
                                .background(Color.Red)
                        )
                    },
                    subContents = {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(100f.dp)
                                .background(Color.Cyan)
                        )
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(100f.dp)
                                .background(Color.Magenta)
                        )
                    }
                )
            }

//            Box(
//                Modifier
//                    .fillMaxSize()
//                    .background(Color.Blue)
//                    .clickable {
//                        Log.d(TAG, "CustomLayout: Scrolling")
//                    }
//                    .verticalScroll(scrollState))

            val state = rememberLazyListState()
            LazyColumn {
                items(20) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(
                                Color(0xFF000000 + Random.nextLong(0xFFFFFF))
                            )
                    ) {
                        Text(text = it.toString(), fontSize = 30.sp)
                    }
                }

            }

        },
    ) { measurables, constraints ->
        val header = measurables.first().measure(constraints)
        val footer = measurables.last().measure(constraints)
        layout(constraints.maxWidth, constraints.maxHeight) {
            header.placeRelative(x = 0, y = 0)
            footer.placeRelative(x = 0, y = header.height)
        }
    }
}

@Composable
fun Header(
    modifier: Modifier = Modifier,
    content: @Composable (() -> Unit),
    subContents: @Composable (() -> Unit)
) {
    Layout(modifier = modifier, content = {
        content()
        subContents()
    }) { measurables, constraints ->
        val title = measurables.first().measure(constraints)

        val children = measurables.drop(1).map { measurable ->
            measurable.measure(constraints)
        }

        layout(constraints.maxWidth, constraints.maxHeight) {
            title.placeRelative(x = 0, y = 0)

            var yPosition = title.height

            for (child in children) {
                child.placeRelative(0, yPosition)
                yPosition += child.height
            }
        }

    }
}

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.animateConstraints() = composed {
    // Creates a size animation
    var sizeAnimation: Animatable<IntSize, AnimationVector2D>? by remember {
        mutableStateOf(null)
    }

    this.intermediateLayout { measurable, _ ->
        // When layout changes, the lookahead pass will calculate a new final size for the
        // child layout. This lookahead size can be used to animate the size
        // change, such that the animation starts from the current size and gradually
        // change towards `lookaheadSize`.
        if (lookaheadSize != sizeAnimation?.targetValue) {
            sizeAnimation?.run {
                launch { animateTo(lookaheadSize) }
            } ?: Animatable(lookaheadSize, IntSize.VectorConverter).let {
                sizeAnimation = it
            }
        }
        val (width, height) = sizeAnimation!!.value
        // Creates a fixed set of constraints using the animated size
        val animatedConstraints = Constraints.fixed(width, height)
        // Measure child with animated constraints.
        val placeable = measurable.measure(animatedConstraints)
        layout(placeable.width, placeable.height) {
            placeable.place(0, 0)
        }
    }
}