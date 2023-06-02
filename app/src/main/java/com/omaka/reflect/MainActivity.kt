package com.omaka.reflect

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.math.MathUtils
import com.omaka.reflect.ui.theme.ReflectTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.random.Random

const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReflectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Home()
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Home() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var text by remember {
        mutableStateOf("")
    }
    Box {
        CustomLayout()
    }
//    Box {
//        BasicTextField(
//            modifier = Modifier.fillMaxWidth(),
//            value = text,
//            onValueChange = {
//                text = it
//            },
//            textStyle = MaterialTheme.typography.bodyLarge
//        ) { innerTextField ->
//            Box(
//                Modifier
//                    .fillMaxWidth()
////                            .border(
////                                width = 1.0f.dp,
////                                Color.Red,
////                                shape = MaterialTheme.shapes.large,
////                            )
//                    .clip(MaterialTheme.shapes.large)
//                    .background(MaterialTheme.colorScheme.primaryContainer)
//                    .padding(16.0f.dp)
//            ) {
//                innerTextField()
//            }
//        }
//        LazyColumn {
//            items(20) {
//                Box(
//                    Modifier
//                        .fillMaxWidth()
//                        .height(100.dp)
//                        .background(
//                            Color(Random.nextLong(0xFFFFFFFF))
//                        )
//                ) {
//                    Text(text = it.toString(), fontSize = 30.sp)
//                }
//            }
//
//        }
//    }

}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ReflectTheme {
        Home()
    }
}

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
    val scrollState = rememberScrollState()

    val nestedScrollConnection = object : NestedScrollConnection {
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            val newOffset = height + available.y
            height = MathUtils.clamp(newOffset, minHeight, maxHeight)
            val excess = newOffset - height

            return Offset(0f, available.y - excess)
        }

        override suspend fun onPreFling(available: Velocity): Velocity {
            val velocityTracker = VelocityTracker()
//            velocityTracker.
            return available
        }
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
    Layout(content = {
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