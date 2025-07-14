package com.jamdoughnut.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jamdoughnut.test.catalog.presentation.ProductCatalogScreen
import com.jamdoughnut.test.product_details.presentation.DetailsScreen
import com.jamdoughnut.test.ui.theme.JamDoughnutTestTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JamDoughnutTestTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "main") {
                    composable("main") {
                        ProductCatalogScreen(
                            onItemClick = { id ->
                                navController.navigate("details/$id")
                            }
                        )
                    }
                    composable("details/{itemId}",
                        arguments = listOf(navArgument("itemId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val itemId = backStackEntry.arguments?.getInt("itemId") ?: 0
                          DetailsScreen(itemId, navController)
                    }
                }
            }
        }
    }

}

