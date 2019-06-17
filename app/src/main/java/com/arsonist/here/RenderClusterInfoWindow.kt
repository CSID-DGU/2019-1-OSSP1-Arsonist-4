package com.arsonist.here

import android.content.Context
import android.widget.ImageView
import com.arsonist.here.Models.Place
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator


class RenderClusterInfoWindow internal constructor(context: Context, map: GoogleMap, clusterManager: ClusterManager<Place>) : DefaultClusterRenderer<Place>(context, map, clusterManager) {

    private val mIconGenerator = IconGenerator(context)
    private val mClusterIconGenerator = IconGenerator(context)
    private val mImageView: ImageView? = null
    private val mClusterImageView: ImageView? = null
    private val mDimension: Int = 0


    override fun onClusterRendered(cluster: Cluster<Place>?, marker: Marker?) {
        super.onClusterRendered(cluster, marker)
    }

    override fun onBeforeClusterItemRendered(item: Place?, markerOptions: MarkerOptions?) {
        markerOptions!!.title("${item!!.lat}, ${item.lng}")

        super.onBeforeClusterItemRendered(item, markerOptions)
        var markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);

        /*if(item.Data != null) {
            val smallMarker = Bitmap.createScaledBitmap(item.Data,200,200,false)
            markerDescriptor = BitmapDescriptorFactory.fromBitmap(smallMarker)
        }*/

        markerOptions.icon(markerDescriptor).snippet(item.title);
    }
}