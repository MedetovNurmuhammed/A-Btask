package ab.service;

import ab.dto.request.GroupRequest;
import ab.dto.response.AllGroupResponse;
import ab.dto.response.GroupResponse;
import ab.dto.response.SimpleResponse;

public interface GroupService {
    SimpleResponse save(GroupRequest groupRequest);

    SimpleResponse update(Long groupId, GroupRequest groupRequest);

    AllGroupResponse findAllGroup(int size, int page);

    GroupResponse getGroup(Long groupId);

    SimpleResponse delete(long groupId);
}

