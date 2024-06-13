package ab.service;

import ab.dto.request.AnswerTaskRequest;
import ab.dto.response.AnswerTaskResponses;
import ab.dto.response.SimpleResponse;

import java.nio.file.AccessDeniedException;

public interface AnswerTaskService {
    SimpleResponse save(Long answerTaskId, AnswerTaskRequest answerTaskRequest) throws AccessDeniedException;

    AnswerTaskResponses findAnswerByTaskId(Long taskId);
}
