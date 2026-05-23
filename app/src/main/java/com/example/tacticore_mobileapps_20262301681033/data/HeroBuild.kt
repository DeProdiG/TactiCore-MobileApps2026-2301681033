package com.example.tacticore.data

data class HeroBuild(
    val id: Long = 0,                 // уникален идентификатор (автоматично генериран)
    val heroId: Int,                  // ID на героя (свързва се с Hero.id)
    val userNotes: String,            // потребителски бележки
    val rating: Int,                  // оценка 1..5
    val timestamp: Long = System.currentTimeMillis()  // време на създаване/редактиране
)