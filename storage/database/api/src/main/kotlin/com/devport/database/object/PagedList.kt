package com.devport.database.`object`

import org.hibernate.query.Page

data class PagedList<E>(
    val page: Page,
    val list: List<E>
)