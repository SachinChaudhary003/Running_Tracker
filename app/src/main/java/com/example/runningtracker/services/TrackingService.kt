package com.example.runningtracker.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.runningtracker.R
import com.example.runningtracker.other.Constants.ACTION_PAUSE_SERVICE
import com.example.runningtracker.other.Constants.ACTION_SHOW_TRACKING_FRAGMENT
import com.example.runningtracker.other.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.runningtracker.other.Constants.ACTION_STOP_SERVICE
import com.example.runningtracker.other.Constants.FASTEST_LOCATION_INTERVAL
import com.example.runningtracker.other.Constants.LOCATION_UPDATE_INTERVAL
import com.example.runningtracker.other.Constants.NOTIFICATION_CHANNEL_ID
import com.example.runningtracker.other.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.runningtracker.other.Constants.NOTIFICATION_ID
import com.example.runningtracker.other.Trackingutility
import com.example.runningtracker.ui.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import timber.log.Timber

typealias Polyline = MutableList<LatLng>
typealias Polylines = MutableList<Polyline>

class TrackingService:LifecycleService() {

    var isfisrtdrun = true

    lateinit var  fusedLocationProviderClient: FusedLocationProviderClient

    companion object{
        val istracking = MutableLiveData<Boolean>()
        val pathpoints = MutableLiveData<Polylines>()
    }

    private fun postInitialValues(){
        istracking.postValue(false)
        pathpoints.postValue(mutableListOf())
    }

    override fun onCreate() {
        super.onCreate()
        postInitialValues()
        fusedLocationProviderClient = FusedLocationProviderClient(this)

        istracking.observe(this, Observer {
            updatelocationTracking(it)
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when(it.action){
                ACTION_START_OR_RESUME_SERVICE->{
                    if(isfisrtdrun){
                       startforeGroundServices()
                        isfisrtdrun=false
                    }
                    else {
                        Timber.d("resuming service")
                    }
                }
                ACTION_PAUSE_SERVICE->{
                    Timber.d("pause service")
                }
                ACTION_STOP_SERVICE->{
                    Timber.d("stop service")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    @SuppressLint("MissingPermission")
    private fun updatelocationTracking(isTracking : Boolean){
        if(isTracking){
            if(Trackingutility.haslocationpermission(this)){
                val request = LocationRequest().apply {
                      interval = LOCATION_UPDATE_INTERVAL
                    fastestInterval = FASTEST_LOCATION_INTERVAL
                    priority = PRIORITY_HIGH_ACCURACY
                }
                 fusedLocationProviderClient.requestLocationUpdates(
                     request,
                     locationCallback,
                     Looper.getMainLooper()
                 )
            }
        }
        else{
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }

    }


    val locationCallback = object :LocationCallback(){
        override fun onLocationResult(result: LocationResult?) {
            super.onLocationResult(result)
            if(istracking.value!!){
                result?.locations?.let {locations ->
                    for(location in locations){
                        addPathPoints(location)
                        Timber.d("New Location : ${location.latitude}, ${location.longitude}")
                    }

                }
            }
        }
    }

    private fun addPathPoints(location : Location?){
        location?.let {
            val pos = LatLng(location.latitude,location.longitude)
            pathpoints.value?.apply {
                last().add(pos)
                pathpoints.postValue(this)
            }
        }
    }


    private fun addemptyPolyline()= pathpoints.value?.apply {
        add(mutableListOf())
        pathpoints.postValue(this)
    }?: pathpoints.postValue(mutableListOf(mutableListOf()))


    private fun startforeGroundServices(){

        addemptyPolyline()
        istracking.postValue(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
        as NotificationManager

        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager )
        }
       val notificationBuilder = NotificationCompat.Builder(this , NOTIFICATION_CHANNEL_ID)
           .setAutoCancel(false)
           .setOngoing(true)
           .setSmallIcon(R.drawable.ic_directions_run_black_24dp)
           .setContentTitle("Tracking Your Run")
           .setContentText("00:00:00")
           .setContentIntent(getMainActivityPendingIntent())

        startForeground(NOTIFICATION_ID,notificationBuilder.build())
    }

    private  fun  getMainActivityPendingIntent()=PendingIntent.getActivity(
        this,
        0,
        Intent(this,MainActivity::class.java).also {
            it.action = ACTION_SHOW_TRACKING_FRAGMENT
        },
        FLAG_UPDATE_CURRENT
    )

    @RequiresApi(Build.VERSION_CODES.O)
    private fun  createNotificationChannel(notificationManager: NotificationManager){
         val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID,
             NOTIFICATION_CHANNEL_NAME,
             IMPORTANCE_LOW
             )
        notificationManager.createNotificationChannel(channel)
    }
}