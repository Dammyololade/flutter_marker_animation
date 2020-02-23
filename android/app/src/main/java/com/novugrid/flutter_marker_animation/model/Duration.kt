package com.novugrid.flutter_marker_animation.model

import com.google.gson.annotations.SerializedName

data class Duration (

		@SerializedName("text") val text : String,
		@SerializedName("value") val value : Int
)