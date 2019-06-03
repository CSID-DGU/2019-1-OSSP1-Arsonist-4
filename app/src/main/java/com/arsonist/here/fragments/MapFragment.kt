package com.arsonist.here.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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

    private fun getRealPathFromURI(contentURI: Uri, context: Context): String {
        val result: String
        val cursor = context.contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) {
            result = contentURI.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }

    private fun addPlaceItems() {
        val args = Bundle()
        //var photoInfo = args?.getSerializable("photoInfo") as PhotoMetadata

        //Log.d("test", photoInfo.data.toString())
        //mClusterManager!!.addItem(Place(photoInfo.location.latitude, photoInfo.location.longitude))

        var photoList = PhotoMetadataList.photoMetadataList
        for (i in 0 until photoList.size) {
            val contentURI = photoList[i].data
            //val baseUri = Uri.parse("content://media/external/images/media")

            //val Uri = Uri.withAppendedPath(baseUri, "" + photoList[i].id);
            //Log.d("test", Uri.toString())
            //Log.d("test2", Uri.fromFile(File(stringUri)).toString())

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


        mClusterManager!!.setOnClusterClickListener(
            ClusterManager.OnClusterClickListener<Place> {
                Toast.makeText(context, "Cluster click", Toast.LENGTH_SHORT).show()
                false
            })

        mClusterManager!!.setOnClusterItemClickListener(
            ClusterManager.OnClusterItemClickListener<Place> {
                Toast.makeText(context, "Cluster item click", Toast.LENGTH_SHORT).show()

                // if true, click handling stops here and do not show info view, do not move camera
                // you can avoid this by calling:
                // renderer.getMarker(clusterItem).showInfoWindow();

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