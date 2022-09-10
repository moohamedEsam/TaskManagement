package com.example.taskmanagement.domain.dataModels.task

import com.example.taskmanagement.domain.dataModels.activeUser.ActiveUserDto

sealed class TaskScreenUIEvent {
    object StatusChanged : TaskScreenUIEvent()
    sealed class TaskItems(val taskItem: TaskItem) : TaskScreenUIEvent() {
        class Add(taskItem: TaskItem) : TaskItems(taskItem)
        class Edit(taskItem: TaskItem, val oldTaskItem: TaskItem) : TaskItems(taskItem)
        class Remove(taskItem: TaskItem) : TaskItems(taskItem)
    }

    class MembersRemove(val activeUserDto: ActiveUserDto) : TaskScreenUIEvent()

    sealed class Comments(val comment: CommentView) : TaskScreenUIEvent() {
        class Edit(comment: CommentView, val oldComment: CommentView) : Comments(comment)
        class Add(comment: CommentView) : Comments(comment)
        class Remove(comment: CommentView) : Comments(comment)
    }

}