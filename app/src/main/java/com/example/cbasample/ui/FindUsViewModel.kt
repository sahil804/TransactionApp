package com.example.cbasample.ui

import androidx.lifecycle.ViewModel
import com.example.cbasample.data.model.Atm
import javax.inject.Inject

class FindUsViewModel @Inject constructor() : ViewModel() {
    lateinit var atm: Atm
}