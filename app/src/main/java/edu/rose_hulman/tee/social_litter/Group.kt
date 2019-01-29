package edu.rose_hulman.tee.social_litter

import android.os.Parcel
import android.os.Parcelable


class Group(var groupName : String,
            var description : String,
            var public : Boolean
) : Parcelable{
    constructor(parcel: Parcel) : this(
        TODO("groupName"),
        parcel.readString(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(description)
        parcel.writeByte(if (public) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Group> {
        override fun createFromParcel(parcel: Parcel): Group {
            return Group(parcel)
        }

        override fun newArray(size: Int): Array<Group?> {
            return arrayOfNulls(size)
        }
    }

}