package com.novugrid.flutter_marker_animation.model

import com.google.gson.annotations.SerializedName


data class Bounds (

		@SerializedName("northeast") val northeast : Northeast,
		@SerializedName("southwest") val southwest : Southwest
)