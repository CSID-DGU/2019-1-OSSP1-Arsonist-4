package com.arsonist.here

import android.database.Cursor
import android.location.Location
import android.provider.BaseColumns
import android.provider.MediaStore

data class PhotoMetadata(
    val id: Long,
    val data: String,
    val dateTaken: Long,
    val location: Location
) {
    // companion : static object
    companion object {
        fun photoMetadataFromCursor(cursor: Cursor): PhotoMetadata {
            val latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.LATITUDE))
            val longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.LONGITUDE))
            val location = Location("").apply {
                this.latitude = latitude
                this.longitude = longitude
            }

            return PhotoMetadata(
                id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID)),
                data = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA)),
                dateTaken = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_TAKEN)),
                location = location
            )
        }
    }
}