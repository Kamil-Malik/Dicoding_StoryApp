package com.lelestacia.dicodingstoryapp.ui.add_story_activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.lelestacia.dicodingstoryapp.R
import com.lelestacia.dicodingstoryapp.databinding.ActivityAddStoryBinding
import com.lelestacia.dicodingstoryapp.ui.camera.CameraActivity
import com.lelestacia.dicodingstoryapp.utility.NetworkResponse
import com.lelestacia.dicodingstoryapp.utility.Utility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class AddStoryActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityAddStoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AddStoryViewModel>()
    private var currentFile: File? = null

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
        if (currentFile != null &&
            binding.edtInputDeskripsi.text.toString().isNotEmpty()
        ) {
            viewModel.uploadPhoto(
                currentFile as File,
                binding.edtInputDeskripsi.text.toString()
            )
        } else if (binding.edtInputDeskripsi.text.toString().isEmpty()) {
            binding.edtInputDeskripsi.error = resources.getString(R.string.empty_description)
        } else {
            Toast.makeText(
                this, resources.getString(R.string.empty_picture),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun startCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            launcherIntentCameraX.launch(Intent(this, CameraActivity::class.java))
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_CODE)
        }
    }

    private fun startGallery() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED
        ) {
            launcherIntentGallery.launch(
                Intent.createChooser(
                    Intent(Intent.ACTION_GET_CONTENT).setType("image/*"),
                    resources
                        .getString(R.string.choose_picture)
                )
            )
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), READ_EXTERNAL)
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = if (VERSION.SDK_INT >= 33) {
                it.data?.getSerializableExtra("picture", File::class.java) as File
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra("picture") as File
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(
                this,
                resources.getString(R.string.permission_granted),
                Toast.LENGTH_SHORT
            ).show()
            startCamera()
        } else if (requestCode == CAMERA_CODE && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(
                this,
                resources.getString(R.string.permission_denied),
                Toast.LENGTH_SHORT
            ).show()
        } else if (requestCode == READ_EXTERNAL && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            launcherIntentGallery.launch(
                Intent.createChooser(
                    Intent(Intent.ACTION_GET_CONTENT).setType("image/*"),
                    resources
                        .getString(R.string.choose_picture)
                )
            )
        } else if (requestCode == READ_EXTERNAL && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(
                this,
                resources.getString(R.string.permission_denied),
                Toast.LENGTH_SHORT
            ).show()
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
    }
}