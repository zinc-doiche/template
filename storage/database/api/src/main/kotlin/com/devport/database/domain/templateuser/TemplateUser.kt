package com.devport.database.domain.templateuser

import com.devport.database.domain.auditable.Timestamp
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "TBL_TEMPLATE_USER")
class TemplateUser(
    username: String,

    @Column(unique = true, nullable = false)
    val uuid: UUID,

    @Embedded val timestamp: Timestamp = Timestamp()
) {
    @Id
    @GeneratedValue
    val id: Long = 0

    @Column(unique = true, nullable = false)
    var username: String = username
        protected set

    override fun toString(): String {
        return "TemplateUser(uuid=$uuid, timestamp=$timestamp, id=$id, username='$username')"
    }
}