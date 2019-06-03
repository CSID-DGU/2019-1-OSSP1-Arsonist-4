package com.arsonist.here

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.arsonist.here.fragments.GalleryFragment
import com.arsonist.here.fragments.MapFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        private const val VIEWPAGER_COUNT = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                    GalleryFragment.REQUEST_CODE_READ_EXTERNAL_STORAGE
                )
            }
        } else {
            view_pager.adapter = ViewPagerAdapter(supportFragmentManager,this)
        }


    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == GalleryFragment.REQUEST_CODE_READ_EXTERNAL_STORAGE) {
            if (grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                // 사진을 불러오는 코드 작성
                //startToFetchPhoto()
                view_pager.adapter = ViewPagerAdapter(supportFragmentManager,this)
            }
        }
    }

    private class ViewPagerAdapter internal  constructor(fm: FragmentManager?, val mContext: Context?) : FragmentPagerAdapter(fm) {
        //internal ViewPagerAdapter class, with 2 fragments.

        override fun getItem(position: Int): Fragment? {
            lateinit var fragment : Fragment
            when (position){
                0 -> fragment = GalleryFragment()
                1 -> fragment = MapFragment()
            }
            return fragment
        }

        override fun getCount(): Int {
            return VIEWPAGER_COUNT
        }
        // for the pageTitleStrip View at the top of the viewpager
        override fun getPageTitle(position: Int): CharSequence? {
            when(position){
                0 -> return  mContext?.resources?.getString(R.string.gallery_fragment)
                1 -> return  mContext?.resources?.getString(R.string.map_fragment)
            }
            return ""
        }
    }
}