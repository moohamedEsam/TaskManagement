package com.example.taskmanagement.domain.dataModels.utils

enum class ParentRoute {
    Teams,
    Projects,
    Tasks;

    companion object {
        fun fromString(value: String) = when (value) {
            "teams" -> Teams
            "projects" -> Projects
            "tasks" -> Tasks
            else -> throw Exception("value not recognized")
        }
    }

    override fun toString(): String {
        return when (this) {
            Teams -> "teams"
            Projects -> "projects"
            else -> "tasks"
        }
    }
}