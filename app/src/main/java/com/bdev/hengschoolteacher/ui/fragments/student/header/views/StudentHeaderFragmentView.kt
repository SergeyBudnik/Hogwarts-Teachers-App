package com.bdev.hengschoolteacher.ui.fragments.student.header.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.bdev.hengschoolteacher.NavGraphDirections
import com.bdev.hengschoolteacher.R
import com.bdev.hengschoolteacher.ui.fragments.profile.header.ProfileHeaderFragmentData
import com.bdev.hengschoolteacher.ui.fragments.student.header.StudentHeaderFragmentData
import com.bdev.hengschoolteacher.ui.fragments.student.header.data.StudentHeaderFragmentItem
import com.bdev.hengschoolteacher.ui.navigation.NavCommand
import com.bdev.hengschoolteacher.ui.page_fragments.student.information.StudentInformationPageFragmentArguments
import com.bdev.hengschoolteacher.ui.page_fragments.student.payments.StudentPaymentsPageFragmentArguments
import com.bdev.hengschoolteacher.ui.page_fragments.student.statistics.StudentStatisticsPageFragmentArguments
import kotlinx.android.synthetic.main.view_student_header.view.*

class StudentHeaderView : LinearLayout {
    init {
        View.inflate(context, R.layout.view_student_header, this)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun bind(
        data: StudentHeaderFragmentData,
        navCommandHandler: (NavCommand) -> Unit
    ) {
        studentHeaderDetailsView.bind(
                active = data.item == StudentHeaderFragmentItem.DETAILS,
                hasAlert = false,
                clickAction = {
                    navCommandHandler(
                        NavCommand.replace(
                            navDir = NavGraphDirections.studentHeaderToStudentInformationAction(
                                args = StudentInformationPageFragmentArguments(
                                    login = data.login
                                )
                            )
                        )
                    )
                }
        )

        studentHeaderAttendanceView.bind(
                active = data.item == StudentHeaderFragmentItem.ATTENDANCE,
                hasAlert = false,
                clickAction = {
                    navCommandHandler(
                        NavCommand.replace(
                            navDir = NavGraphDirections.studentHeaderToStudentStatisticsAction(
                                args = StudentStatisticsPageFragmentArguments(
                                    login = data.login
                                )
                            )
                        )
                    )
                }
        )

        studentHeaderPaymentsView.bind(
                active = data.item == StudentHeaderFragmentItem.PAYMENTS,
                hasAlert = false,
                clickAction = {
                    navCommandHandler(
                        NavCommand.replace(
                            navDir = NavGraphDirections.studentHeaderToStudentPaymentsAction(
                                args = StudentPaymentsPageFragmentArguments(
                                    login = data.login
                                )
                            )
                        )
                    )
                }
        )
    }
}