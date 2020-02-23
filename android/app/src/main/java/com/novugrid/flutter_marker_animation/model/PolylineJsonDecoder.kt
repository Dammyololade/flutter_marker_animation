package com.novugrid.flutter_marker_animation.model

import com.google.gson.annotations.SerializedName

data class PolylineJsonDecoder (

        @SerializedName("geocoded_waypoints") val geocoded_waypoints : List<GeocodedWaypoints>,
        @SerializedName("routes") val routes : List<Routes>,
        @SerializedName("status") val status : String
)