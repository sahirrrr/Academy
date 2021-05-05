package com.dicoding.academies.data

import com.dicoding.academies.data.source.remote.RemoteDataSource
import com.dicoding.academies.data.source.remote.response.CourseResponse
import com.dicoding.academies.utils.DataDummy
import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

class AcademyRepositoryTest {

    private val remote = Mockito.mock(RemoteDataSource::class.java)
    private val academyRepository = FakeAcademyRepository(remote)

    private val courseResponse = DataDummy.generateRemoteDummyCourses()
    private val courseId = courseResponse[0].id
    private val moduleResponses = DataDummy.generateRemoteDummyModules(courseId)
    private val moduleId = moduleResponses[0].moduleId
    private val content = DataDummy.generateRemoteDummyContent(moduleId)

    @Test
    fun getAllCourses() {
        `when`(remote.getAllCourses()).thenReturn(courseResponse)
        val courseEntities = academyRepository.getAllCourses()
        verify(remote).getAllCourses()
        assertNotNull(courseEntities)
        assertEquals(courseResponse.size.toLong(), courseEntities.size.toLong())
    }

    @Test
    fun getBookmarkedCourses() {
        `when`(remote.getModules(courseId)).thenReturn(moduleResponses)
        val moduleEntities = academyRepository.getAllModulesByCourse(courseId)
        verify(remote).getModules(courseId)
        assertNotNull(moduleEntities)
        assertEquals(moduleResponses.size.toLong(), moduleEntities.size.toLong())
    }

    @Test
    fun getCourseWithModules() {
        `when`(remote.getAllCourses()).thenReturn(courseResponse)
        val courseEntities = academyRepository.getBookmarkedCourses()
        verify(remote).getAllCourses()
        assertNotNull(courseEntities)
        assertEquals(courseResponse.size.toLong(), courseEntities.size.toLong())
    }

    @Test
    fun getAllModulesByCourse() {
        `when`(remote.getModules(courseId)).thenReturn(moduleResponses)
        `when`(remote.getContent(moduleId)).thenReturn(content)
        val resultModule = academyRepository.getContent(courseId, moduleId)
        verify(remote).getContent(moduleId)
        assertNotNull(resultModule)
        assertEquals(content.content, resultModule.contentEntity?.content)
    }

    @Test
    fun getContent() {
        `when`(remote.getAllCourses()).thenReturn(courseResponse)
        val resultCourse = academyRepository.getCourseWithModules(courseId)
        verify(remote).getAllCourses()
        assertNotNull(resultCourse)
        assertEquals(courseResponse[0].title, resultCourse.title)
    }
}