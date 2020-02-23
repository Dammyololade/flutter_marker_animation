package com.novugrid.flutter_marker_animation

import android.animation.ValueAnimator
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.LinearInterpolator

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.util.ArrayList

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var polyLineList: List<LatLng>
    private lateinit var polylineOptions: PolylineOptions
    private lateinit var blackPolylineOptions: PolylineOptions
    private lateinit var blackPolyline: Polyline
    private lateinit var greyPolyLine: Polyline
    private lateinit var marker: Marker
    private lateinit var handler: Handler
    private lateinit var lagos: LatLng
    private var index = 0
    private  var next:Int = 0
    private var v = 0f
    private var lat = 0.0
    private  var lng = 0.0
    private lateinit var startPosition: LatLng
    private lateinit var endPosition:LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val latitude = 6.502750
        val longitude = 3.370530

        mMap.isTrafficEnabled = false
        mMap.isIndoorEnabled = false
        mMap.isBuildingsEnabled = false
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.setAllGesturesEnabled(true)
        mMap.uiSettings.isZoomGesturesEnabled = true
        lagos = LatLng(latitude, longitude)
        mMap.addMarker(MarkerOptions().position(lagos).title("Marker in Lagos"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(lagos))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(lagos))
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.Builder()
                .target(googleMap.cameraPosition.target)
                .zoom(17f)
                .bearing(30f)
                .tilt(45f)
                .build()))
        polyLineList = decodePolyline(getString(R.string.encoded_polyline))
        drawPolyline()
        animateMarker()
    }

    private fun decodePolyline(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(
                    lat.toDouble() / 1E5,
                    lng.toDouble() / 1E5
            )
            poly.add(p)
        }

        return poly
    }

    private fun getBearingAngle(begin: LatLng, end: LatLng): Float {
        val lat = Math.abs(begin.latitude - end.latitude)
        val lng = Math.abs(begin.longitude - end.longitude)
        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return Math.toDegrees(Math.atan(lng / lat)).toFloat()
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (90 - Math.toDegrees(Math.atan(lng / lat)) + 90).toFloat()
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (Math.toDegrees(Math.atan(lng / lat)) + 180).toFloat()
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (90 - Math.toDegrees(Math.atan(lng / lat)) + 270).toFloat()
        return (-1).toFloat()
    }

    private fun drawPolyline() {
        //Adjusting bounds
        val builder = LatLngBounds.Builder()
        for (latLng in polyLineList) {
            builder.include(latLng)
        }
        val bounds = builder.build()
        if(bounds.contains(lagos)) {
            val mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 2)
            mMap.animateCamera(mCameraUpdate)
        }

        polylineOptions = PolylineOptions()
        polylineOptions.color(Color.GRAY)
        polylineOptions.width(5f)
        polylineOptions.startCap(SquareCap())
        polylineOptions.endCap(SquareCap())
        polylineOptions.jointType(JointType.ROUND)
        polylineOptions.addAll(polyLineList)
        greyPolyLine = mMap.addPolyline(polylineOptions)

        blackPolylineOptions = PolylineOptions()
        blackPolylineOptions.width(5f)
        blackPolylineOptions.color(Color.BLACK)
        blackPolylineOptions.startCap(SquareCap())
        blackPolylineOptions.endCap(SquareCap())
        blackPolylineOptions.jointType(JointType.ROUND)
        blackPolyline = mMap.addPolyline(blackPolylineOptions)

        mMap.addMarker(MarkerOptions()
                .position(polyLineList[polyLineList.size - 1])
                .icon(BitmapDescriptorFactory.defaultMarker(1.0f))
        )
    }

    private fun animateMarker() {
        val polylineAnimator = ValueAnimator.ofInt(0, 100)
        polylineAnimator.duration = 2000
        polylineAnimator.interpolator = LinearInterpolator()
        polylineAnimator.addUpdateListener { valueAnimator ->
            val points = greyPolyLine.points
            val percentValue = valueAnimator.animatedValue as Int
            val size = points.size
            val newPoints = (size * (percentValue / 100.0f)).toInt()
            val p: List<LatLng> = points.subList(0, newPoints)
            blackPolyline.points = p
        }
        polylineAnimator.start()
        marker = mMap.addMarker(MarkerOptions().position(lagos)
                .flat(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car)))
        handler = Handler()
        index = -1
        next = 1
        handler.postDelayed(object: Runnable {

            override fun run() {
                if (index < polyLineList.size - 1) {
                    index++
                    next = index + 1
                }
                if (index < polyLineList.size - 1) {
                    startPosition = polyLineList[index]
                    endPosition = polyLineList[next]
                }
//            startPosition = lagos
//            endPosition = LatLng(6.595680, 3.337030)
                val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
                valueAnimator.duration = 3000
                valueAnimator.interpolator = LinearInterpolator()
                valueAnimator.addUpdateListener {
                    v = it.animatedFraction
                    lng = v * endPosition.longitude + (1 - v) * startPosition.longitude
                    lat = v * endPosition.latitude + (1 - v) * startPosition.latitude
                    val newPos = LatLng(lat, lng)
                    marker.position = newPos
                    marker.setAnchor(0.5f, 0.5f)
                    marker.rotation = getBearingAngle(startPosition, newPos)
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.Builder().target(newPos)
                            .zoom(12.5f).build()))
                }
                valueAnimator.start()
                if (index != polyLineList.size - 1) {
                    handler.postDelayed(this, 3000);
                }
            }

        }, 3000)
    }
}
