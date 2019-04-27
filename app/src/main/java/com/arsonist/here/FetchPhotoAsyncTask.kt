package com.arsonist.here

import android.database.Cursor
import android.os.AsyncTask

class FetchPhotoAsyncTask(
    private val cursor: Cursor,
    private val callback : ((PhotoMetadata) -> Unit)
): AsyncTask<Unit, PhotoMetadata, Unit>() {
    override fun doInBackground(vararg params: Unit?) {
        with(cursor) {
            cursor?.let {
                if (cursor.moveToFirst()) {
                    publishProgress(PhotoMetadata.photoMetadataFromCursor(it))
                }
                while (cursor.moveToNext()) {
                    // 비동기로 돌아가는 것을 확인하기 위한 코드
                    // SystemClock.sleep(100)
                    publishProgress(PhotoMetadata.photoMetadataFromCursor(it))
                }
            }
        }
    }

    override fun onProgressUpdate(vararg values: PhotoMetadata) {
        super.onProgressUpdate(*values)
        values.forEach { callback(it) }
    }
}