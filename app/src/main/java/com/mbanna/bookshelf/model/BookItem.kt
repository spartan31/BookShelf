package com.mbanna.bookshelf.model

import android.os.Parcel
import android.os.Parcelable

data class BookItem(
    val alias: String?,
    val hits: Int,
    val id: String?,
    val image: String?,
    val lastChapterDate: Int,
    val title: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(alias)
        parcel.writeInt(hits)
        parcel.writeString(id)
        parcel.writeString(image)
        parcel.writeInt(lastChapterDate)
        parcel.writeString(title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BookItem> {
        override fun createFromParcel(parcel: Parcel): BookItem {
            return BookItem(parcel)
        }

        override fun newArray(size: Int): Array<BookItem?> {
            return arrayOfNulls(size)
        }
    }
}