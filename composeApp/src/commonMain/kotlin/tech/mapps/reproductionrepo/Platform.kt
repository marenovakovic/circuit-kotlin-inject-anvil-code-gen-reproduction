package tech.mapps.reproductionrepo

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform