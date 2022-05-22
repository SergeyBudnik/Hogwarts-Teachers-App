package com.bdev.hengschoolteacher.ui.page_fragments.lesson.status

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.bdev.hengschoolteacher.data.common.MutableLiveDataWithState
import com.bdev.hengschoolteacher.data.school.lesson.LessonStatus
import com.bdev.hengschoolteacher.data.school.lesson.LessonStatusType
import com.bdev.hengschoolteacher.interactors.lessons.LessonsInteractor
import com.bdev.hengschoolteacher.interactors.lessons_status.LessonsStatusLoadingInteractor
import com.bdev.hengschoolteacher.interactors.lessons_status.LessonsStatusStorageInteractor
import com.bdev.hengschoolteacher.interactors.staff_members.StaffMembersStorageInteractor
import com.bdev.hengschoolteacher.interactors.teachers.TeacherInfoInteractor
import com.bdev.hengschoolteacher.ui.navigation.NavCommand
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModel
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragmentViewModelImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

interface LessonStatusPageFragmentViewModel : BasePageFragmentViewModel {
    fun getDataLiveData(): LiveData<LessonStatusPageFragmentData>

    fun markStatus(statusType: LessonStatusType)
}

@HiltViewModel
class LessonStatusPageFragmentViewModelImpl @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val lessonsService: LessonsInteractor,
    private val lessonsStatusStorageInteractor: LessonsStatusStorageInteractor,
    private val lessonsStatusLoadingInteractor: LessonsStatusLoadingInteractor,
    private val staffMembersStorageInteractor: StaffMembersStorageInteractor,
    private val teacherInfoInteractor: TeacherInfoInteractor,
) : LessonStatusPageFragmentViewModel, BasePageFragmentViewModelImpl() {
    private val dataLiveData =
        savedStateHandle.get<LessonStatusPageFragmentArguments>("args")?.let { args ->
            val lesson = lessonsService.getLesson(args.lessonId)?.lesson ?: throw RuntimeException()
            val teacher = staffMembersStorageInteractor.getStaffMember(lesson.teacherLogin) ?: throw RuntimeException()

            val status = lessonsStatusStorageInteractor.getLessonStatus(
                args.lessonId,
                lessonsService.getLessonStartTime(
                    args.lessonId,
                    args.weekIndex
                )
            )

            MutableLiveDataWithState(
                initialValue = LessonStatusPageFragmentData(
                    lesson = lesson,
                    lessonStartTime = lessonsService.getLessonStartTime(args.lessonId, args.weekIndex),
                    teacher = teacher,
                    teacherName = teacherInfoInteractor.getTeachersName(teacher),
                    teacherSurname = teacherInfoInteractor.getTeachersSurname(teacher),
                    actualStatusType = status?.type,
                    progressStatusType = null
                )
            )
        } ?: throw RuntimeException()

    override fun getDataLiveData() = dataLiveData.getLiveData()

    override fun markStatus(statusType: LessonStatusType) {
        val value = dataLiveData.getValue()

        dataLiveData.setValue(mutator = { oldValue ->
            oldValue.copy(progressStatusType = statusType)
        })

        lessonsStatusLoadingInteractor.addLessonStatus(
            LessonStatus(
                id = null,
                lessonId = value.lesson.id,
                type = statusType,
                actionTime = value.lessonStartTime,
                creationTime = Date().time
            )
        ).onSuccess {
            dataLiveData.postValue(mutator = { oldValue ->
                oldValue.copy(
                    actualStatusType = statusType,
                    progressStatusType = null
                )
            })

            doGoBack()
        }.onAuthFail {
            /* ToDo */
        }.onOtherFail {
            /* ToDo */
        }
    }

    override fun goBack() { doGoBack() }

    private fun doGoBack() { navigate(navCommand = NavCommand.back()) }
}