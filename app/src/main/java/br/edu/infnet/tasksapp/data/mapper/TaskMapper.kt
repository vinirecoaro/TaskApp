package br.edu.infnet.tasksapp.data.mapper

import br.edu.infnet.tasksapp.data.model.TaskEntity
import br.edu.infnet.tasksapp.domain.model.TaskDomain

fun TaskDomain.toEntity() = TaskEntity(
    id = id,
    title = title,
    description = description,
    userId = userId,
    expirationDate = expirationDate
)

fun TaskEntity.toDomain() = TaskDomain(
    id = id,
    title = title,
    description = description,
    userId = userId,
    expirationDate = expirationDate
)