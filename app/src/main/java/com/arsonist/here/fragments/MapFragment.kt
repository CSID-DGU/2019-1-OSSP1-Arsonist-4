package com.arsonist.here.fragments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.arsonist.here.Models.PhotoMetadataList
import com.arsonist.here.Models.Place
import com.arsonist.here.MultiDrawable
import com.arsonist.here.R
import com.arsonist.here.adapters.PopupAdapter
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import java.io.File
import java.util.*


class MapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var rootView: View
    private lateinit var mapView: MapView
    private lateinit var mMap: GoogleMap

    private class PlaceRenderer internal constructor(
        context: Context,
        mMap: GoogleMap,
        clusterManager: ClusterManager<Place>
    ) : DefaultClusterRenderer<Place>(context, mMap, clusterManager) {

        private val mIconGenerator = IconGenerator(context)
        private val mClusterIconGenerator = IconGenerator(context)
        private var mImageView: ImageView? = null
        private var mClusterImageView: ImageView? = null
        private var mDimension: Int = 0

        init {
            var multiProfile = LayoutInflater.from(context).inflate(R.layout.multi_photo, null)
            mClusterIconGenerator.setContentView(multiProfile);
            mClusterImageView = multiProfile.findViewById(R.id.imageMap) as ImageView
            mImageView = ImageView(context)
            mDimension = context.resources.getDimension(R.dimen.custom_photo_image).toInt()
            mImageView!!.layoutParams = ViewGroup.LayoutParams(mDimension, mDimension)
            var padding = context.resources.getDimension(R.dimen.custom_photo_padding).toInt()
            mImageView!!.setPadding(padding, padding, padding, padding)
            mIconGenerator.setContentView(mImageView);
        }

        override fun onClusterRendered(cluster: Cluster<Place>?, marker: Marker?) {
            super.onClusterRendered(cluster, marker)
        }

        override fun onBeforeClusterItemRendered(item: Place?, markerOptions: MarkerOptions?) {
            markerOptions!!.title("${item!!.lat}, ${item.lng}")
            super.onBeforeClusterItemRendered(item, markerOptions)

            val options = BitmapFactory.Options();
            val file = File(item.Data)
            val originalBm = BitmapFactory.decodeFile(file.absolutePath, options)
            mImageView!!.setImageBitmap(originalBm)
            var icon = mIconGenerator.makeIcon()
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(item.name)
        }

        override fun onBeforeClusterRendered(cluster: Cluster<Place>?, markerOptions: MarkerOptions?) {
            super.onBeforeClusterRendered(cluster, markerOptions)
            val profilePhotos = ArrayList<Drawable>(cluster!!.getSize())
            val width = mDimension
            val height = mDimension

            for (p in cluster.getItems()) {
                // Draw 4 at most.
                if (profilePhotos.size == 1) break
                val options = BitmapFactory.Options();
                val file = File(p.Data)
                val originalBm = BitmapFactory.decodeFile(file.absolutePath, options)
                val drawable = bitmapToDrawable(originalBm)
                drawable.setBounds(0, 0, width, height)
                profilePhotos.add(drawable)
            }
            val multiDrawable = MultiDrawable(profilePhotos)
            multiDrawable.setBounds(0, 0, width, height)

            mClusterImageView!!.setImageDrawable(multiDrawable)
            val icon = mClusterIconGenerator.makeIcon(cluster.getSize().toString())
            if (markerOptions != null) {
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon))
            }
        }

        override fun shouldRenderAsCluster(cluster: Cluster<Place>?): Boolean {
            if (cluster != null) {
                return cluster.size > 1
            } else return super.shouldRenderAsCluster(cluster)
        }

        @Suppress("DEPRECATION")
        private fun bitmapToDrawable(bitmap: Bitmap?): BitmapDrawable {
            return BitmapDrawable(bitmap)
        }
    }
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

        for (i in 0 until photoList.size) {

            if (photoList[i].location.latitude != 0.0 && photoList[i].location.longitude != 0.0) {
                mClusterManager!!.addItem(
                    Place(
                        photoList[i].location.latitude,
                        photoList[i].location.longitude,
                        "",
                        photoList[i].data
                    )
                )
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        setupMap(googleMap)

        mClusterManager = ClusterManager(context, googleMap)

        //val renderer = RenderClusterInfoWindow(context!!, googleMap, this.mClusterManager!!)

        val renderer = PlaceRenderer(context!!, googleMap, this.mClusterManager!!)
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
                    Log.e("test marker2", address)

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