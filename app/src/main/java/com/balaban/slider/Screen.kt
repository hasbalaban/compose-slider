package com.balaban.slider

import com.balaban.slider.model.SliderItem
import com.google.gson.Gson


sealed class Screen(val route: String) {
    data object Slider : Screen("sliderScreen")
    data class SliderItemDetail(private val rt: String ="sliderItemDetail?item="): Screen(route = "sliderItemDetail?item={item}"){
        fun createRoute(item: SliderItem): String {
            val data = Gson().toJson(item)
            return rt + data
        }
    }
}