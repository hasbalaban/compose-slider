package com.balaban.slider

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.balaban.slider.model.SliderItem
import com.balaban.slider.ui.theme.SliderTheme
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SliderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        MainNavigation()
                    }
                }
            }
        }
    }
}

@Composable
private fun MainNavigation(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Slider.route){

        composable(Screen.Slider.route){
            MainScreen(
                onClickedSliderDetail = {
                    navController.navigate(Screen.SliderItemDetail().createRoute(item = it))
                }
            )
        }

        composable(
            Screen.SliderItemDetail().route,
            arguments = listOf(
                navArgument("item") {
                    type = NavType.StringType
                    defaultValue = ""
                })
        ) {backStackEntry ->
            val itemDataString = backStackEntry.arguments?.getString("item")
            val itemData = Gson().fromJson(itemDataString, SliderItem::class.java)

            itemData?.let {
                ImageDetail(
                    sliderItem = it,
                    onBackClicked = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

@Composable
fun MainScreen(
    onClickedSliderDetail : (SliderItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    val sliderList by remember {
        mutableStateOf(
            listOf(
                SliderItem(imageUrl = "https://cdn.pixabay.com/photo/2024/05/26/10/15/bird-8788491_1280.jpg"),
                SliderItem("https://cdn.pixabay.com/photo/2016/12/12/13/10/lake-louise-1901556_1280.jpg"),
                SliderItem("https://cdn.pixabay.com/photo/2024/05/23/13/23/moorente-8783210_1280.jpg"),
                SliderItem("https://cdn.pixabay.com/photo/2019/04/26/12/29/blue-4157419_1280.jpg"),
                SliderItem("https://cdn.pixabay.com/photo/2019/10/14/03/26/landscape-4547734_1280.jpg"),
                SliderItem("https://cdn.pixabay.com/photo/2023/11/12/18/29/red-berries-8383886_1280.jpg"),
                SliderItem("https://cdn.pixabay.com/photo/2020/08/22/17/51/boat-5509027_1280.jpg"),
            )
        )
    }

    val pagerState = rememberPagerState(
        initialPage = 1,
        initialPageOffsetFraction = 0f,
        pageCount = { sliderList.size }
    )

    val fling = PagerDefaults.flingBehavior(
        state = pagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(10)
    )

    Column (
        modifier = Modifier.wrapContentSize(),
    ){
        HorizontalPager(
            modifier = Modifier
                .fillMaxHeight(0.4f),
            state = pagerState,
            flingBehavior = fling,
            contentPadding = PaddingValues(horizontal = 32.dp)
        ) {pageIndex ->

            Card(
                Modifier
                    .clickable {
                        onClickedSliderDetail.invoke(sliderList[pageIndex])
                    }
                    .padding(top = 48.dp, start = 6.dp, end = 6.dp)
                    .graphicsLayer {
                        val pageOffset = (
                                (pagerState.currentPage - pageIndex) + pagerState
                                    .currentPageOffsetFraction
                                ).absoluteValue

                        // We animate the alpha, between 50% and 100%
                        alpha = lerpAlpha(
                            start = 0.4f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )

                        scaleY = lerpSize(
                            start = 0.80f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    }
            ) {
                SliderItem(sliderItem = sliderList[pageIndex])
            }
        }

        Row(
            Modifier
                .padding(top = 12.dp)
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color.Red else Color.LightGray
                Box(
                    modifier = Modifier
                        .clickable {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(iteration)
                            }
                        }
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(16.dp)
                )
            }
        }
    }

}

@Composable
fun SliderItem(sliderItem : SliderItem, modifier: Modifier = Modifier) {

        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = sliderItem.imageUrl,
                contentDescription = "Translated description of what the image contains",
                contentScale = ContentScale.FillBounds
            )
        }
}

@Composable
private fun ImageDetail(
    sliderItem: SliderItem,
    onBackClicked : () -> Unit,
    modifier: Modifier = Modifier){
    Column(modifier = Modifier
        .wrapContentSize()
        .padding(start = 12.dp, end = 12.dp, top = 12.dp)) {
        Image(
            modifier = modifier
                .clickable {
                    onBackClicked.invoke()
                },
            painter = painterResource(id = R.drawable.go_back), contentDescription = "go back"
        )

        Column(
            modifier = Modifier.padding(top = 36.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = sliderItem.imageUrl,
                contentDescription = "Translated description of what the image contains",
                contentScale = ContentScale.FillBounds
            )

            Text(
                modifier = Modifier.padding(top = 24.dp),
                text = "Photo", fontSize = 24.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SliderTheme {
        MainScreen(
            onClickedSliderDetail = {}
        )
    }
}



fun lerpAlpha(start: Float, stop: Float, fraction: Float): Float {
    return start + fraction * (stop - start)
}
fun lerpSize(start: Float, stop: Float, fraction: Float): Float {
    return start + fraction * (stop - start)
}
