package com.example.runningtracker.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.runningtracker.R
import com.example.runningtracker.other.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.runningtracker.other.Constants.POLYLINE_COLOR
import com.example.runningtracker.other.Constants.POLYLINE_WIDTH
import com.example.runningtracker.services.TrackingService
import com.example.runningtracker.ui.viewmodels.MainViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_tracking.*

@AndroidEntryPoint
class TrackingFragment : Fragment(R.layout.fragment_tracking) {

    private val  viewModel : MainViewModel by viewModels()



    private var map : GoogleMap?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)
          btnToggleRun.setOnClickListener{
              sendCommandtoService(ACTION_START_OR_RESUME_SERVICE)
          }
        mapView.getMapAsync {
            map =it
        }
    }





     private fun sendCommandtoService(action : String)=Intent(requireContext(),TrackingService::class.java).also {
         it.action=action
         requireContext().startService(it)
     }
    override fun onResume() {
        super.onResume()
          mapView?.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }
}