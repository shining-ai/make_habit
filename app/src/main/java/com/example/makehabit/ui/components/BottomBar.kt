package com.example.makehabit.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController


@Composable
fun BottomBar(navController: NavController, currentRoute: String?) {
    NavigationBar(modifier = Modifier.fillMaxWidth()) {
        val items = listOf(
            BottomNavItem("home", "ホーム"),
            BottomNavItem("history", "履歴"),
            BottomNavItem("achieve", "達成状況")
        )

        items.forEach { item ->
            NavigationBarItem(
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = { navController.navigate(item.route) },
                icon = {}
            )
        }
    }
}

data class BottomNavItem(val route: String, val label: String)