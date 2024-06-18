package com.balaban.slider.ui.mainslider

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.balaban.slider.model.SliderItem

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SliderItem(
    sliderItem: SliderItem,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    modifier: Modifier = Modifier
) {

    val text by remember {
        mutableStateOf(("Günbatımında Huzurun Rengi\n" +
                "\n" +
                "Bu nefes kesen günbatımı manzarası, doğanın büyüleyici gücünü ve huzur veren atmosferini yansıtıyor. Sonsuz gökyüzü ve sakin deniz, günün son ışıklarıyla birleşerek unutulmaz bir an yaratıyor. Doğanın bu büyüsü karşısında kendimi çok şanslı hissediyorum. \uD83C\uDF05✨"))
    }
    with(sharedTransitionScope) {
        Box(
            modifier = modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .sharedElement(
                        rememberSharedContentState(key = "image" + "-" + sliderItem.imageUrl),
                        animatedVisibilityScope = animatedContentScope,
                    ),
                model = sliderItem.imageUrl,
                contentDescription = "Translated description of what the image contains",
                contentScale = ContentScale.FillBounds
            )

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(modifier = modifier
                    .sharedElement(
                        rememberSharedContentState(key = "title" + "-" + sliderItem.imageUrl),
                        animatedVisibilityScope = animatedContentScope,
                    ),
                    contentAlignment = Alignment.TopCenter){
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp)
                            .alpha(0.5f)
                            .background(color = Color(0xFFB5CCFA))
                    )

                    Text(
                        text = sliderItem.imageName.toString(),
                        fontSize = 24.sp,
                        color = Color.Black
                    )
                }

                Box(modifier = modifier
                    .sharedElement(
                        rememberSharedContentState(key = "subTitle" + "-" + sliderItem.imageUrl),
                        animatedVisibilityScope = animatedContentScope,
                    ),
                    contentAlignment = Alignment.BottomCenter){
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .alpha(0.5f)
                            .background(color = Color(0xFF3E3F41))
                    )

                    Text(
                        modifier = Modifier.padding(horizontal = 6.dp),
                        text = text,
                        fontSize = 10.sp,
                        color = Color.White,
                        lineHeight = 12.sp
                    )
                }

            }

        }
    }

}