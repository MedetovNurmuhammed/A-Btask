package ab.api;

import ab.dto.request.GroupRequest;
import ab.dto.response.AllGroupResponse;
import ab.dto.response.GroupResponse;
import ab.dto.response.SimpleResponse;
import ab.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups")
@Validated
public class GroupApi {
    private final GroupService groupService;

    @Secured("INSTRUCTOR")
    @Operation(summary = "Создать группу", description = "Создание новой группы с предоставленными данными.Авторизация: Инструктор!")
    @PostMapping
    public SimpleResponse save(@RequestBody @Valid GroupRequest groupRequest) {
        return groupService.save(groupRequest);
    }

    @Secured("INSTRUCTOR")
    @Operation(summary = "Обновить группу", description = "Обновление данных существующей группы по ID.Авторизация: Инструктор!")
    @PatchMapping("/{groupId}")
    public SimpleResponse update(@PathVariable Long groupId, @RequestBody GroupRequest groupRequest) {
        return groupService.update(groupId, groupRequest);
    }

    @Secured("INSTRUCTOR")
    @Operation(summary = "Получить все группы", description = "Получение списка всех групп с возможностью пагинации.Авторизация: Инструктор!")
    @GetMapping()
    public AllGroupResponse findAll(@RequestParam(required = false, defaultValue = "1") int page,
                                    @RequestParam(required = false, defaultValue = "8") int size) {
        return groupService.findAllGroup(size, page);
    }

    @Secured("INSTRUCTOR")
    @Operation(summary = "Получить информацию о группе", description = "Получение данных группы по ID. Авторизация: Инструктор!")
    @GetMapping("/{groupId}")
    public GroupResponse get(@PathVariable Long groupId) {
        return groupService.getGroup(groupId);
    }

    @Secured("INSTRUCTOR")
    @Operation(summary = "Удалить группу", description = "Удаление группы по ID.Авторизация: Инструктор!")
    @DeleteMapping("/{groupId}")
    public SimpleResponse delete(@PathVariable long groupId) {
        return groupService.delete(groupId);
    }

}

