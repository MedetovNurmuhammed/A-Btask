package ab.service;

import ab.dto.request.StudentRequest;
import ab.dto.response.AllStudentResponse;
import ab.dto.response.SimpleResponse;
import ab.dto.response.StudentResponse;

public interface StudentService {
    SimpleResponse save(StudentRequest studentRequest, Long groupId);

    AllStudentResponse findAllGroupStud(int page, int size, Long groupId);

    StudentResponse findById(Long studId);

    SimpleResponse update(Long studId, StudentRequest studentRequest);

    SimpleResponse delete(Long studId);
}
