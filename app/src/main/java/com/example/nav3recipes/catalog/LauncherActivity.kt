package com.example.nav3recipes.catalog

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview

class LauncherActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ActivityListView()
                }
            }
        }
    }
}

data class ActivityInfo(val name: String, val label: String, val className: String)

@Composable
fun ActivityListView() {
    val context = LocalContext.current
    val activities = remember {
        val packageManager = context.packageManager
        val packageName = context.packageName
        val componentName = ComponentName(context, LauncherActivity::class.java)
        packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            .activities
            .filter { it.name != componentName.className }
            .mapNotNull { activityInfo ->
                val activityLabel = activityInfo.loadLabel(packageManager).toString()
                val activityName = activityInfo.name.substringAfterLast('.')
                ActivityInfo(
                    name = activityName,
                    label = if (activityLabel.isNotEmpty() && activityLabel != activityInfo.name) activityLabel else activityName,
                    className = activityInfo.name
                )
            }
            .sortedBy { it.label }
    }

    LazyColumn {
        items(activities) { activity ->
            ListItem(
                headlineContent = { Text(activity.label) },
                modifier = Modifier.clickable {
                    val intent = Intent().setClassName(context.packageName, activity.className)
                    context.startActivity(intent)
                }
            )
            Divider()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MaterialTheme {
        ActivityListView()
    }
}
