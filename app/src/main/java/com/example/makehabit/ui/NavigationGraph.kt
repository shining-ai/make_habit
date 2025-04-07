package com.example.makehabit.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.makehabit.ui.AchieveScreen
import com.example.makehabit.ui.HistoryScreen
import com.example.makehabit.ui.MainScreenContent
import com.example.makehabit.ui.components.BottomBar
import com.example.makehabit.viewmodel.HabitViewModel

@Composable
fun NavigationGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val viewModel: HabitViewModel = viewModel()

    Scaffold(
        bottomBar = {
            BottomBar(navController, currentRoute)
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { MainScreenContent(viewModel = viewModel) }
            composable("history") { HistoryScreen(viewModel = viewModel) }
            composable("achieve") { AchieveScreen(viewModel = viewModel) }
        }
    }
}
