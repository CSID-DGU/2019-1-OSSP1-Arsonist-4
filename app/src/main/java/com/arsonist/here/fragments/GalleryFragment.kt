package com.arsonist.here.fragments

import android.content.Intent
import android.os.Bundle
import android.provider.BaseColumns
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.arsonist.here.FetchPhotoAsyncTask
import com.arsonist.here.Models.PhotoMetadataList
import com.arsonist.here.R
import com.arsonist.here.ViewActivity
import com.arsonist.here.adapters.PhotoRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_gallery.*


class GalleryFragment : Fragment() {

    companion object {
        const val REQUEST_CODE_READ_EXTERNAL_STORAGE = 1
    }

    private val mainRecyclerViewAdapter = PhotoRecyclerViewAdapter(this) { photoMetadata ->
        // 눌렀을때 처리
        Log.d("where", photoMetadata.location.latitude.toString())

        val myIntent = Intent(this.context, ViewActivity::class.java)
        var pos = 0;
        for (i in 0 until PhotoMetadataList.photoMetadataList.size) {
            if (photoMetadata.id == PhotoMetadataList.photoMetadataList[i].id) {
                pos = i;
                break;
            }
        }
        Log.d("position", pos.toString())
        myIntent.putExtra("ImagePos", pos) // TODO id == list order ???
        startActivity(myIntent)
//        Toast.makeText(
//            context,
//            "${photoMetadata.location.latitude}, ${photoMetadata.location.longitude}",
//            Toast.LENGTH_SHORT
//        ).show()
    }

    private val mainRecyclerViewLayoutManager by lazy { GridLayoutManager(activity, 5) }

    private fun startToFetchPhoto() {

        val cursor = context!!.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf(
                // ID : 가져오는 사진의 UNIQUE한 ID
                BaseColumns._ID,
                MediaStore.Images.ImageColumns.DATA, // 파일 경로
                MediaStore.Images.ImageColumns.DATE_TAKEN, // 찍은 날짜
                MediaStore.Images.ImageColumns.LATITUDE, // 위치
                MediaStore.Images.ImageColumns.LONGITUDE
            ),
            null,
            null,
            "${MediaStore.Images.ImageColumns.DATE_TAKEN} DESC"
        )

        cursor?.let {
            val asyncTask = FetchPhotoAsyncTask(it) { photoMetadata ->
                mainRecyclerViewAdapter.addPhoto(photoMetadata)
            }
            asyncTask.execute()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startToFetchPhoto()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return if (inflater != null) {
            inflater.inflate(R.layout.fragment_gallery, container, false)
        } else {
            super.onCreateView(inflater, container, savedInstanceState)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainRecyclerView.adapter = mainRecyclerViewAdapter
        mainRecyclerView.layoutManager = mainRecyclerViewLayoutManager

    }
}