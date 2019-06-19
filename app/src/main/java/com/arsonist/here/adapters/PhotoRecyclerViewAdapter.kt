package com.arsonist.here.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arsonist.here.Models.PhotoMetadataList.photoMetadataList
import com.arsonist.here.PhotoMetadata
import com.arsonist.here.R
import com.arsonist.here.fragments.GalleryFragment
import com.bumptech.glide.Glide
import com.l4digital.fastscroll.FastScroller
import kotlinx.android.synthetic.main.item_photo_view.view.*
import java.text.DateFormat

class PhotoRecyclerViewAdapter(val context: GalleryFragment, val itemClick: (PhotoMetadata) -> Unit) :
    RecyclerView.Adapter<PhotoRecyclerViewAdapter.PhotoViewHolder>(),
    FastScroller.SectionIndexer {


    fun addPhoto(photoMetadata: PhotoMetadata) {
        photoMetadataList.add(photoMetadata)
        notifyItemInserted(photoMetadataList.size - 1)
    }

    override fun getSectionText(position: Int): CharSequence {
        return DateFormat.getDateInstance().format(photoMetadataList[position].dateTaken)
    }

    override fun getItemCount() = photoMetadataList.size

    override fun onBindViewHolder(viewHolder: PhotoViewHolder, position: Int) {

        photoMetadataList.getOrNull(position)?.let {
            viewHolder.loadView(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_photo_view, parent, false), itemClick
        )
    }

    inner class PhotoViewHolder(itemView: View, itemClick: (PhotoMetadata) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val photoView = itemView.photoView

        // error check
        fun loadView(photoMetadata: PhotoMetadata) {
            Glide.with(itemView.context)
                .load(photoMetadata.data)
                .centerCrop()
                .thumbnail(0.1f)
                .into(photoView)

            itemView.setOnClickListener { itemClick(photoMetadata) }
        }

    }
}