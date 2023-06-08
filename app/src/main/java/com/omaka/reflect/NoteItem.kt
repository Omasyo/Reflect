package com.omaka.reflect

import android.icu.util.Calendar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date

sealed class NoteItem {
    abstract val id: String
    abstract val created: Date
    abstract val modified: Date
}

data class TextItem(
    val content: String,
    override val id: String,
    override val created: Date,
    override val modified: Date,
) : NoteItem()

data class ImageItem(
    val path: String,
    val caption: String,
    override val id: String,
    override val created: Date,
    override val modified: Date,
) : NoteItem()

data class AudioItem(
    val path: String,
    override val id: String,
    override val created: Date,
    override val modified: Date,
) : NoteItem()

//TODO: add collage item