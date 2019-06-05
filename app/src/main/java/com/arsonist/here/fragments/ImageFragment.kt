package com.arsonist.here.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.arsonist.here.Models.PhotoMetadataList
import com.arsonist.here.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_image.*


class ImageFragment : Fragment() {

    var imagePos = 0;
    var imagePosCurrent = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)

        imagePos = args!!.getInt("ImagePos")
        imagePosCurrent = args!!.getInt("ImagePosCurrent")

        if(imagePos != 0 && imagePos != imagePosCurrent)
            imagePos += imagePosCurrent
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return if (inflater != null) {
            inflater.inflate(R.layout.fragment_image, container, false)
        } else {
            super.onCreateView(inflater, container, savedInstanceState)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.e("Image Data3", PhotoMetadataList.photoMetadataList.get(imagePos).toString())
        System.out.print(PhotoMetadataList.photoMetadataList.get(imagePos).data)
        Glide.with(this)
            .load(PhotoMetadataList.photoMetadataList.get(imagePos).data)
            .into(image_view)
    }
}