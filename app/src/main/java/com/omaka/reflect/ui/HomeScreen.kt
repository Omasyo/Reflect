package com.omaka.reflect.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigate: () -> Unit
) {
    Scaffold(
//        Modifier.windowInsetsPadding(WindowInsets.ime),
        containerColor = MaterialTheme.colorScheme.surface,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /*TODO*/ },
            ) {

            }
        }
    ) { innerPadding ->


        HomeLayout(
            topbar = {
                Box(
                    Modifier
                        .defaultMinSize(
                            minHeight = 48f.dp
                        )
                        .fillMaxWidth()

                ) {
                    Text(
                        text = "Reflect",
                        Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            },
            searchBar = {

                Row(
                    Modifier
                        .padding(16f.dp)
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .fillMaxWidth()

                ) {
                    val focusManager = LocalFocusManager.current
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChanged,
                        Modifier
                            .padding(16f.dp)
                            .onFocusChanged(onFocusChanged)
                    )
                }
            },
        ) {

//            Column(
//                Modifier
//                    .fillMaxSize().background(Color.Cyan), verticalArrangement = Arrangement.Center) {
//                repeat(4) {
//                    Box(
//                        Modifier
//                            .fillMaxWidth()
//                            .background(
//                                Color(0xFF000000 + Random.nextLong(0xFFFFFF))
//                            )
//                    ) {
//                        Text(text = it.toString(), fontSize = 30.sp)
//                    }
//                }
//            }

            LazyColumn(
                Modifier.padding(innerPadding).background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(vertical = 16f.dp)
            ) {
                items(20) {
                    Box(
                        Modifier
                            .padding(horizontal = 16f.dp, vertical = 8f.dp)
                            .fillMaxWidth()
                            .height(100.dp)
                            .clip(MaterialTheme.shapes.large)
                            .background(
                                Color(0xFF000000 + Random.nextLong(0xFFFFFF))
                            )

                    )
                }

            }
        }

    }

}

@Composable
fun HomeLayout(
    topbar: @Composable () -> Unit,
    searchBar: @Composable SearchBarScope.() -> Unit,
    content: @Composable () -> Unit,
) {
    val searchBarScope = remember { SearchBarScope() }
    SubcomposeLayout { constraints ->


        val topBarPlaceables = subcompose(SlotsEnum.TopBar, topbar).map { measurable ->
            measurable.measure(constraints)
        }
        val topBarHeight = if (searchBarScope.isFocused) 0 else topBarPlaceables.maxOf { it.height }

        val searchBarPlaceables =
            subcompose(SlotsEnum.SearchBar, { searchBar(searchBarScope) }).map { measurable ->
                measurable.measure(constraints)
            }
        val searchBarHeight = searchBarPlaceables.maxOf { it.height }


        val contentConstraints =
            constraints.copy(maxHeight = constraints.maxHeight - searchBarHeight - topBarHeight)
        Log.d("TAG", "HomeLayout: TopBarHeight is $topBarHeight")

        val contentPlaceables = subcompose(SlotsEnum.Content, content).map { measurable ->
            measurable.measure(contentConstraints)
        }


        layout(constraints.maxWidth, constraints.maxHeight) {
            Log.d("TAG", "HomeLayout: searchbar focused ${searchBarScope.isFocused}")
            if (!searchBarScope.isFocused) {
                topBarPlaceables.forEach { it.placeRelative(0, 0) }
            }

            searchBarPlaceables.forEach { it.placeRelative(0, topBarHeight) }
            contentPlaceables.forEach { it.placeRelative(0, topBarHeight + searchBarHeight) }

        }

    }

}

class SearchBarScope {
    private var _value by mutableStateOf("")
    val value get() = _value

    private var focused by mutableStateOf(false)
    val isFocused get() = focused

    val onValueChanged: (String) -> Unit = { _value = it }
    val onFocusChanged: (FocusState) -> Unit = { focused = it.isFocused }

}

private enum class SlotsEnum { TopBar, SearchBar, Content, Fab }