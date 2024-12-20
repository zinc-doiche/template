package com.devport.database.domain.auditable

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.time.LocalDateTime

@Embeddable
data class Timestamp(
    @Column(nullable = false)
    val createdDateTime: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    val updatedDateTime: LocalDateTime = LocalDateTime.now()
)