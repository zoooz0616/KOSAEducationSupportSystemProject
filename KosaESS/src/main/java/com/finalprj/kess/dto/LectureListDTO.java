package com.finalprj.kess.dto;

import java.util.List;

import com.finalprj.kess.model.LectureVO;
import com.finalprj.kess.model.ProfessorVO;
import com.finalprj.kess.model.SubjectVO;

import lombok.Data;

@Data
public class LectureListDTO {
	private List<LectureVO> lectureList;
    private List<SubjectVO> subjectList;
    private List<ProfessorVO> professorList;

    public LectureListDTO(List<LectureVO> lectureList, List<SubjectVO> subjectList, List<ProfessorVO> professorList) {
        this.lectureList = lectureList;
        this.subjectList = subjectList;
        this.professorList = professorList;
    }
}
