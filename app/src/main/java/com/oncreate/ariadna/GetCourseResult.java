package com.oncreate.ariadna;


import com.oncreate.ariadna.ModelsVO.Course;
import com.oncreate.ariadna.loginLearn.ServiceResult;

public class GetCourseResult extends ServiceResult {
    private Course course;

    public Course getCourse() {
        return this.course;
    }

    public boolean isUpdated() {
        return this.course != null;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
