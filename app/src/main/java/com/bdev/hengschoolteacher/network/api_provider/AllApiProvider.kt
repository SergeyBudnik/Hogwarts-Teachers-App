package com.bdev.hengschoolteacher.network.api_provider

import android.annotation.SuppressLint
import com.bdev.hengschoolteacher.interactors.auth.AuthStorageInteractorImpl
import com.bdev.hengschoolteacher.network.api.auth.AuthApi
import com.bdev.hengschoolteacher.network.api.groups.GroupsApi
import com.bdev.hengschoolteacher.network.api.lessons_status.LessonsStatusApi
import com.bdev.hengschoolteacher.network.api.staff_members.StaffMembersApi
import com.bdev.hengschoolteacher.network.api.students.StudentsApi
import com.bdev.hengschoolteacher.network.api.students_attendances.StudentsAttendancesApi
import com.bdev.hengschoolteacher.network.api.students_payments.StudentsPaymentsApi
import okhttp3.OkHttpClient
import okhttp3.Request
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

interface AllApiProvider {
    fun provideAuthApi(): AuthApi
    fun provideGroupsApi(): GroupsApi
    fun provideLessonsStatusApi(): LessonsStatusApi
    fun provideStaffMembersApi(): StaffMembersApi
    fun provideStudentsApi(): StudentsApi
    fun provideStudentsAttendancesApi(): StudentsAttendancesApi
    fun provideStudentsPaymentsApi(): StudentsPaymentsApi
}

@EBean(scope = EBean.Scope.Singleton)
open class AllApiProviderImpl : AllApiProvider {
    @Bean
    lateinit var authService: AuthStorageInteractorImpl

    private val sslSocketFactory = getSslSocketFactory()
    private val sslTrustManager = getSslTrustManager()
    private val hostnameVerifier = getHostnameVerifier()

    private val publicOkHttpClient = getOkHttpClient(requiresAuth = false)
    private val protectedOkHttpClient = getOkHttpClient(requiresAuth = true)

    override fun provideAuthApi(): AuthApi =
        getRetrofit(requiresAuth = false).create(AuthApi::class.java)

    override fun provideGroupsApi(): GroupsApi =
        getRetrofit(requiresAuth = true).create(GroupsApi::class.java)

    override fun provideLessonsStatusApi(): LessonsStatusApi =
        getRetrofit(requiresAuth = true).create(LessonsStatusApi::class.java)

    override fun provideStaffMembersApi(): StaffMembersApi =
        getRetrofit(requiresAuth = true).create(StaffMembersApi::class.java)

    override fun provideStudentsApi(): StudentsApi =
        getRetrofit(requiresAuth = true).create(StudentsApi::class.java)

    override fun provideStudentsAttendancesApi(): StudentsAttendancesApi =
        getRetrofit(requiresAuth = true).create(StudentsAttendancesApi::class.java)

    override fun provideStudentsPaymentsApi(): StudentsPaymentsApi =
        getRetrofit(requiresAuth = true).create(StudentsPaymentsApi::class.java)

    private fun getRetrofit(requiresAuth: Boolean): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                when {
                    requiresAuth -> protectedOkHttpClient
                    else -> publicOkHttpClient
                }
            )
            .baseUrl(getRootUrl())
            .build()

    private fun getRootUrl(): String = "https://hogwarts-engschool.ru/HogwartsAPI/"

    private fun getOkHttpClient(requiresAuth: Boolean): OkHttpClient = OkHttpClient
        .Builder()
        .addInterceptor { chain ->
            val original = chain.request()

            var requestBuilder = original.newBuilder()

            getAuthToken(requiresAuth = requiresAuth)?.let { authToken ->
                requestBuilder = addAuthHeader(
                    requestBuilder = requestBuilder,
                    authToken = authToken
                )
            }

            val request = requestBuilder
                .method(original.method(), original.body())
                .build()

            chain.proceed(request)
        }
        .sslSocketFactory(sslSocketFactory, sslTrustManager)
        .hostnameVerifier(hostnameVerifier)
        .build()

    private fun getAuthToken(requiresAuth: Boolean): String? =
        if (requiresAuth) {
            authService.getAuthInfo()?.token
        } else {
            null
        }

    private fun addAuthHeader(
        requestBuilder: Request.Builder,
        authToken: String
    ): Request.Builder = requestBuilder.addHeader("Authorization", authToken)

    private fun getSslSocketFactory(): SSLSocketFactory {
        val sslContext = SSLContext.getInstance("SSL")

        sslContext.init(null, arrayOf(getSslTrustManager()), SecureRandom())

        return sslContext.socketFactory
    }

    @SuppressLint("CustomX509TrustManager")
    private fun getSslTrustManager(): X509TrustManager = object: X509TrustManager {
        @SuppressLint("TrustAllX509TrustManager")
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
        @SuppressLint("TrustAllX509TrustManager")
        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
    }

    private fun getHostnameVerifier(): HostnameVerifier = HostnameVerifier { _, _ -> true }
}