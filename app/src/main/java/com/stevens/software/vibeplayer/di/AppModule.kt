package com.stevens.software.vibeplayer.di

import com.stevens.software.vibeplayer.VibePlayerViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::VibePlayerViewModel)
}