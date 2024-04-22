package io.github.asvid.mvvmexample.domain.items

import java.util.UUID

data class Item(
    val value: Int,
    val id: UUID = UUID.randomUUID()
)