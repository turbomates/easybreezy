package io.easybreezy.user.model

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class Name(
    @Column(name = "first_name")
    private val firstName: String?,

    @Column(name = "last_name")
    private val lastName: String?
)
