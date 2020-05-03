package com.ta.dodo.ui.main.scan

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import com.ta.dodo.R
import com.ta.dodo.databinding.ScanFragmentBinding
import mu.KotlinLogging


private val logger = KotlinLogging.logger {}

class ScanFragment : Fragment() {

    companion object {
        fun newInstance() = ScanFragment()
    }

    private val REQUEST_CAMERA = 1

    private val scanViewModel: ScanViewModel by lazy {
        ViewModelProvider(this).get(ScanViewModel::class.java)
    }

    private lateinit var qrCodeReader: QRCodeReaderView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ScanFragmentBinding.inflate(inflater).apply {
            viewModel = scanViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        qrCodeReader = view.findViewById(R.id.scan_qr_decoder)
        qrCodeReader.setOnQRCodeReadListener(scanViewModel)
        qrCodeReader.setQRDecodingEnabled(true)

        requestCameraPermission()
    }

    private fun requestCameraPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA)
        } else {
            qrCodeReader.setBackCamera()
            logger.info { "Permission granted" }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA) {
            qrCodeReader.setBackCamera()
            logger.info { "Permission granted" }
        }
    }
}
