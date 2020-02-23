package com.novugrid.flutter_marker_animation.model
import com.google.gson.annotations.SerializedName
import com.novugrid.flutter_marker_animation.model.Bounds
import com.novugrid.flutter_marker_animation.model.Legs
import com.novugrid.flutter_marker_animation.model.OverviewPolyline


data class Routes (

        @SerializedName("bounds") val bounds : Bounds,
        @SerializedName("copyrights") val copyrights : String,
        @SerializedName("legs") val legs : List<Legs>,
        @SerializedName("overview_polyline") val overview_polyline : OverviewPolyline,
        @SerializedName("summary") val summary : String,
        @SerializedName("warnings") val warnings : List<String>,
        @SerializedName("waypoint_order") val waypoint_order : List<String>
)