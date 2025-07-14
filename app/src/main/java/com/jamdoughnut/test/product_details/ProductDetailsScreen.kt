package com.jamdoughnut.test.product_details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.jamdoughnut.test.R
import com.jamdoughnut.test.catalog.data.dto.ProductDto
import com.jamdoughnut.test.ui.theme.Typography
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Currency

@Composable
fun DetailsScreen(
    itemId: Int,
    navController: NavController?,
    detailsViewModel: ProductDetailsViewModel = hiltViewModel()
) {

    val state by detailsViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(itemId) {
        detailsViewModel.getProductDetailsById(itemId)
    }

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        topBar = { DetailsTopBar(navController, state.result?.name ?: "") },
        snackbarHost = {
            SnackbarHost(hostState = scaffoldState.snackbarHostState)
        },
        content = { padding ->
            DetailsContent(
                modifier = Modifier.padding(padding),
                state = state,
                viewModel = detailsViewModel,
                scaffoldState
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailsTopBar(
    navController: NavController?,
    header: String
) {
    TopAppBar(
        title = { Text(header) },
        navigationIcon = {
            IconButton(onClick = { navController?.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = stringResource(R.string.navigation_back)
                )
            }
        }
    )
}

@Composable
private fun DetailsContent(
    modifier: Modifier = Modifier,
    state: DetailsState,
    viewModel: ProductDetailsViewModel,
    scaffoldState:ScaffoldState
) {

    val scope = rememberCoroutineScope()
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        when {
            state.isRefreshing -> LoadingIndicator()
            state.isError -> ErrorPlaceholder()
            else -> state.result?.let {
                ProductContent(state.result, viewModel) { quantity, totalPrice ->
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message  = "You clicked Payment button!",
                            actionLabel = "Ok"
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductContent(
    product: ProductDto,
    viewModel: ProductDetailsViewModel,
    onBuyClick: (Int, Int) -> Unit
) {

    val quantity by viewModel.quantity.collectAsStateWithLifecycle()
    val totalPrice by viewModel.price.collectAsStateWithLifecycle()

    val currencyCode = stringResource(R.string.currency_gbp)
    val formattedPrice = remember(totalPrice, currencyCode) {
        NumberFormat.getCurrencyInstance().apply {
            currency = Currency.getInstance(currencyCode)
        }.format(totalPrice)
    }

    Scaffold(
        bottomBar = {
            Button(
                onClick = { onBuyClick(quantity, totalPrice) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = "Buy for $formattedPrice",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {

            ProductImageCarousel(
                images = product.leadImageUrls,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )

            ProductInfoSection(
                product = product,
                quantity = quantity,
                onQuantityChange = { newQuantity ->
                    viewModel.setQuantity(newQuantity)
                    viewModel.updatePrice(product.price)
                },
                modifier = Modifier.padding(16.dp)
            )
        }
    }

}

@Composable
private fun ProductInfoSection(
    product: ProductDto,
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = product.name,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        CashBackText(product.cashbackPercentage ?: 0)
        PriceText(product.price)

        Spacer(modifier = Modifier.height(24.dp))

        StyledQuantitySelector(
            currentQuantity = quantity,
            onQuantityChange = onQuantityChange,
            minQuantity = 1,
            maxQuantity = 10,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
private fun LoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
private fun ErrorPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(stringResource(R.string.details_error))
    }
}

@Composable
private fun ProductImageCarousel(
    images: List<String>,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        ImageCarouselWithPager(images)
    }
}


@Composable
fun ImageCarouselWithPager(images: List<String>) {
    val pagerState = rememberPagerState(pageCount = { images.size })

    Column {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.height(250.dp)
        ) { page ->
            AsyncImage(
                model = images[page],
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(images.size) { index ->
                val color = if (pagerState.currentPage == index) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                }

                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(color)
                        .padding(2.dp)
                )
                if (index < images.size - 1) Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}

@Composable
fun StyledQuantitySelector(
    currentQuantity: Int,
    onQuantityChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    minQuantity: Int = 1,
    maxQuantity: Int = Int.MAX_VALUE
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                .clickable(
                    enabled = currentQuantity > minQuantity,
                    onClick = { onQuantityChange(currentQuantity - 1) }
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "Decrease",
                tint = if (currentQuantity > minQuantity) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                }
            )
        }

        Text(
            text = currentQuantity.toString(),
            modifier = Modifier
                .width(48.dp)
                .padding(horizontal = 8.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium
        )

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                .clickable(
                    enabled = currentQuantity < maxQuantity,
                    onClick = { onQuantityChange(currentQuantity + 1) }
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Increase",
                tint = if (currentQuantity < maxQuantity) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                }
            )
        }
    }
}

@Composable
fun CashBackText(cashBack: Int) {

    Text(
        text = stringResource(
            R.string.cashback_text,
            cashBack.toString()
        ),
        style = MaterialTheme.typography.bodySmall.copy(
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
    )
}

@Composable
fun PriceText(
    price: Int,
    currencyCode: String = stringResource(R.string.currency_gbp),
    style: TextStyle = Typography.bodyLarge
) {
    val format = remember(currencyCode) {
        NumberFormat.getCurrencyInstance().apply {
            currency = Currency.getInstance(currencyCode)
        }
    }
    Text(
        text = format.format(price),
        style = style
    )
}