package ab.serviceImpl;

import ab.dto.request.GroupRequest;
import ab.dto.response.AllGroupResponse;
import ab.dto.response.GroupResponse;
import ab.dto.response.SimpleResponse;
import ab.entities.Group;
import ab.exceptions.AlreadyExistsException;
import ab.exceptions.IllegalArgumentException;
import ab.exceptions.NotFoundException;
import ab.repository.GroupRepository;
import ab.service.impl.GroupServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GroupServiceImplTest {
    @Mock
    private GroupRepository groupRepository;
    @InjectMocks
    private GroupServiceImpl groupService;

    @Test
    public void save() {
        GroupRequest groupRequest = new GroupRequest("Java1", LocalDate.of(2024, 6, 26));
        Mockito.when(groupRepository.existsByTitle(groupRequest.title())).thenReturn(false);
        SimpleResponse response = groupService.save(groupRequest);
        assertEquals(HttpStatus.OK, response.httpStatus());
        assertEquals("Группа успешно создана!", response.message());
        verify(groupRepository, times(1)).existsByTitle(groupRequest.title());
        verify(groupRepository, times(1)).save(any(Group.class));
    }

    @Test
    public void SaveGroup_AlreadyExists() {
        GroupRequest groupRequest = new GroupRequest("Java", LocalDate.of(2024, 6, 30));
        Mockito.when(groupRepository.existsByTitle(groupRequest.title())).thenReturn(true);
        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class, () -> {
            groupService.save(groupRequest);
        });

        assertEquals("Группа с названием Java уже существует", exception.getMessage());

        verify(groupRepository, times(1)).existsByTitle(groupRequest.title());
        verify(groupRepository, times(0)).save(any(Group.class));
    }

    @Test
    public void UpdateGroup_Success() {
        Long groupId = 1L;
        LocalDate dateOfStart = LocalDate.of(2024, 6, 1); // предположим, что у вас есть дата начала

        Group existingGroup = Group.builder()
                .id(groupId)
                .title("Java2")
                .dateOfStart(dateOfStart)
                .dateOfEnd(LocalDate.of(2024, 6, 30))
                .build();

        LocalDate newDateOfEnd = LocalDate.of(2024, 12, 31);
        GroupRequest groupRequest = new GroupRequest("JS-12", newDateOfEnd);

        if (newDateOfEnd.isBefore(existingGroup.getDateOfStart())) {
            throw new IllegalArgumentException("Дата окончания не может быть раньше даты начала");
        }

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(existingGroup));
        when(groupRepository.existsByTitle(groupRequest.title())).thenReturn(false);
        when(groupRepository.save(any(Group.class))).thenReturn(existingGroup);

        SimpleResponse response = groupService.update(groupId, groupRequest);

        assertEquals(HttpStatus.OK, response.httpStatus());
        assertEquals("Успешно обнавлено!", response.message());

        verify(groupRepository, times(1)).findById(groupId);
        verify(groupRepository, times(1)).existsByTitle(groupRequest.title());
        verify(groupRepository, times(1)).save(existingGroup);
    }

    @Test
    public void getGroup() {
        // Группа найдена!
        Long foundGroupId = 1L;
        Group existingGroup = new Group();
        existingGroup.setId(foundGroupId);
        existingGroup.setTitle("Java2");
        existingGroup.setDateOfStart(LocalDate.of(2024, 6, 1));
        existingGroup.setDateOfEnd(LocalDate.of(2024, 6, 30));

        when(groupRepository.findById(foundGroupId)).thenReturn(Optional.of(existingGroup));

        GroupResponse response = groupService.getGroup(foundGroupId);

        assertEquals(foundGroupId, response.id());
        assertEquals("Java2", response.title());
        assertEquals(LocalDate.of(2024, 6, 1), response.dateOfStart());
        assertEquals(LocalDate.of(2024, 6, 30), response.dateOfEnd());
        //Группа не найдена!
        Long notFoundGroupId = 2L;

        when(groupRepository.findById(notFoundGroupId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            groupService.getGroup(notFoundGroupId);
        });
    }

    @Test
    public void deleteGroup() {
        // Группа успешно удалена!
        long foundGroupId = 1L;
        Group existingGroup = new Group();
        existingGroup.setId(foundGroupId);

        when(groupRepository.findById(foundGroupId)).thenReturn(Optional.of(existingGroup));

        SimpleResponse response = groupService.delete(foundGroupId);

        assertEquals(response.httpStatus(), HttpStatus.OK);
        assertEquals(response.message(), "Группа успешно удалено!");

        verify(groupRepository, times(1)).findById(foundGroupId);
        verify(groupRepository, times(1)).delete(existingGroup);

        // Группа не найдена!
        long notFoundGroupId = 2L;

        when(groupRepository.findById(notFoundGroupId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            groupService.getGroup(notFoundGroupId);
        });
    }

    @Test
    public void findAllGroup() {
        int invalidSize = -1;
        int invalidPage = 0;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            groupService.findAllGroup(invalidSize, invalidPage);
        });
        assertEquals("Индекс страницы не должен быть меньше нуля", exception.getMessage());
        verifyNoInteractions(groupRepository);

        int validSize = 10;
        int validPage = 1;

        Page<GroupResponse> mockPage = new PageImpl<>(Collections.emptyList());
        when(groupRepository.findAllGroup(any(Pageable.class))).thenReturn(mockPage);

        AllGroupResponse response = groupService.findAllGroup(validSize, validPage);

        assertNotNull(response);
        assertEquals(1, response.page());
        assertEquals(0, response.size());
        assertTrue(response.groupResponses().isEmpty());
        verify(groupRepository, times(1)).findAllGroup(PageRequest.of(validPage - 1, validSize, Sort.by("id")));
        verifyNoMoreInteractions(groupRepository);
    }
}



