package com.balaban.slider.ui.sliderdetail

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.balaban.slider.LocalAnimatedContentScope
import com.balaban.slider.LocalSharedTransitionScope
import com.balaban.slider.R
import com.balaban.slider.model.SliderItem
import kotlin.random.Random

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ImageDetail(
    sliderItem: SliderItem,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sharedTransitionScope = LocalSharedTransitionScope.current
    val animatedContentScope = LocalAnimatedContentScope.current

    val text by remember {
        mutableStateOf(("Günbatımında Huzurun Rengi\n" +
                "\n" +
                "Bu nefes kesen günbatımı manzarası, doğanın büyüleyici gücünü ve huzur veren atmosferini yansıtıyor. Sonsuz gökyüzü ve sakin deniz, günün son ışıklarıyla birleşerek unutulmaz bir an yaratıyor. Doğanın bu büyüsü karşısında kendimi çok şanslı hissediyorum. \uD83C\uDF05✨").repeat(
            Random.nextInt(3,6)))
    }

    with(sharedTransitionScope){
        Column(
            modifier = Modifier
                .wrapContentSize()
        ) {
            Row(modifier = modifier
                .fillMaxWidth()
                .background(color = Color(0xFFB5CCFA))
                .padding(vertical = 12.dp)) {

                Image(
                    modifier = modifier
                        .clickable {
                            onBackClicked.invoke()
                        }
                        .padding(start = 12.dp),
                    painter = painterResource(id = R.drawable.go_back), contentDescription = "go back"
                )
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .sharedElement(
                            rememberSharedContentState(key = "title" + "-" + sliderItem.imageUrl),
                            animatedVisibilityScope = animatedContentScope,
                        ),
                    contentAlignment = Alignment.BottomCenter
                ) {
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

                Column(modifier = modifier.verticalScroll(rememberScrollState())) {
                    AsyncImage(
                        modifier = Modifier
                            .sharedElement(
                                rememberSharedContentState(key = "image" + "-" + sliderItem.imageUrl),
                                animatedVisibilityScope = animatedContentScope,
                            ),
                        model = sliderItem.imageUrl,
                        contentDescription = "Translated description of what the image contains",
                        contentScale = ContentScale.FillBounds
                    )

                    Text(
                        modifier = Modifier
                            .sharedElement(
                                rememberSharedContentState(key = "subTitle" + "-" + sliderItem.imageUrl),
                                animatedVisibilityScope = animatedContentScope,
                            )
                            .padding(top = 12.dp, start = 12.dp, end = 12.dp),
                        text = text,
                        fontSize = 14.sp
                    )
                }


            }
        }
    }

}
