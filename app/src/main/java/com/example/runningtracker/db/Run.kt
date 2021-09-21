package com.example.runningtracker.db
import android.graphics.Bitmap
import  androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(tableName = "run_table")
data class Run (
var img:Bitmap?=null,
var timestamp:Long=0L,
var avgSpeedInKMh :Float =0f,
var distanceInMeters :Int =0,
var timeInMillis :Long =0L,
var caloriesBurn :Int =0
){
    @PrimaryKey(autoGenerate = true)
    var id :Int?=null
}