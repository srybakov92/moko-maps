/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.app

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.SupportMapFragment
import com.icerockdev.app.databinding.ActivityMainBinding
import com.icerockdev.library.TrackerViewModel
import dev.icerock.moko.geo.LocationTracker
import dev.icerock.moko.maps.google.GoogleMapController
import dev.icerock.moko.mvvm.MvvmActivity
import dev.icerock.moko.mvvm.createViewModelFactory
import dev.icerock.moko.permissions.PermissionsController


class MainActivity : MvvmActivity<ActivityMainBinding, TrackerViewModel>() {
    override val layoutId: Int = R.layout.activity_main
    override val viewModelVariableId: Int = BR.viewModel
    override val viewModelClass: Class<TrackerViewModel> = TrackerViewModel::class.java

    override fun viewModelFactory(): ViewModelProvider.Factory {
        return createViewModelFactory {
            TrackerViewModel(
                locationTracker = LocationTracker(
                    permissionsController = PermissionsController(
                        applicationContext = applicationContext
                    )
                ),
                mapsController = GoogleMapController(
                    geoApiKey = "AIzaSyAQI7utH24gJIetY5atclseaZTc14ugVP8"
                )
            ).apply { start() }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.locationTracker.bind(lifecycle, this, supportFragmentManager)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync {
            viewModel.mapsController.bind(
                lifecycle = lifecycle,
                googleMap = it,
                context = this
            )
        }
    }
}