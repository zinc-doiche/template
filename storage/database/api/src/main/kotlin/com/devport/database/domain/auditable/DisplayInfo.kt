package com.devport.database.domain.auditable

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class DisplayInfo(
    @Column(nullable = false)
    val displayName: String,

    @Column(nullable = false)
    val material: String,

    @Column(nullable = true)
    val lore: String,

    @Column(nullable = true)
    val tags: String
)