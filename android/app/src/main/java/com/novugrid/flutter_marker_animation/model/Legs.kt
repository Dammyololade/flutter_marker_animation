package com.novugrid.flutter_marker_animation.model

import com.google.gson.annotations.SerializedName


data class Legs (

        @SerializedName("distance") val distance : Distance,
        @SerializedName("duration") val duration : Duration,
        @SerializedName("end_address") val end_address : String,
        @SerializedName("end_location") val end_location : EndLocation,
        @SerializedName("start_address") val start_address : String,
        @SerializedName("start_location") val start_location : StartLocation,
        @SerializedName("steps") val steps : List<Steps>,
        @SerializedName("traffic_speed_entry") val traffic_speed_entry : List<String>,
        @SerializedName("via_waypoint") val via_waypoint : List<String>
)