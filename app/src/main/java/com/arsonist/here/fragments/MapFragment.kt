package com.arsonist.here.fragments

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.arsonist.here.Models.PhotoMetadataList
import com.arsonist.here.Models.Place
import com.arsonist.here.R
import com.arsonist.here.RenderClusterInfoWindow
import com.arsonist.here.adapters.PopupAdapter
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import java.util.*


class MapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var rootView: View
    private lateinit var mapView: MapView
    private lateinit var mmap: GoogleMap

    private var mClusterManager: ClusterManager<Place>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_map, container, false)
        mapView = rootView.findViewById(R.id.mapview) as MapView
        mapView.getMapAsync(this)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mapView.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onResume() {
        mapView.onResume()
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    protected fun setupMap(mMap: GoogleMap?) {
        if (mMap != null) {
            mMap.isBuildingsEnabled = true
            mMap.isIndoorEnabled = true
            mMap.isTrafficEnabled = true
            val mUiSettings: UiSettings = mMap.uiSettings
            mUiSettings.isZoomControlsEnabled = true
            mUiSettings.isCompassEnabled = true
            mUiSettings.isMyLocationButtonEnabled = true
            mUiSettings.isScrollGesturesEnabled = true
            mUiSettings.isZoomGesturesEnabled = true
            mUiSettings.isTiltGesturesEnabled = true
            mUiSettings.isRotateGesturesEnabled = true

        }

    }

    private fun addPlaceItems() {
        var photoList = PhotoMetadataList.photoMetadataList

        Log.e("photo size", photoList.size.toString())
        for (i in 0 until photoList.size) {
            val contentURI = photoList[i].data
            //val baseUri = Uri.parse("content://media/external/images/media")

            //val Uri = Uri.withAppendedPath(baseUri, "" + photoList[i].id);
            //Log.d("test", Uri.toString())

            if (photoList[i].location.latitude != 0.0 && photoList[i].location.longitude != 0.0) {

                //val bitmap = MediaStore.Images.Media.getBitmap(context!!.contentResolver, Uri)

                mClusterManager!!.addItem(
                    Place(
                        photoList[i].location.latitude,
                        photoList[i].location.longitude,
                        ""//,
                        //bitmap
                    )
                )
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        setupMap(googleMap)

        mClusterManager = ClusterManager(context, googleMap)

        val renderer = RenderClusterInfoWindow(context!!, googleMap, this.mClusterManager!!)

        mClusterManager!!.setRenderer(renderer)


        googleMap.setOnCameraIdleListener(mClusterManager)
        googleMap.setOnMarkerClickListener(mClusterManager)
        googleMap.setOnInfoWindowClickListener(mClusterManager)

        addPlaceItems()

        val geocode: Geocoder = Geocoder(context, Locale.getDefault())
        var addresses: List<Address>

        mClusterManager!!.setOnClusterClickListener(
            ClusterManager.OnClusterClickListener<Place> {


                addresses = geocode.getFromLocation(
                    it.position.latitude,
                    it.position.longitude,
                    1
                ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                if (addresses.isNotEmpty()) {
                    val address =
                        addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    val city = addresses[0].getLocality() // 구
                    val state = addresses[0].getAdminArea() // 시
                    val country = addresses[0].getCountryName() // 나라
                    val postalCode = addresses[0].getPostalCode() // 우편 번호
                    val knownName = addresses[0].getFeatureName() // 지번

                    // if true, click handling stops here and do not show info view, do not move camera
                    // you can avoid this by calling:
                    // renderer.getMarker(clusterItem).showInfoWindow();

                    Log.e("test marker", address)

                    Toast.makeText(context, address, Toast.LENGTH_SHORT).show()
                }
                false
            })

        mClusterManager!!.setOnClusterItemClickListener(
            ClusterManager.OnClusterItemClickListener<Place> {

                addresses = geocode.getFromLocation(
                    it.lat,
                    it.lng,
                    1
                ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                Log.e("test marker", addresses.size.toString())

                if (addresses.isNotEmpty()) {

                    val address =
                        addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    val city = addresses[0].getLocality() // 구
                    val state = addresses[0].getAdminArea() // 시
                    val country = addresses[0].getCountryName() // 나라
                    val postalCode = addresses[0].getPostalCode() // 우편 번호
                    val knownName = addresses[0].getFeatureName() // 지번

                    // if true, click handling stops here and do not show info view, do not move camera
                    // you can avoid this by calling:
                    // renderer.getMarker(clusterItem).showInfoWindow();

                    Log.e("test marker", address)

                    Toast.makeText(context, address, Toast.LENGTH_SHORT).show()
                }

                false
            })

        mClusterManager!!.getMarkerCollection()
            .setOnInfoWindowAdapter(PopupAdapter(LayoutInflater.from(context)))

        mClusterManager!!.setOnClusterItemInfoWindowClickListener(
            ClusterManager.OnClusterItemInfoWindowClickListener<Place> { stringClusterItem ->
                Toast.makeText(
                    context, "Clicked info window: " + stringClusterItem.lat + ", " + stringClusterItem.lng,
                    Toast.LENGTH_SHORT
                ).show()
            })


        googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(37.56, 126.97)))
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(5F))

        mClusterManager!!.cluster()
    }
}