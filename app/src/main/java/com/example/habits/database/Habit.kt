package com.example.habits.database

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*


/**
 * Data class of a single 'Habit' to be stored in the database. Parcelable so it can be passed as an argument in to [CreateHabitFragment.kt]
 */
@Entity
data class Habit(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "habit_name") val habitName: String,
    @ColumnInfo(name = "habit_desc") val habitDesc: String,
    @ColumnInfo(name = "days_of_week") val daysOfWeek: String,
    @ColumnInfo(name = "notif_hour") val notifHour: Int,
    @ColumnInfo(name = "notif_min") val notifMin: Int,
    @ColumnInfo(name = "streak") val streak: Int,
    @ColumnInfo(name = "notif_active") val notifActive: Int,
    @ColumnInfo(name = "last_notified") val lastNotified: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(uid)
        parcel.writeString(habitName)
        parcel.writeString(habitDesc)
        parcel.writeInt(notifHour)
        parcel.writeInt(notifMin)
        parcel.writeInt(streak)
        parcel.writeInt(notifActive)
        parcel.writeInt(lastNotified)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Habit> {
        override fun createFromParcel(parcel: Parcel): Habit {
            return Habit(parcel)
        }

        override fun newArray(size: Int): Array<Habit?> {
            return arrayOfNulls(size)
        }
    }
}
