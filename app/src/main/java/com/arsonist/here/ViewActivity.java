package com.arsonist.here;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.arsonist.here.Models.PhotoMetadataList;
import com.arsonist.here.fragments.ImageFragment;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ViewActivity extends AppCompatActivity {

    private List<PhotoMetadata> photoList;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private volatile int cursor = 0; // TODO TO add modifier ????

    public static List<PhotoMetadata> getSortedMetaList(){
        final Comparator cmp = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                if(o1 instanceof PhotoMetadata && o2 instanceof PhotoMetadata){
                    PhotoMetadata o1p = (PhotoMetadata) o1;
                    PhotoMetadata o2p = (PhotoMetadata) o2;
                    if(o1p.getDateTaken() > o2p.getDateTaken()){
                        return -1;
                    }else if(o1p.getDateTaken() < o2p.getDateTaken()){
                        return 1;
                    }else{
                        return 0;
                    }
                }else{
                    return 0;
                }
            }
        };
        List<PhotoMetadata> list = PhotoMetadataList.INSTANCE.getPhotoMetadataList();

        Collections.sort(list, cmp);
        return list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getIntent().getExtras().containsKey("ImagePos")){
            cursor = getIntent().getExtras().getInt("ImagePos");
        }else{
            cursor = 0;
        }

        initView();

    }

    private void initView(){
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        photoList = PhotoMetadataList.INSTANCE.getPhotoMetadataList();

        mViewPager.setCurrentItem(cursor,false);
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new ImageFragment();
            Bundle args = new Bundle();
            args.putInt("ImagePos", position);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return PhotoMetadataList.INSTANCE.getPhotoMetadataList().size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = DateFormat.getDateInstance().format(photoList.get(position).component3()) + "";
            return title;
        }
    }

}