package com.example.nav3recipes.commonui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.nav3recipes.content.ContentGreen
import com.example.nav3recipes.content.ContentPurple
import com.example.nav3recipes.content.ContentRed
import com.example.nav3recipes.ui.theme.Nav3RecipesTheme

class Nav2CommonUiActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Nav3RecipesTheme {
                Nav2CommonUiScreen()
            }
        }
    }
}

sealed class Screen(val route: String, val icon: ImageVector) {
    object Home : Screen("home", Icons.Filled.Home)
    object ChatList : Screen("ChatList", Icons.Default.Face)
    object Camera : Screen("Camera", Icons.Default.PlayArrow)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Nav2CommonUiScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavigationItems = listOf(
        Screen.Home,
        Screen.ChatList,
        Screen.Camera
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavigationItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.route) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    // on the back stack as users select items
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    // Avoid building a large stack of destinations on the back stack as users select items
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { _ ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route
        ) {
            composable(Screen.Home.route) {
                ContentRed("Home screen")
            }
            composable(Screen.ChatList.route) {
                ContentGreen("Chat list screen"){
                    Button(onClick = {}) {
                        Text("Go to conversation")
                    }
                }
            }
            composable(Screen.Camera.route) {
                ContentPurple("Camera screen")
            }
        }
    }
}