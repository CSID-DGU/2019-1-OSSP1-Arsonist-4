package com.arsonist.here.Models

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class Place(val lat: Double, val lng: Double, var name: String? = "", var Data: String? = null) : ClusterItem {

    fun getmPosition(): LatLng = mPosition

    private val mPosition: LatLng

    init {
        mPosition = LatLng(lat, lng)
    }

    override fun getPosition(): LatLng = this.getmPosition()

    override fun getTitle(): String? = null

    override fun getSnippet(): String? = null

}