package com.ta.dodo.ui.verification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class IdentityViewModel : ViewModel() {

    val fullName = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val phoneNumber = MutableLiveData<String>()
}
