package com.finance_backend.controller;

import com.finance_backend.dto.DashboardResponse;
import com.finance_backend.dto.RecordRequest;
import com.finance_backend.dto.RecordResponse;
import com.finance_backend.entity.User;
import com.finance_backend.service.RecordService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/records")
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public RecordResponse create(@Valid @RequestBody RecordRequest request,
                                 HttpServletRequest httpRequest) {

        User user = getAuthenticatedUser(httpRequest);

        return recordService.createRecord(request, user.getId());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('VIEWER','ANALYST','ADMIN')")
    public List<RecordResponse> list(@RequestParam(required = false) LocalDate fromDate,
                                     @RequestParam(required = false) LocalDate toDate,
                                     @RequestParam(required = false) String category,
                                     @RequestParam(required = false) String type,
                                     HttpServletRequest httpRequest) {

        User user = getAuthenticatedUser(httpRequest);

        return recordService.getRecords(user.getId(), fromDate, toDate, category, type);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('VIEWER','ANALYST','ADMIN')")
    public RecordResponse getById(@PathVariable Long id, HttpServletRequest httpRequest) {

        User user = getAuthenticatedUser(httpRequest);

        return recordService.getRecordById(id, user.getId());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RecordResponse update(@PathVariable Long id,
                                 @Valid @RequestBody RecordRequest request,
                                 HttpServletRequest httpRequest) {

        User user = getAuthenticatedUser(httpRequest);

        return recordService.updateRecord(id, request, user.getId());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id, HttpServletRequest httpRequest) {

        User user = getAuthenticatedUser(httpRequest);

        recordService.deleteRecord(id, user.getId());
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('VIEWER','ANALYST','ADMIN')")
    public DashboardResponse dashboard(HttpServletRequest request) {

        User user = getAuthenticatedUser(request);

        return recordService.getDashboard(user.getId());
    }

    private User getAuthenticatedUser(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        return user;
    }
}