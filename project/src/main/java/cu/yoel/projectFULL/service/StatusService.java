package cu.yoel.projectFULL.service;

import cu.yoel.projectFULL.models.Status;

import java.util.List;

public interface StatusService {
    Status save(Status status);
    List<Status> getStatus();
}
