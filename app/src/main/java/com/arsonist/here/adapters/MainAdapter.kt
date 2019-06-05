package com.arsonist.here.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.arsonist.here.fragments.GalleryFragment
import com.arsonist.here.fragments.MapFragment

class MainAdapter : FragmentPagerAdapter {
    var data1 : Fragment = GalleryFragment()
    var data2 : Fragment = MapFragment()

    var mData : ArrayList<Fragment> = arrayListOf(data1,data2)

    constructor(fm : FragmentManager) : super(fm){
    }

    override fun getItem(position: Int): Fragment {
        return mData.get(position)
    }
    override fun getCount(): Int {
        return mData.size
    }
}
