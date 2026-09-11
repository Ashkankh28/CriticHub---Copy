package com.example.critichub.viewmodel

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.critichub.CriticHubApplication
import com.example.critichub.di.AppContainer

/** دسترسی ساده به ظرف وابستگی‌ها از داخل initializer های ViewModel. */
internal val CreationExtras.appContainer: AppContainer
    get() = (this[APPLICATION_KEY] as CriticHubApplication).container
