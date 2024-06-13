package ab.service.impl;

import ab.dto.request.GroupRequest;
import ab.dto.response.AllGroupResponse;
import ab.dto.response.GroupResponse;
import ab.dto.response.SimpleResponse;
import ab.entities.Group;
import ab.exceptions.AlreadyExistsException;
import ab.exceptions.IllegalArgumentException;
import ab.exceptions.NotFoundException;
import ab.repository.GroupRepository;
import ab.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;

    @Override
    public SimpleResponse save(GroupRequest groupRequest) {
        boolean exists = groupRepository.existsByTitle(groupRequest.title());
        if (exists) throw new AlreadyExistsException("Группа с названием " + groupRequest.title() + " уже существует");
        groupRepository.save(
                Group.builder()
                        .title(groupRequest.title())
                        .dateOfStart(LocalDate.now())
                        .dateOfEnd(groupRequest.dateOfEnd())
                        .build()
        );
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Группа успешно создана!")
                .build();
    }

    @Override
    public SimpleResponse update(Long groupId, GroupRequest groupRequest) {
        Group updatedGroup = getById(groupId);

        if (!updatedGroup.getTitle().equals(groupRequest.title())) {
            boolean exists = groupRepository.existsByTitle(groupRequest.title());
            if (exists)
                throw new AlreadyExistsException("Группа с названием " + groupRequest.title() + " уже существует!");
        }
        updatedGroup.setTitle(groupRequest.title());
        updatedGroup.setDateOfEnd(groupRequest.dateOfEnd());
        groupRepository.save(updatedGroup);

        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Успешно обнавлено!")
                .build();
    }

    @Override
    public AllGroupResponse findAllGroup(int size, int page) {
        if (page < 1 && size < 1) throw new IllegalArgumentException("Индекс страницы не должен быть меньше нуля");
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id"));
        Page<GroupResponse> allGroup = groupRepository.findAllGroup(pageable);

        return AllGroupResponse.builder()
                .page(allGroup.getNumber() + 1)
                .size(allGroup.getNumberOfElements())
                .groupResponses(allGroup.getContent())
                .build();
    }

    @Override
    public GroupResponse getGroup(Long groupId) {
        Group group = getById(groupId);
        return GroupResponse.builder()
                .id(groupId)
                .title(group.getTitle())
                .dateOfStart(group.getDateOfStart())
                .dateOfEnd(group.getDateOfEnd())
                .build();
    }

    @Override
    public SimpleResponse delete(long groupId) {
        Group group = getById(groupId);
        groupRepository.delete(group);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Группа успешно удалено!")
                .build();
    }

    private Group getById(long id) {
        return groupRepository.findById(id).orElseThrow(() -> new NotFoundException("Группа не найдена"));
    }
}
