package com.omaka.reflect.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun NoteCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    onTap: () -> Unit
) {
    Box(
        modifier
            .fillMaxWidth()
            .height(240.dp)
            .clip(MaterialTheme.shapes.large)
            .background(
                MaterialTheme.colorScheme.inverseOnSurface
            )
            .clickable(onClick = onTap)
//            .ges

    ) {
        Column(Modifier.padding(16f.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)

            Text(
                subtitle,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}