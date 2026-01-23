package com.yintianyan.tallybook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yintianyan.tallybook.model.database.TallyBookDatabase
import com.yintianyan.tallybook.routes.NavRoutes
import com.yintianyan.tallybook.components.AppBottomNavigation
import com.yintianyan.tallybook.screens.home.HomeScreen
import com.yintianyan.tallybook.screens.profile.ProfileScreen
import com.yintianyan.tallybook.screens.statistics.StatisticsScreen
import com.yintianyan.tallybook.theme.TallyBookTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // 在应用启动时插入示例数据
        // lifecycleScope.launch {
        //     val database = TallyBookDatabase.getDatabase(applicationContext)
        //     TallyBookDatabase.insertSampleData(database)
        // }
        
        setContent {
            TallyBookTheme {
                AppNavHost()
            }
        }
    }
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: NavRoutes.HOME

    Scaffold(
        bottomBar = {
            AppBottomNavigation(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = NavRoutes.HOME,
            modifier = Modifier.padding(it)
        ) {
            composable(NavRoutes.HOME) {
                HomeScreen()
            }
            composable(NavRoutes.STATISTICS) {
                StatisticsScreen()
            }
            composable(NavRoutes.PROFILE) {
                ProfileScreen()
            }
        }
    }
}

@Composable
fun MainScreen(navController: NavHostController) {
    // This composable can be used if needed for additional main screen content
}