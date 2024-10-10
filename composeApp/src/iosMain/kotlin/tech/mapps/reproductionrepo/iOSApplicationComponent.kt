package tech.mapps.reproductionrepo

import me.tatarka.inject.annotations.Component
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.MergeComponent
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Component
@MergeComponent(AppScope::class)
@SingleIn(AppScope::class)
interface iOSApplicationComponent : ApplicationComponent, iOSApplicationComponentMerged {
    companion object
}

//@KmpComponentCreate
//expect fun ApplicationComponent.Companion.create(): ApplicationComponent