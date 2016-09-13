package com.oncreate.ariadna.ModelsVO;


import com.oncreate.ariadna.loginLearn.ServiceResult;

public class GetCourseResult extends ServiceResult {
    private CursoPre respuesta;

    public CursoPre getCourse() {
        return this.respuesta;
    }

    public boolean isUpdated() {
        return this.respuesta != null;
    }

    public void setCourse(CursoPre course) {
        this.respuesta = course;
    }
}
