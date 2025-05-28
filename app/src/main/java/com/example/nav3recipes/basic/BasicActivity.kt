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

package com.example.nav3recipes.basic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.nav3recipes.content.ContentBlue
import com.example.nav3recipes.content.ContentGreen

/**
 * Basic example with two screens, showing how to use the Navigation 3 API.
 */

data object RouteA

data class RouteB(val id: String)

// This is the main Activity that hosts our Navigation 3 setup.
class BasicActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            // `backStack` is a MutableStateList that represents the current navigation stack.
            // Each element in the list is a "route", which can be any data type.
            // Here we initialize it with `RouteA` as the starting destination.
            val backStack = remember { mutableStateListOf<Any>(RouteA) }

            // `NavDisplay` is the core composable for displaying content based on the navigation state.
            // It takes the `backStack`, an `onBack` lambda, and an `entryProvider` lambda.
            NavDisplay(
                // The current navigation back stack. NavDisplay renders the last entry in the backStack.
                backStack = backStack,
                // `onBack` is a lambda that is called when the user presses the back button.
                // Here, we remove the last element from the backStack, effectively navigating back.
                onBack = { backStack.removeLastOrNull() },
                // `entryProvider` is a lambda that maps a route key (from the backStack) to a `NavEntry`.
                // A `NavEntry` contains the Composable content to display for that route.
                entryProvider = { key ->
                    when (key) {
                        // If the key is RouteA, create a NavEntry with Green content.
                        is RouteA -> NavEntry(key) {
                            ContentGreen("Welcome to Nav3") {
                                // A button that adds RouteB with an ID to the backStack when clicked, navigating to RouteB.
                                Button(onClick = {
                                    backStack.add(RouteB("123"))
                                }) {
                                    Text("Click to navigate")
                                }
                            }
                        } // NavEntry for RouteA

                        // If the key is RouteB, create a NavEntry with Blue content, displaying the ID.
                        is RouteB -> NavEntry(key) {
                            ContentBlue("Route id: ${key.id} ")
                        } // NavEntry for RouteB

                        else -> {
                            error("Unknown route: $key")
                        }
                    }
                }
            )
        }
    }
}
