package com.autospare.viewmodel

import com.autospare.service.LogService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author: Hari K
 * Date: 21/11/2023.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    logService: LogService,
) : AutoSpareViewModel(logService) {

}