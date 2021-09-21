package com.example.runningtracker.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RunDAO {
     @Insert(onConflict =  OnConflictStrategy.REPLACE)
     suspend fun insertRun(run: Run)

     @Delete
     suspend fun  deleteRun(run :Run)



     @Query("SELECT * FROM run_table ORDER BY timestamp DESC")
     fun getAllRunsSortedByDate():LiveData<List<Run>>

    @Query("SELECT * FROM run_table ORDER BY avgSpeedInKMh DESC")
    fun getAllRunsSortedBySpeed():LiveData<List<Run>>

    @Query("SELECT * FROM run_table ORDER BY timeInMillis DESC")
    fun getAllRunsSortedByTimeinMillis():LiveData<List<Run>>

    @Query("SELECT * FROM run_table ORDER BY distanceInMeters DESC")
    fun getAllRunsSortedByDistanceinMeters():LiveData<List<Run>>

    @Query("SELECT * FROM run_table ORDER BY caloriesBurn DESC")
    fun getAllRunsSortedByCaloriesBurn():LiveData<List<Run>>



    @Query("SELECT SUM(timeInMillis) FROM run_table")
    fun getTotalTimeInMIllis():LiveData<Long>

    @Query("SELECT SUM(caloriesBurn) FROM run_table")
    fun getTotalCaloriesBurn():LiveData<Int>

    @Query("SELECT SUM(distanceInMeters) FROM run_table")
    fun getTotalDistanceInMeters():LiveData<Int>

    @Query("SELECT SUM(avgSpeedInKMh) FROM run_table")
    fun getTotalAvgSpeed():LiveData<Float>
}