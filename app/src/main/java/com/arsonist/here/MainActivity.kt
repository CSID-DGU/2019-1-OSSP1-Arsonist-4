package com.arsonist.here

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.BaseColumns
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.widget.Toast
import com.arsonist.here.Views.PhotoRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE_READ_EXTERNAL_STORAGE = 1
    }

    private val photoMetadataList = mutableListOf<PhotoMetadata>()
    private val mainRecyclerViewAdapter = PhotoRecyclerViewAdapter()
    private val mainRecyclerViewLayoutManager by lazy { GridLayoutManager(this, 3) }

    private fun startToFetchPhoto() {
        val cursor = contentResolver.query(
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
                photoMetadataList.add(photoMetadata)
                mainRecyclerViewAdapter.addPhoto(photoMetadata)
            }
            asyncTask.execute()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainRecyclerView.adapter = mainRecyclerViewAdapter
        mainRecyclerView.layoutManager = mainRecyclerViewLayoutManager

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                Toast.makeText(this, "앱 권한을 설정해주세요", Toast.LENGTH_SHORT).show()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_CODE_READ_EXTERNAL_STORAGE
                )
            }
        } else {
            // 사진을 불러오는 코드 작성
            startToFetchPhoto()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_READ_EXTERNAL_STORAGE) {
            if (grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                // 사진을 불러오는 코드 작성
                startToFetchPhoto()
            }
        }
    }
}
