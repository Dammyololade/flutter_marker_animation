package com.novugrid.flutter_marker_animation.model

import com.google.gson.annotations.SerializedName
import com.novugrid.flutter_marker_animation.model.*


data class Steps (

        @SerializedName("distance") val distance : Distance,
        @SerializedName("duration") val duration : Duration,
        @SerializedName("end_location") val end_location : EndLocation,
        @SerializedName("html_instructions") val html_instructions : String,
        @SerializedName("polyline") val polyline : Polyline,
        @SerializedName("start_location") val start_location : StartLocation,
        @SerializedName("travel_mode") val travel_mode : String
)