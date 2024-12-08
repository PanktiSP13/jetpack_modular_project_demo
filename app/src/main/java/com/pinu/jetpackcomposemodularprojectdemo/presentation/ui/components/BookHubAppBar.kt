package com.pinu.jetpackcomposemodularprojectdemo.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.pinu.domain.entities.events.CartEvents
import com.pinu.domain.entities.events.FavouritesEvents
import com.pinu.domain.entities.viewmodels.CartViewModel
import com.pinu.domain.entities.viewmodels.FavouriteViewModel
import com.pinu.jetpackcomposemodularprojectdemo.R
import com.pinu.jetpackcomposemodularprojectdemo.navigation.NavigationRoutes
import com.pinu.jetpackcomposemodularprojectdemo.presentation.ui.theme.BookHubTypography
import com.pinu.jetpackcomposemodularprojectdemo.presentation.ui.theme.OnPrimaryColor
import com.pinu.jetpackcomposemodularprojectdemo.presentation.ui.theme.Pink
import com.pinu.jetpackcomposemodularprojectdemo.presentation.ui.theme.PrimaryColor
import com.pinu.jetpackcomposemodularprojectdemo.presentation.ui.util.showCustomToast

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun BookHubAppBar(
    title: String = "Dashboard",
    canGoBack: Boolean = false,
    isCartVisible: Boolean = false,
    isFavouritesVisible: Boolean = false,
    isProfileOptionAvailable: Boolean = false,
    navController: NavController = rememberNavController(),
    favouriteViewModel: FavouriteViewModel = hiltViewModel<FavouriteViewModel>(),
    cartViewModel: CartViewModel = hiltViewModel<CartViewModel>(),
    onCartClick: () -> Unit = {},
    onBackPressed: () -> Unit = {},
) {

    val showFavourites = rememberSaveable { mutableStateOf(false) }
    val cartState = cartViewModel.cartState.collectAsState().value
    val favouriteState = favouriteViewModel.favouriteState.collectAsState().value

    val context = LocalContext.current

    LaunchedEffect(favouriteState.toastMessage) {
        favouriteState.toastMessage.message.takeIf { it.isNotEmpty() }?.let {
            showCustomToast(context = context, toastMessage = favouriteState.toastMessage)
            favouriteViewModel.onEvent(FavouritesEvents.ClearToastMessage)
        }
    }

    LaunchedEffect(cartState.toastMessage) {
        cartState.toastMessage.message.takeIf { it.isNotEmpty() }?.let {
            showCustomToast(context = context, toastMessage = cartState.toastMessage)
            cartViewModel.onEvent(CartEvents.ClearToastMessage)
        }
    }

    LaunchedEffect(key1 = cartState.itemMovedToCart) {
        cartState.itemMovedToCart.takeIf { it }?.let {
            showFavourites.value = false
            navController.navigate(route = NavigationRoutes.CartScreen.route)
            cartViewModel.onEvent(CartEvents.ValueUpdateItemMovedToCart)
        }
    }

    Surface(color = Pink) {
        TopAppBar(
            title = {
                Text(
                    text = title,
                    style = BookHubTypography.titleLarge.copy(
                        fontWeight = FontWeight.Normal, color = OnPrimaryColor),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryColor ),
            navigationIcon = {
                if (canGoBack) {
                    Image(
                        painter = painterResource(id = R.drawable.back_arrow),
                        contentDescription = stringResource(R.string.back),
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .size(25.dp)
                            .indication(indication = ripple(bounded = false),
                                interactionSource = remember {
                                    MutableInteractionSource()
                                })
                            .clickable {
                                navController.popBackStack()
                                onBackPressed()
                            },
                        colorFilter = ColorFilter.tint(color = OnPrimaryColor)
                    )
                } else {

                    if (isProfileOptionAvailable){
                        IconButton(onClick = {
                            navController.navigate(NavigationRoutes.ProfileScreen.route)
                        }) {
                            Icon(imageVector = Icons.Default.Person,
                                contentDescription ="profile", tint = OnPrimaryColor )
                        }
                    }
                }
            },
            actions = {
                if (isFavouritesVisible){
                    IconButton(onClick = { showFavourites.value = true }) {
                        Image(
                            painter = painterResource(id = R.drawable.favourite_checked),
                            modifier = Modifier.size(25.dp),
                            contentDescription = stringResource(R.string.favourites)
                        )
                    }
                }

                if (isCartVisible){
                    IconButton(onClick = {
                        navController.navigate(NavigationRoutes.CartScreen.route)
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.cart),
                            modifier = Modifier.size(25.dp),
                            contentDescription = stringResource(R.string.cart),
                            colorFilter = ColorFilter.tint(color = Color.White)
                        )
                    }
                }
            },
        )
    }

    if (showFavourites.value){
        FavouritesBottomSheet(
            favouriteState = favouriteState,
            onEvents = { event ->
                when (event) {
                    is FavouritesEvents.NavigateToBookDetailScreen -> {
                        navController.navigate(NavigationRoutes.BookDetailScreen.getBookDetail(event.bookID))
                        showFavourites.value = false
                    }
                    is FavouritesEvents.NavigateToCartScreen -> {
                        navController.navigate(NavigationRoutes.CartScreen.route)
                        showFavourites.value = false
                    }

                    else -> {}
                }
                favouriteViewModel.onEvent(event)
            },
            onCartEvents = { cartEvent ->
                cartViewModel.onEvent(cartEvent)
            },
            onDismiss = {
            showFavourites.value = false
        })
    }
}

