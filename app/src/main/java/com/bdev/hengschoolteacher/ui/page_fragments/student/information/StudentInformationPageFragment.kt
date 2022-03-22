package com.bdev.hengschoolteacher.ui.page_fragments.student.information

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.DayOfWeek
import com.bdev.hengschoolteacher.data.school.group.GroupAndLesson
import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.interactors.lessons.LessonsInteractor
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import com.bdev.hengschoolteacher.ui.adapters.BaseWeekItemsListAdapter
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import com.bdev.hengschoolteacher.ui.views.app.AppLayoutView
import com.bdev.hengschoolteacher.ui.views.app.student.StudentHeaderItem
import com.bdev.hengschoolteacher.ui.views.branded.BrandedPhoneView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_student_information.*
import kotlinx.android.synthetic.main.view_list_item_student_information_timetable.view.*
import java.util.*
import javax.inject.Inject

class StudentInformationTimetableListItemView : LinearLayout {
    init {
        View.inflate(context, R.layout.view_list_item_student_information_timetable, this)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(groupAndLesson: GroupAndLesson): StudentInformationTimetableListItemView {
        val startTimeName = resources.getString(groupAndLesson.lesson.startTime.translationId)
        val finishTimeName = resources.getString(groupAndLesson.lesson.finishTime.translationId)

        studentInformationListItemTimeView.text = "$startTimeName - $finishTimeName"

        return this
    }
}

class StudentInformationTimetableListAdapter(context: Context) : BaseWeekItemsListAdapter<GroupAndLesson>(context) {
    override fun getElementView(item: GroupAndLesson, convertView: View?): View {
        return if (convertView == null || convertView !is StudentInformationTimetableListItemView) {
            StudentInformationTimetableListItemView(context)
        } else {
            convertView
        }.bind(item)
    }

    override fun getElementDayOfWeek(item: GroupAndLesson): DayOfWeek {
        return item.lesson.day
    }

    override fun getElementComparator(): Comparator<GroupAndLesson> {
        return GroupAndLesson.getComparator(Calendar.getInstance())
    }
}

@AndroidEntryPoint
class StudentInformationPageFragment : BasePageFragment<StudentInformationPageFragmentViewModel>() {
    companion object {
        const val EXTRA_STUDENT_LOGIN = "EXTRA_STUDENT_LOGIN"
    }

    lateinit var studentLogin: String

    @Inject lateinit var studentsStorageInteractor: StudentsStorageInteractor
    @Inject lateinit var lessonsService: LessonsInteractor

    override fun provideViewModel(): StudentInformationPageFragmentViewModel =
        ViewModelProvider(this).get(StudentInformationPageFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.activity_student_information, container, false)

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        // studentLogin = intent.getStringExtra(EXTRA_STUDENT_LOGIN)!! todo

        val student = studentsStorageInteractor.getByLogin(studentLogin) ?: throw RuntimeException()

        studentInformationHeaderView
                .setTitle("Студент. ${student.person.name}")
                .setLeftButtonAction { doFinish() }

        studentInformationSecondaryHeaderView.bind(
                item = StudentHeaderItem.DETAILS,
                studentLogin = studentLogin
        )

        initPhonesList(student)

        initList(student)
    }

    private fun initPhonesList(student: Student) {
        student.person.contacts.phones.forEach {
            studentInformationPhonesLayoutView.addView(BrandedPhoneView(requireContext()).bind(it))
        }
    }

    private fun initList(student: Student) {
        val currentTime = Date().time

        val adapter = StudentInformationTimetableListAdapter(requireContext())

        adapter.setItems(lessonsService
                .getStudentLessons(student.login)
                .filter { it.lesson.creationTime <= currentTime }
                .filter { currentTime <= it.lesson.deactivationTime }
        )

        studentInformationTimetableListView.adapter = adapter
    }

    private fun doFinish() {
//        finish()
//        overridePendingTransition(R.anim.slide_close_enter, R.anim.slide_close_exit)
    }

    override fun getAppLayoutView(): AppLayoutView? {
        return null
    }
}
