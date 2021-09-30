package com.example.runningtracker.repositeries

import com.example.runningtracker.db.Run
import com.example.runningtracker.db.RunDAO
import javax.inject.Inject

class MainRepository @Inject constructor(
    val runDAO: RunDAO
){
   suspend fun  insertRun(run : Run) = runDAO.insertRun(run)

    suspend fun deleteRun(run: Run) = runDAO.deleteRun(run)

    fun getAllRunSortedByDate() = runDAO.getAllRunsSortedByDate()

    fun getAllRunSortedByDistance() = runDAO.getAllRunsSortedByDistanceinMeters()

    fun getAllRunSortedByTimeInMillis() = runDAO.getAllRunsSortedByTimeinMillis()

    fun getAllRunSortedByCaloriesBurn() = runDAO.getAllRunsSortedByCaloriesBurn()

    fun getAllRunSortedBySpeed() = runDAO.getAllRunsSortedBySpeed()

    fun getTotalAvgSpeed() = runDAO.getTotalAvgSpeed()

    fun getTotalCaloriesBurn() = runDAO.getTotalCaloriesBurn()

    fun getTotalDistanceInMeters() = runDAO.getTotalDistanceInMeters()

    fun getTotalTimeInMIllis() = runDAO.getTotalTimeInMIllis()

}