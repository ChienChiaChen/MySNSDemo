
package com.chiachen.mysnsdemo.di

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val snsDispatcher: MySnsDispatchers)

enum class MySnsDispatchers {
    Default,
    IO,
}
