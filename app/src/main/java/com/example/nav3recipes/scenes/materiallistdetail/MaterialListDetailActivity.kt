package com.example.nav3recipes.scenes.materiallistdetail

/*
 * Copyright 2025 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.ui.Alignment
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.nav3recipes.content.ContentBlue
import com.example.nav3recipes.content.ContentGreen
import com.example.nav3recipes.content.ContentRed
import com.example.nav3recipes.content.ContentYellow
import com.example.nav3recipes.ui.setEdgeToEdgeConfig
import kotlinx.serialization.Serializable

/**
 * This example uses the Material ListDetailSceneStrategy to create an adaptive scene. It has three
 * destinations: ConversationList, ConversationDetail and Profile. When the window width allows it,
 * the content for these destinations will be shown in a two pane layout.
 */
@Serializable
private object ConversationList : NavKey

@Serializable
private data class ConversationDetail(val id: String) : NavKey

@Serializable
private data object Profile : NavKey

class MaterialListDetailActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    /**
     * This activity demonstrates the use of Navigation 3 with Material Design 3 adaptive layouts.
     * It utilizes `ListDetailSceneStrategy` to create a two-pane layout for list-detail flows
     * when the window size is sufficient. The navigation is handled by `NavDisplay`, which
     * manages the back stack and displays content based on the current `NavKey`.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        setEdgeToEdgeConfig()
        super.onCreate(savedInstanceState)

        setContent {

            val backStack = rememberNavBackStack(ConversationList)
            // `rememberNavBackStack` creates and remembers a mutable back stack for navigation.
            // It is initialized with the starting destination `ConversationList`.

            val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>()
            // `rememberListDetailSceneStrategy` provides a strategy for arranging destinations
            // within a list-detail layout, adapting to different window sizes.


            NavDisplay(
                backStack = backStack,
                // `NavDisplay` is the main composable for handling Navigation 3.
                // It takes the `backStack` to determine the current destination.

                onBack = { keysToRemove -> repeat(keysToRemove) { backStack.removeLastOrNull() } },
                sceneStrategy = listDetailStrategy,
                entryProvider = entryProvider {
                    entry<ConversationList>(
                        metadata = ListDetailSceneStrategy.listPane(
                            detailPlaceholder = {
                                ContentYellow("Choose a conversation from the list")
                            }
                        )
                    ) {
                        ContentRed("Welcome to Nav3") {
                            // `entryProvider` defines the content for each destination (`NavKey`).
                            // `entry<NavKey>` associates a Composable content with a specific `NavKey`.
                            // Here, `entry<ConversationList>` defines the content for the ConversationList destination.
                            // `metadata = ListDetailSceneStrategy.listPane(...)` indicates this content should be displayed in the list pane.
                            Button(onClick = {
                                backStack.add(ConversationDetail("ABC"))
                            }) {
                                Text("View conversation")
                            }
                        }
                    }
                    entry<ConversationDetail>(
                        metadata = ListDetailSceneStrategy.detailPane()
                    ) { conversation ->
                        // `entry<ConversationDetail>` defines the content for the ConversationDetail destination.
                        // `metadata = ListDetailSceneStrategy.detailPane()` indicates this content should be displayed in the detail pane.
                        ContentBlue("Conversation ${conversation.id} ") {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Button(onClick = {
                                    backStack.add(Profile)
                                }) {
                                    Text("View profile")
                                }
                            }
                        }
                    }
                    entry<Profile>(
                        metadata = ListDetailSceneStrategy.extraPane()
                        // `entry<Profile>` defines the content for the Profile destination.
                        // `metadata = ListDetailSceneStrategy.extraPane()` indicates this content should be displayed in an extra pane (if available).
                    ) {
                        ContentGreen("Profile")
                    }
                }
            )
        }
    }
}
