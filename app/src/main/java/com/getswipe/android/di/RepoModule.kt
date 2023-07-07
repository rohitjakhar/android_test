package com.getswipe.android.di

import com.getswipe.android.data.repo.GetSwipeRepoImpl
import com.getswipe.android.domain.repo.GetSwipeRepo
import org.koin.dsl.module

val repoModule = module {
    single<GetSwipeRepo> {
        return@single GetSwipeRepoImpl(get())
    }
}
