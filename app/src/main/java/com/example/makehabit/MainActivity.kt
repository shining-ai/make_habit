package com.example.makehabit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.makehabit.ui.theme.MakeHabitTheme
import androidx.navigation.compose.rememberNavController
import com.example.makehabit.ui.NavigationGraph

enum class HabitScreen(){
    Home,
    History
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MakeHabitTheme {
                val navController = rememberNavController()
                NavigationGraph(navController)
            }
        }
    }
}





