package com.lelestacia.dicodingstoryapp.ui.add_story_activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build.VERSION
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.lelestacia.dicodingstoryapp.R
import com.lelestacia.dicodingstoryapp.databinding.ActivityAddStoryBinding
import com.lelestacia.dicodingstoryapp.ui.camera.CameraActivity
import com.lelestacia.dicodingstoryapp.utility.NetworkResponse
import com.lelestacia.dicodingstoryapp.utility.Utility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.EasyPermissions
import java.io.File

@AndroidEntryPoint
class AddStoryActivity : AppCompatActivity(), View.OnClickListener,
    EasyPermissions.PermissionCallbacks {

    private var _binding: ActivityAddStoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AddStoryViewModel>()
    private var currentFile: File? = null
    private var currentLocation: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.elevation = 0f
        subscribe()
        binding.apply {
            btnUpload.setOnClickListener(this@AddStoryActivity)
            btnGaleri.setOnClickListener(this@AddStoryActivity)
            btnKamera.setOnClickListener(this@AddStoryActivity)
            checkLocation.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    getLocation()
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        if (EasyPermissions.hasPermissions(
                this@AddStoryActivity, Manifest
                    .permission.ACCESS_FINE_LOCATION
            )
        ) {
            val fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(this@AddStoryActivity)
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                if (it != null)
                    currentLocation = LatLng(it.latitude, it.longitude)
            }
        } else {
            EasyPermissions.requestPermissions(
                this@AddStoryActivity,
                resources.getString(R.string.need_location_permission),
                ACCESS_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    private fun subscribe() {
        viewModel.uploadStatus.observe(this) {
            when (it) {
                is NetworkResponse.Loading -> binding.apply {
                    layoutLoading.root.visibility = View.VISIBLE
                    btnUpload.visibility = View.GONE
                }
                else -> binding.apply {
                    layoutLoading.root.visibility = View.GONE
                    btnUpload.visibility = View.VISIBLE
                }
            }
        }

        viewModel.uploadStatus.observe(this) { uploadStatus ->
            when (uploadStatus) {
                is NetworkResponse.GenericException -> Toast
                    .makeText(
                        this,
                        resources.getString(
                            R.string.error_message,
                            uploadStatus.code, uploadStatus.cause
                        ),
                        Toast.LENGTH_SHORT
                    ).show()
                NetworkResponse.NetworkException -> Toast
                    .makeText(
                        this,
                        resources.getString(R.string.error_network),
                        Toast.LENGTH_SHORT
                    ).show()
                is NetworkResponse.Success -> Toast
                    .makeText(this, uploadStatus.data.message, Toast.LENGTH_SHORT).show().also {
                        lifecycleScope.launch(Dispatchers.Main) {
                            delay(1000)
                            finish()
                        }
                    }
                else -> return@observe
            }
        }
    }

    private fun uploadFile() {
        val desc = binding.edtInputDeskripsi.text.toString()
        if (currentLocation != null && currentFile != null && desc.isNotEmpty())
            viewModel.uploadStory(
                file = currentFile as File,
                description = desc,
                lat = (currentLocation as LatLng).latitude.toFloat(),
                lon = (currentLocation as LatLng).longitude.toFloat()
            )
        else if (currentFile != null && desc.isNotEmpty())
            viewModel.uploadStory(
                file = currentFile as File,
                description = desc
            )
        else
            Toast.makeText(this, "PLease complete everything", Toast.LENGTH_SHORT).show()
    }

    private fun startCamera() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
            launcherIntentCameraX.launch(Intent(this, CameraActivity::class.java))
        } else {
            EasyPermissions.requestPermissions(
                this,
                resources.getString(R.string.need_camera_permission),
                CAMERA_CODE,
                Manifest.permission.CAMERA
            )
        }
    }

    private fun startGallery() {
        when {
            VERSION.SDK_INT >= 33 -> {
                if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_MEDIA_IMAGES)) {
                    launcherIntentGallery.launch(
                        Intent.createChooser(
                            Intent(Intent.ACTION_GET_CONTENT).setType("image/*"),
                            resources
                                .getString(R.string.choose_picture)
                        )
                    )
                } else {
                    EasyPermissions.requestPermissions(
                        this,
                        resources.getString(R.string.need_gallery_permission),
                        READ_EXTERNAL,
                        Manifest.permission.READ_MEDIA_IMAGES
                    )
                }
            }
            else -> {
                if (EasyPermissions.hasPermissions(
                        this, Manifest
                            .permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    launcherIntentGallery.launch(
                        Intent.createChooser(
                            Intent(Intent.ACTION_GET_CONTENT).setType("image/*"),
                            resources
                                .getString(R.string.choose_picture)
                        )
                    )
                } else {
                    EasyPermissions.requestPermissions(
                        this,
                        resources.getString(R.string.need_gallery_permission),
                        READ_EXTERNAL,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                }
            }
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = when {
                VERSION.SDK_INT >= 33 -> it.data?.getSerializableExtra(
                    "picture",
                    File::class.java
                ) as File
                else -> it.data?.getSerializableExtra("picture") as File
            }

            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            val result = Utility().rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )
            currentFile = myFile
            binding.previewImageView.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            currentFile = Utility().fileFromUri(selectedImg, this)
            binding.previewImageView.setImageURI(selectedImg)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        when (requestCode) {
            READ_EXTERNAL -> startGallery()
            CAMERA_CODE -> startCamera()
            ACCESS_LOCATION -> getLocation()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        when (requestCode) {
            READ_EXTERNAL -> Toast.makeText(
                this,
                resources.getString(R.string.permission_denied),
                Toast.LENGTH_SHORT
            ).show()

            CAMERA_CODE -> Toast.makeText(
                this,
                resources.getString(R.string.permission_denied),
                Toast.LENGTH_SHORT
            ).show()

            ACCESS_LOCATION -> {
                Toast.makeText(
                    this,
                    resources.getString(R.string.need_location_permission),
                    Toast.LENGTH_SHORT
                ).show()
                binding.checkLocation.isChecked = false
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.btnGaleri.id -> startGallery()
            binding.btnUpload.id -> uploadFile()
            binding.btnKamera.id -> startCamera()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private const val CAMERA_CODE = 100
        private const val READ_EXTERNAL = 300
        private const val ACCESS_LOCATION = 500
    }
}