package com.omaka.reflect.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.icu.util.Calendar
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.omaka.reflect.NoteItem
import com.omaka.reflect.TextItem
import com.omaka.reflect.ui.theme.ReflectTheme

@Composable
fun NoteScreen(onPop: () -> Unit) {
    val pop = {
        onPop()
    }
    Scaffold(
        Modifier
            .imePadding()
            .navigationBarsPadding(),
        containerColor = MaterialTheme.colorScheme.inverseOnSurface,
        topBar = {
            Box(
                Modifier
                    .statusBarsPadding()
                    .defaultMinSize(
                        minHeight = 56f.dp
                    )
                    .fillMaxWidth()

            ) {
                IconButton(
                    onClick = pop,
                    Modifier.align(Alignment.CenterStart),
                ) {
                    Icon(Icons.Outlined.ArrowBack, null)
                }
                Row(
                    Modifier.align(Alignment.CenterEnd)
                ) {
                    IconButton(
                        onClick = {},
                    ) {
                        Icon(Icons.Outlined.Settings, null)
                    }
                }
                Divider(
                    Modifier
                        .padding(horizontal = 16f.dp)
                        .align(Alignment.BottomCenter), 0.5f.dp
                )
            }
        }
    ) { innerPadding ->
        var titleText by remember { mutableStateOf("") }
        var testText by remember { mutableStateOf(test) }

        var noteItems = remember {
            mutableStateListOf<NoteItem>(
                TextItem(
                    "Hello",
                    id = "",
                    created = Calendar.getInstance().time,
                    modified = Calendar.getInstance().time
                ),
            )
        }

        Column(Modifier.padding(innerPadding)) {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentPadding = PaddingValues(16f.dp)
            ) {

                item {
                    TextField(
                        value = titleText,
                        onValueChange = {
                            titleText = it
                        },
                        hintText = "Title",
                        textStyle = MaterialTheme.typography.headlineSmall,
                    )
                }
                items(noteItems) { noteItem ->
                    NoteItemContent(
                        noteItem = noteItem,
                        onSubmit = {
                            noteItems.add(
                                TextItem(
                                    "",
                                    id = "",
                                    created = Calendar.getInstance().time,
                                    modified = Calendar.getInstance().time
                                )
                            )
                        }
                    )
                }
                item {
                    TextField(
                        modifier = Modifier.padding(top = 16f.dp),
                        value = testText,
                        onValueChange = {
                            testText = it
                        },
                        hintText = "Subs",
                        textStyle = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
            BottomBar()
        }

        BackHandler(onBack = onPop)
    }
}

@Composable
fun NoteItemContent(
    modifier: Modifier = Modifier,
    noteItem: NoteItem,
    onSubmit:  () -> Unit,
) {
    Box(modifier.background(Color.Yellow)) {
        when (noteItem) {
            is TextItem -> {
                var testText by remember {
                    mutableStateOf(noteItem.content)
                }
                TextField(
                    value = testText,
                    onValueChange = {
                        testText = it
                    },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    hintText = "Test",
                    onSubmit = onSubmit
                )
            }

            else -> {
                TODO()
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BottomBar() {
    AnimatedVisibility(
        visible = WindowInsets.isImeVisible,
        enter = slideIn(SpringSpec(stiffness = Spring.StiffnessHigh)) {
            IntOffset(
                0,
                it.height
            )
        },
        exit = slideOut(SpringSpec(stiffness = Spring.StiffnessHigh)) {
            IntOffset(
                0,
                it.height
            )
        }
    ) {

        Box {
            Divider(
                Modifier
                    .padding(horizontal = 16f.dp)
                    .align(Alignment.TopCenter), 0.5f.dp
            )
            Row(
                Modifier
//                            .background(MaterialTheme.colorScheme.secondaryContainer)
                    .fillMaxWidth()
                    .padding(horizontal = 16f.dp, vertical = 8f.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Outlined.Phone, null)
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Outlined.Phone, null)
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Outlined.Phone, null)
                }
            }
        }
    }
}

@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showSystemUi = true)
@Composable
fun Preview() {
    ReflectTheme {
        NoteScreen({})
    }
}

const val test =
    """Craig Jones, a recently fired and unemployed slacker, living in South Central Los Angeles, spends Friday with his best friend Smokey, a small-time drug peddler. The pair smoke a brokered consignment of marijuana, which Smokey was tasked to sell for Smokey's drug supplier Big Worm.

Big Worm attempts to collect his money from Smokey, who accidentally involves Craig, subjecting both to Big Worm's ultimatum: if they don't repay him ${'$'}200 by 10:00 PM that evening, Craig and Smokey will be killed.

Craig attempts to borrow money from a number of people, including his mother Betty, his sister Dana, and jealous girlfriend Joi, whom the latter refuses under the assumption that Craig is being unfaithful with local drug addict and mooch Felisha and her sister Debbie. Craig retrieves a gun to walk Smokey home, but his father Willie tells him that he needs to use his fists instead of weapons to help himself.

Smokey sells some drugs to Hector, a former smoking buddy, while Deebo, the neighborhood bully, forces Smokey to break into their neighbor Stanley's house to burglarize and they manage to steal ${'$'}200 that Deebo decides to keep for himself.

Smokey attempts to retrieve the money from Deebo, who is asleep with Felisha at her house, but fails due to interference from the petty thief Ezal. Seeing Deebo awake, Craig and Smokey notice a car driving slowly and, suspecting a drive-by shooting, they hide in Craig's room for the evening. After failing to contact Big Worm, and with 10:00 PM approaching, they return outside, but are forced to evade Big Worm's men as they're sitting in a black van with its headlights off, starting a shootout.

After the shootout, the neighbors come out of their houses upon hearing the gunshots. Debbie confronts Deebo for beating Felisha, assuming Felisha was behind Smokey's attempted theft. As Craig and Smokey arrive, Deebo angrily punches Debbie, knocking her to the ground, leading to a physical altercation between him and Craig, with Craig being victorious. Smokey steals the ${'$'}200 from the incapacitated Deebo.

Other locals, such as Red and Ezal, retrieve their stolen items. Debbie tends to Craig's wounds, leading him to break up with Joi on the phone while his father informs him his former supervisor called wanting to see him tomorrow.

Smokey settles his debt with Big Worm, telling him he will no longer sell drugs and is set to enter rehabilitation. Smokey then smokes a joint and ends the movie by looking at the camera and saying "I was just bullshittin'! And you know this, man!""""