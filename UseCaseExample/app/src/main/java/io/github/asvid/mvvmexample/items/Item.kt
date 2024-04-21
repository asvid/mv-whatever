package io.github.asvid.mvvmexample.items

import java.util.UUID

data class Item(
    val value: Int,
    val id: UUID = UUID.randomUUID()
)