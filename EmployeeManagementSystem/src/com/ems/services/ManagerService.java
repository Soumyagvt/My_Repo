package com.ems.services;

import com.ems.dao.LeaveDAO;
import com.ems.dao.TaskDAO;
import com.ems.models.LeaveRequest;
import com.ems.models.Task;

import java.util.List;

public class ManagerService {
    private final LeaveDAO leaveDAO = new LeaveDAO();
    private final TaskDAO taskDAO = new TaskDAO();

    public List<LeaveRequest> viewPendingLeaves(int managerId) {
        return leaveDAO.listByManager(managerId);
    }

    public boolean updateLeaveStatus(int leaveId, String status) {
        return leaveDAO.updateStatus(leaveId, status);
    }

    public boolean assignTask(Task t) {
        return taskDAO.create(t);
    }

    public List<Task> viewTasksForEmployee(int employeeId) {
        return taskDAO.listByEmployee(employeeId);
    }
}
