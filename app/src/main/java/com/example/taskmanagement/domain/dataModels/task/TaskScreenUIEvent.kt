package com.example.taskmanagement.domain.dataModels.task

import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUserDto

sealed class TaskScreenUIEvent {
    object StatusChanged : TaskScreenUIEvent()
    class TaskItemToggle(val taskItem: TaskItem) : TaskScreenUIEvent()
    class MembersRemove(val activeUserDto: ActiveUserDto) : TaskScreenUIEvent()
    sealed class Comments(val comment: Comment) : TaskScreenUIEvent() {
        class Edit(comment: Comment) : Comments(comment)
        class Add(comment: Comment) : Comments(comment)
        class Remove(comment: Comment) : Comments(comment)
    }

}