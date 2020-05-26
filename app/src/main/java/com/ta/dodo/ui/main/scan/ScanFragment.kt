package com.ta.dodo.ui.main.scan

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import com.ta.dodo.R
import com.ta.dodo.databinding.ScanFragmentBinding
import com.ta.dodo.model.wallet.Payment
import mu.KotlinLogging


private val logger = KotlinLogging.logger {}

class ScanFragment : Fragment() {

    companion object {
        private const val REQUEST_CAMERA = 1
        fun newInstance() = ScanFragment()
    }

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

    override fun onStart() {
        super.onStart()
        setOnPaymentReadListener()
    }

    private fun setOnPaymentReadListener() {
        scanViewModel.payment.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                navigateToPayment(it)
            }
        })
    }

    private fun navigateToPayment(payment: Payment) {
        val action = ScanFragmentDirections.actionScanFragmentToPaymentFragment(
            payment = payment
        )
        findNavController().navigate(action)
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
            qrCodeReader.visibility = View.VISIBLE
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
            qrCodeReader.visibility = View.VISIBLE
        }
    }
}
