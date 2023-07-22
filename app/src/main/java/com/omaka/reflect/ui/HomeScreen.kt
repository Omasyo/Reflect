package com.omaka.reflect.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToSettings: () -> Unit,
    onNavigateToNote: () -> Unit,
) {

    Scaffold(
        Modifier
            .imePadding(),
        topBar = {
            Box(
                Modifier
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .statusBarsPadding()
                    .defaultMinSize(
                        minHeight = 56f.dp
                    )
                    .fillMaxWidth()

            ) {
                Text(
                    text = "Reflect",
                    Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(
                    onClick = onNavigateToSettings,
                    Modifier.align(Alignment.CenterEnd),
                ) {
                    Icon(Icons.Outlined.Settings, null)
                }
                Divider(
                    Modifier
                        .padding(horizontal = 16f.dp)
                        .align(Alignment.BottomCenter), 0.5f.dp
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToNote) {

            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
    ) { innerPadding ->
        var query by remember {
            mutableStateOf("")
        }



        Box(
            Modifier
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(innerPadding)
        ) {


            LazyVerticalStaggeredGrid(
                StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 16f.dp,
                horizontalArrangement = Arrangement.spacedBy(16f.dp),
                contentPadding = PaddingValues(16.dp),
            ) {
                items(20) {
                    NoteCard(
                        title = "This is a long title",
                        subtitle = """It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like""",
onTap = onNavigateToNote
                    )
                }

            }
        }
    }
}
