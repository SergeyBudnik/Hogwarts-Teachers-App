package com.bdev.hengschoolteacher.dao

import android.content.Context
import com.bdev.hengschoolteacher.data.school.lesson.LessonStatus
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LessonStatusModel constructor(
    @SerializedName("lessonsStatuses") val lessonsStatuses: List<LessonStatus>
)

interface LessonStatusDao : CommonDao<LessonStatusModel>

class LessonStatusDaoImpl @Inject constructor(
    @ApplicationContext context: Context
): LessonStatusDao, CommonDaoImpl<LessonStatusModel>(context = context) {
    override fun getFileName() = "lessons-statuses.data"
    override fun newInstance(): LessonStatusModel = LessonStatusModel(emptyList())
    override fun deserialize(json: String): LessonStatusModel = Gson().fromJson(json, LessonStatusModel::class.java)
}
