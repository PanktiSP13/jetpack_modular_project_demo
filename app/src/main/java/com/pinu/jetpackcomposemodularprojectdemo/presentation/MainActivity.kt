package com.pinu.jetpackcomposemodularprojectdemo.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pinu.domain.entities.events.BooksEvents
import com.pinu.domain.entities.viewmodels.BooksViewModel
import com.pinu.domain.entities.viewmodels.CartViewModel
import com.pinu.domain.entities.viewmodels.FavouriteViewModel
import com.pinu.jetpackcomposemodularprojectdemo.navigation.NavArguments
import com.pinu.jetpackcomposemodularprojectdemo.navigation.NavigationRoutes
import com.pinu.jetpackcomposemodularprojectdemo.presentation.ui.books.screens.BookDetailRootUI
import com.pinu.jetpackcomposemodularprojectdemo.presentation.ui.books.screens.BookListRootUI
import com.pinu.jetpackcomposemodularprojectdemo.presentation.ui.screens.CartRootUI
import com.pinu.jetpackcomposemodularprojectdemo.presentation.ui.screens.DashboardRootUI
import com.pinu.jetpackcomposemodularprojectdemo.presentation.ui.screens.ProfileRootUI
import com.pinu.jetpackcomposemodularprojectdemo.presentation.ui.theme.JetpackComposeModularProjectDemoTheme

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    private val booksViewModel: BooksViewModel by viewModels()
    private val favouriteViewModel: FavouriteViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//       not required edge-to-edge
//        enableEdgeToEdge(navigationBarStyle = SystemBarStyle.dark(ContextCompat.getColor(this,R.color.black)))

        setContent {
            JetpackComposeModularProjectDemoTheme(darkTheme = false) {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = NavigationRoutes.DashboardScreen.route
                ) {

                    composable(route = NavigationRoutes.DashboardScreen.route) {
                        DashboardRootUI(navController, favouriteViewModel)
                    }

                    composable(route = NavigationRoutes.BookListScreen.route) {
                        BookListRootUI(navController, booksViewModel, favouriteViewModel)
                    }

                    composable(route = NavigationRoutes.BookDetailScreen.route,
//                        arguments = listOf(
//                            navArgument(NavArguments.BOOK_ID) {
//                                type = NavType.IntType
//                            }
//                        )
                    ) {
//                        val bookId = it.arguments?.getInt(NavArguments.BOOK_ID)
//                        if (bookId != null) {
//                            booksViewModel.onEvent(BooksEvents.FetchBookDetails(bookId))
//                        }

                        BookDetailRootUI(navController, booksViewModel, favouriteViewModel)
                    }

                    composable(route = NavigationRoutes.CartScreen.route) {
                        val cartViewModel : CartViewModel by viewModels()
                        CartRootUI(navController,cartViewModel)
                    }

                    composable(route = NavigationRoutes.ProfileScreen.route) {
                        ProfileRootUI(navController)
                    }


                }


            }

        }
    }
}
