package com.balaban.slider.ui.mainslider

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.balaban.slider.LocalAnimatedContentScope
import com.balaban.slider.LocalSharedTransitionScope
import com.balaban.slider.lerpAlpha
import com.balaban.slider.lerpSize
import com.balaban.slider.model.SliderItem
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainScreen(
    onClickedSliderDetail: (sliderItem: SliderItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    val sharedTransitionScope = LocalSharedTransitionScope.current
    val animatedContentScope = LocalAnimatedContentScope.current

    val sliderList by remember {
        mutableStateOf(
            listOf(
                SliderItem(imageUrl = "https://cdn.pixabay.com/photo/2024/05/26/10/15/bird-8788491_1280.jpg", imageName = "Birds"),
                SliderItem(imageUrl = "https://cdn.pixabay.com/photo/2016/12/12/13/10/lake-louise-1901556_1280.jpg", imageName = "Lake louise"),
                SliderItem(imageUrl = "https://cdn.pixabay.com/photo/2024/05/23/13/23/moorente-8783210_1280.jpg", imageName = "moorente"),
                SliderItem(imageUrl = "https://cdn.pixabay.com/photo/2019/04/26/12/29/blue-4157419_1280.jpg", imageName = "forest"),
                SliderItem(imageUrl = "https://cdn.pixabay.com/photo/2019/10/14/03/26/landscape-4547734_1280.jpg", imageName = "landscape"),
                SliderItem(imageUrl = "https://cdn.pixabay.com/photo/2023/11/12/18/29/red-berries-8383886_1280.jpg",imageName =  "red berries"),
                SliderItem(imageUrl = "https://cdn.pixabay.com/photo/2020/08/22/17/51/boat-5509027_1280.jpg", imageName = "boat"),
            )
        )
    }

    val pagerState = rememberPagerState(
        initialPage = 3,
        initialPageOffsetFraction = 0f,
        pageCount = { sliderList.size }
    )

    val fling = PagerDefaults.flingBehavior(
        state = pagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(10)
    )

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        HorizontalPager(
            modifier = modifier.fillMaxHeight(0.5f),
            state = pagerState,
            flingBehavior = fling,
            contentPadding = PaddingValues(horizontal = 24.dp)
        ) { pageIndex ->
            Card(
                modifier
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
                            start = 0.5f,
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

                SliderItem(
                    sliderItem = sliderList[pageIndex],
                    sharedTransitionScope = sharedTransitionScope,
                    animatedContentScope = animatedContentScope
                )
            }
        }
        Row(
            modifier = modifier
                .padding(top = 12.dp)
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) Color.Red else Color.LightGray
                Box(
                    modifier = modifier
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