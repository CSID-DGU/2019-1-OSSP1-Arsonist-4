package com.arsonist.here.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import com.arsonist.here.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.info_window_layout.view.*

/**
 * Created by anartzmugika on 22/12/17.
 */
class PopupAdapter(inflater: LayoutInflater) : GoogleMap.InfoWindowAdapter {
    private var inflater: LayoutInflater? = null

    init {
        this.inflater = inflater
    }

    override fun getInfoWindow(marker: Marker): View? {
        return null
    }

    @SuppressLint("InflateParams")
    override fun getInfoContents(marker: Marker): View {
        val popup = inflater!!.inflate(R.layout.info_window_layout, null)

        popup.titleInfo.text = marker.title
        return popup
    }
}