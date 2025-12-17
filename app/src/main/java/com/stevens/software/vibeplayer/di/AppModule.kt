package com.stevens.software.vibeplayer.di

import com.stevens.software.vibeplayer.media.MediaProviderImpl
import com.stevens.software.vibeplayer.VibePlayerViewModel
import com.stevens.software.vibeplayer.media.MediaProvider
import com.stevens.software.vibeplayer.media.MediaRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::VibePlayerViewModel)
    factoryOf(::MediaProviderImpl) bind MediaProvider::class
    factoryOf(::MediaRepository)
}