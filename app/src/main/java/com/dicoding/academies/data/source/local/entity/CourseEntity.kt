package com.dicoding.academies.data.source.local.entity

data class CourseEntity(
    var courseId: String,
    var title: String,
    var description: String,
    var deadline: String,
    var bookmarked: Boolean,
    var imagePath: String
)
