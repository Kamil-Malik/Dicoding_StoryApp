package com.lelestacia.dicodingstoryapp.ui.main_activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.lelestacia.dicodingstoryapp.R
import com.lelestacia.dicodingstoryapp.utility.NetworkResponse
import pub.devrel.easypermissions.EasyPermissions


class MapsFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private val currentLocation =
        MutableLiveData<LatLng>()

    private val callback = OnMapReadyCallback { googleMap ->

        googleMap.apply {
            uiSettings.isZoomControlsEnabled = true
            setMinZoomPreference(8F)
            mapType = GoogleMap.MAP_TYPE_TERRAIN
        }


        viewModel.storiesWithLocation.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResponse.GenericException -> viewModel.getAllStoriesWithLocation()
                NetworkResponse.NetworkException -> viewModel.getAllStoriesWithLocation()
                is NetworkResponse.Success -> {
                    with(it.data.listNetworkStory) {
                        this.forEach { story ->
                            googleMap.addMarker(
                                MarkerOptions()
                                    .position(LatLng(story.lat, story.lon))
                                    .title(story.name)
                            )
                        }
                    }
                }
                else -> return@observe
            }
        }

        currentLocation.observe(this) {
            if (it != null)
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(it))
        }
    }

    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun checkPermission() {
        if (EasyPermissions.hasPermissions(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            setLocation()
        } else {
            EasyPermissions.requestPermissions(
                this,
                resources.getString(R.string.need_location_permission),
                ACCESS_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (requestCode == ACCESS_LOCATION)
            setLocation()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (requestCode == ACCESS_LOCATION)
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.need_location_permission),
                Toast.LENGTH_SHORT
            ).show()
    }

    @SuppressLint("MissingPermission")
    private fun setLocation() {
        try {
            val fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(requireActivity())
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                if (it == null) {
                    Log.w(TAG, "setLocation: Failed to get location")
                    setLocation()
                } else
                    Log.d(TAG, "setLocation: ${it.latitude} , ${it.longitude}")
                currentLocation.value = LatLng(it.latitude, it.longitude)
            }.addOnFailureListener {
                Log.e(TAG, "setLocation: ${it.message}")
                setLocation()
            }
        } catch (e: Exception) {
            Log.e(TAG, "setLocation: ${e.message}")
        }
    }

    companion object {
        private const val ACCESS_LOCATION = 500
    }
}