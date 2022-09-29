package com.lelestacia.dicodingstoryapp.ui.main_activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.lelestacia.dicodingstoryapp.R
import com.lelestacia.dicodingstoryapp.utility.NetworkResponse


class MapsFragment : Fragment() {

    private val currentLocation = MutableLiveData<LatLng>()

    private val callback = OnMapReadyCallback { googleMap ->
        viewModel.storiesWithLocation.observe(viewLifecycleOwner) { it ->
            when (it) {
                is NetworkResponse.GenericException -> viewModel.getAllStoriesWithLocation()
                NetworkResponse.NetworkException -> viewModel.getAllStoriesWithLocation()
                is NetworkResponse.Success -> {
                    with(it.data.listNetworkStory) {
                        this.forEach { story ->
                            googleMap.addMarker(MarkerOptions()
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
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_DENIED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    @SuppressLint("MissingPermission")
    val requestPermissionLauncher =  registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            val locationService = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager?
            locationService!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0F
            ) {  location ->
                Log.d(TAG, "onRequestPermissionsResult: Current Location is ${location.latitude} , ${location.longitude}")
                currentLocation.value = LatLng(location.latitude, location.longitude)
            }
        } else {
            Toast.makeText(requireContext(), "Silahkan berikan izin", Toast.LENGTH_SHORT).show()
            currentLocation.value = LatLng(-1.6149644885566579, 115.49701862707302)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ACCESS_LOCATION) {
            Toast.makeText(requireContext(), "Izin berhasil didapatkan", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Izin gagal didapatkan", Toast.LENGTH_SHORT).show()
            val locationService = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager?
            locationService!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0F
            ) {  location ->
                Log.d(TAG, "onRequestPermissionsResult: Current Location is ${location.latitude} , ${location.longitude}")
                currentLocation.postValue(LatLng(location.latitude, location.longitude))
            }
        }
    }

    companion object {
        private const val ACCESS_LOCATION = 500
    }
}