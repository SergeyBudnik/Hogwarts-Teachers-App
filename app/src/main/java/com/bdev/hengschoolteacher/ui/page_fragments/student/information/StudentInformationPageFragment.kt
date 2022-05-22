package com.bdev.hengschoolteacher.ui.page_fragments.student.information

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.data.school.DayOfWeek
import com.bdev.hengschoolteacher.data.school.group.GroupAndLesson
import com.bdev.hengschoolteacher.data.school.student.Student
import com.bdev.hengschoolteacher.interactors.lessons.LessonsInteractor
import com.bdev.hengschoolteacher.interactors.students.StudentsStorageInteractor
import com.bdev.hengschoolteacher.ui.adapters.BaseWeekItemsListAdapter
import com.bdev.hengschoolteacher.ui.fragments.student.header.StudentHeaderFragment
import com.bdev.hengschoolteacher.ui.fragments.student.header.data.StudentHeaderFragmentItem
import com.bdev.hengschoolteacher.ui.page_fragments.BasePageFragment
import com.bdev.hengschoolteacher.ui.views.branded.BrandedPhoneView
import com.bdev.hengschoolteacher.utils.CalendarUtils
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
        return GroupAndLesson.getComparator(CalendarUtils.getInstance())
    }
}

@AndroidEntryPoint
class StudentInformationPageFragment : BasePageFragment<StudentInformationPageFragmentViewModel>() {
    private val args: StudentInformationPageFragmentArgs by navArgs()

    @Inject lateinit var studentsStorageInteractor: StudentsStorageInteractor
    @Inject lateinit var lessonsService: LessonsInteractor

    override fun provideViewModel(): StudentInformationPageFragmentViewModel =
        ViewModelProvider(this).get(StudentInformationPageFragmentViewModelImpl::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.activity_student_information, container, false)

    override fun doOnViewCreated() {
        super.doOnViewCreated()

        fragmentViewModel.getDataLiveData().observe(this) { data ->
            studentInformationHeaderView
                .setTitle("Студент. ${data.student.person.name}")
                .setLeftButtonAction { fragmentViewModel.goBack() }
            
            initPhonesList(data.student)

            initList(data.student)
        }

        getSecondaryHeaderFragment().init(
            login = args.args.login,
            item = StudentHeaderFragmentItem.DETAILS
        )

        fragmentViewModel.init(login = args.args.login)
    }

    private fun initPhonesList(student: Student) {
        student.person.contacts.phones.forEach {
            studentInformationPhonesLayoutView.addView(BrandedPhoneView(requireContext()).bind(it))
        }
    }

    private fun initList(student: Student) {
        val currentTime = Date().time

        val adapter = StudentInformationTimetableListAdapter(requireContext())

        adapter.setItems(
            items = lessonsService
                .getStudentLessons(student.login)
                .filter { it.lesson.creationTime <= currentTime }
                .filter { currentTime <= it.lesson.deactivationTime }
        )

        studentInformationTimetableListView.adapter = adapter
    }

    private fun getSecondaryHeaderFragment(): StudentHeaderFragment =
        childFragmentManager.findFragmentById(
            R.id.studentInformationSecondaryHeaderFragment
        ) as StudentHeaderFragment
}
