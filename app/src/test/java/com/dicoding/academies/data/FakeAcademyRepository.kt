package com.dicoding.academies.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.academies.data.source.local.entity.ContentEntity
import com.dicoding.academies.data.source.local.entity.CourseEntity
import com.dicoding.academies.data.source.local.entity.ModuleEntity
import com.dicoding.academies.data.source.remote.RemoteDataSource
import com.dicoding.academies.data.source.remote.response.ContentResponse
import com.dicoding.academies.data.source.remote.response.CourseResponse
import com.dicoding.academies.data.source.remote.response.ModuleResponse

class FakeAcademyRepository (private val remoteDataSource: RemoteDataSource) : AcademyDataSource {

    override fun getAllCourses(): LiveData<List<CourseEntity>> {
        val courseResult = MutableLiveData<List<CourseEntity>>()
        remoteDataSource.getAllCourses(object: RemoteDataSource.LoadCoursesCallback {
            override fun onAllCoursesReceived(courseResponse: List<CourseResponse>) {
                val courseList = ArrayList<CourseEntity>()
                for (response in courseResponse) {
                    val course = CourseEntity(response.id,
                        response.title,
                        response.description,
                        response.date,
                        false,
                        response.imagePath)

                    courseList.add(course)
                }
                courseResult.postValue(courseList)
            }
        })
        return courseResult
    }

    override fun getBookmarkedCourses(): LiveData<List<CourseEntity>> {
        val courseResult = MutableLiveData<List<CourseEntity>>()
        remoteDataSource.getAllCourses(object: RemoteDataSource.LoadCoursesCallback {
            override fun onAllCoursesReceived(courseResponse: List<CourseResponse>) {
                val courseList = ArrayList<CourseEntity>()
                for (response in courseResponse) {
                    val course = CourseEntity(response.id,
                        response.title,
                        response.description,
                        response.date,
                        false,
                        response.imagePath)

                    courseList.add(course)
                }
                courseResult.postValue(courseList)
            }
        })
        return courseResult
    }

    // Pada metode ini di modul selanjutnya akan mengembalikan kelas POJO baru, gabungan antara course dengan module-nya.
    override fun getCourseWithModules(courseId: String): LiveData<CourseEntity> {
        val courseResult = MutableLiveData<CourseEntity>()
        remoteDataSource.getAllCourses(object: RemoteDataSource.LoadCoursesCallback {
            override fun onAllCoursesReceived(courseResponse: List<CourseResponse>) {
                lateinit var course: CourseEntity
                for (response in courseResponse) {
                    if (response.id == courseId) {
                        course = CourseEntity(response.id,
                            response.title,
                            response.description,
                            response.date,
                            false,
                            response.imagePath)
                    }
                }
                courseResult.postValue(course)
            }
        })
        return courseResult
    }

    override fun getAllModulesByCourse(courseId: String): LiveData<List<ModuleEntity>> {
        val moduleResults = MutableLiveData<List<ModuleEntity>>()
        remoteDataSource.getModules(courseId, object: RemoteDataSource.LoadModulesCallback {
            override fun onAllModulesReceived(moduleResponse: List<ModuleResponse>) {
                val moduleList = ArrayList<ModuleEntity>()
                for(response in moduleResponse) {
                    val course = ModuleEntity(response.moduleId,
                        response.courseId,
                        response.title,
                        response.position,
                        false)

                    moduleList.add(course)
                }
                moduleResults.postValue(moduleList)
            }
        })
        return moduleResults
    }


    override fun getContent(courseId: String, moduleId: String): LiveData<ModuleEntity> {
        val moduleResults = MutableLiveData<ModuleEntity>()
        remoteDataSource.getModules(courseId, object: RemoteDataSource.LoadModulesCallback {
            override fun onAllModulesReceived(moduleResponse: List<ModuleResponse>) {
                lateinit var module: ModuleEntity
                for(response in moduleResponse) {
                    if (response.moduleId == moduleId) {
                        module = ModuleEntity(response.moduleId,
                            response.courseId,
                            response.title,
                            response.position,
                            false)
                        remoteDataSource.getContent(moduleId, object: RemoteDataSource.LoadContentCallback {
                            override fun onContentReceived(contentResponse: ContentResponse) {
                                module.contentEntity = ContentEntity(contentResponse.content)
                                moduleResults.postValue(module)
                            }
                        })
                        break
                    }
                }
            }

        })
        return moduleResults
    }
}