package cu.yoel.projectFULL.service.impl;

import cu.yoel.projectFULL.models.Status;
import cu.yoel.projectFULL.repositories.StatusRepository;
import cu.yoel.projectFULL.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusServiceImpl implements StatusService {

    @Autowired
    StatusRepository statusRepository;

    @Override
    public Status save(Status status) {
        return statusRepository.save(status);
    }

    @Override
    public List<Status> getStatus() {
        return statusRepository.findAll();
    }
}
