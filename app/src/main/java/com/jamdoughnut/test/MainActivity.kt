package com.jamdoughnut.test

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jamdoughnut.test.catalog.ProductCatalogScreen
import com.jamdoughnut.test.catalog.ProductCatalogViewModel
import com.jamdoughnut.test.product_details.DetailsScreen
import com.jamdoughnut.test.product_details.ProductDetailsViewModel
import com.jamdoughnut.test.ui.theme.JamDoughnutTestTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

//    // TODO Make DI using Dagger2
//    private val viewModel: ProductCatalogViewModel by lazy {
//        CatalogModule.inject()
//    }
//
//    private val viewModelA: ProductDetailsViewModel by lazy {
//        CatalogModule.injectA()
//    }

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

//                LaunchedEffect(Unit) {
//                    navController.addOnDestinationChangedListener { _, destination, _ ->
//                        if (destination.route == "main") {
//                            viewModel.onRefresh() // Update when the user returned
//                        }
//                    }
//                }
            }
        }
    }

}

