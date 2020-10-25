package com.example.cbasample.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.lifecycle.ViewModelProvider
import com.example.cbasample.R
import com.example.cbasample.data.model.Atm
import com.example.cbasample.util.hasPermission
import com.example.cbasample.util.requestPermissionWithRationale
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.android.support.DaggerFragment

class FindUsFragment : DaggerFragment(), OnMapReadyCallback,
    ViewTreeObserver.OnGlobalLayoutListener {

    companion object {
        const val REQUEST_FINE_LOCATION_PERMISSIONS_REQUEST_CODE = 37
        const val DEFAULT_DETAIL_ZOOM = 16f
    }

    private var map: GoogleMap? = null
    private var mapView: View? = null
    private var isViewReady = false
    private var isMapReady = false
    private lateinit var atm: Atm

    private lateinit var viewModel: FindUsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.find_us_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(FindUsViewModel::class.java)
        arguments?.let {
            viewModel.atm = FindUsFragmentArgs.fromBundle(it).atm
        }
        atm = viewModel.atm
        initMap()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (!context.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            requestFineLocationPermission()
        }
    }

    private fun addMarker(googleMap: GoogleMap) {
        with(googleMap) {
            if (isViewReady && isMapReady) {
                clear()
                addMarker(
                    MarkerOptions()
                        .position(LatLng(atm.location.lat, atm.location.lng))
                        .title(atm.name)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_atm_commbank))
                )
            }
            animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(atm.location.lat, atm.location.lng),
                    DEFAULT_DETAIL_ZOOM
                )
            )
        }
    }

    private fun initMap() {
        val mapFragment = SupportMapFragment.newInstance()
        childFragmentManager.beginTransaction().replace(R.id.map, mapFragment)
            .commit()
        mapView = mapFragment.view
        if (mapView?.width != 0 && mapView?.height != 0) {
            isViewReady = true
        } else {
            mapView?.viewTreeObserver?.addOnGlobalLayoutListener(this)
        }
        mapFragment.getMapAsync(this)
    }

    private fun requestFineLocationPermission() {
        val permissionApproved =
            context?.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) ?: return

        if (permissionApproved) {
            // not required anything to do
        } else {
            requestPermissionWithRationale(
                Manifest.permission.ACCESS_FINE_LOCATION,
                REQUEST_FINE_LOCATION_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_FINE_LOCATION_PERMISSIONS_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0]
            == PackageManager.PERMISSION_GRANTED
        ) {
            initMap()
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap ?: return
        isMapReady = true
        addMarker(googleMap)
    }

    override fun onGlobalLayout() {
        mapView?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
        isViewReady = true
    }
}