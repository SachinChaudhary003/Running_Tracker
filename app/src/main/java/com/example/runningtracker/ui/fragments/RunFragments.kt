package com.example.runningtracker.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.runningtracker.R
import com.example.runningtracker.other.Constants.REQUEST_CODE_LOCATION_PERMISSION
import com.example.runningtracker.other.Trackingutility
import com.example.runningtracker.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_run.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions


@AndroidEntryPoint
class RunFragments : Fragment(R.layout.fragment_run) ,EasyPermissions.PermissionCallbacks {

    private val  viewModel : MainViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermission()
        fab.setOnClickListener {
            findNavController().navigate(R.id.action_runFragments_to_trackingFragment2)
        }
    }
    private fun requestPermission(){
        if(Trackingutility.haslocationpermission(requireContext())){
            return
        }
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.Q){
            EasyPermissions.requestPermissions(
                this,
                "Location Permission is needed to run app",
                REQUEST_CODE_LOCATION_PERMISSION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
        else{
            EasyPermissions.requestPermissions(
                this,
                "Location Permission is needed to run app",
                REQUEST_CODE_LOCATION_PERMISSION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
            AppSettingsDialog.Builder(this).build().show()
        }
        else{
            requestPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this)
    }
}