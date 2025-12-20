package com.stevens.software.vibeplayer.di

import com.stevens.software.vibeplayer.media.MediaProviderImpl
import com.stevens.software.vibeplayer.VibePlayerViewModel
import com.stevens.software.vibeplayer.core.AudioFileRepository
import com.stevens.software.vibeplayer.media.MediaService
import com.stevens.software.vibeplayer.media.MediaProvider
import com.stevens.software.vibeplayer.media.MediaRepository
import com.stevens.software.vibeplayer.media.PlaybackManager
import com.stevens.software.vibeplayer.player.PlayerViewModel
import com.stevens.software.vibeplayer.scan.ScanViewModel
import com.stevens.software.vibeplayer.search.SearchViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::VibePlayerViewModel)
    viewModelOf(::PlayerViewModel)
    viewModelOf(::ScanViewModel)
    viewModelOf(::SearchViewModel)
    factoryOf(::MediaProviderImpl) bind MediaProvider::class
    factoryOf(::MediaRepository)
    factoryOf(::MediaService)
    factoryOf(::AudioFileRepository)
    singleOf(::PlaybackManager)
}