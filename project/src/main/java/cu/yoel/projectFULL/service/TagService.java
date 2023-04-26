package cu.yoel.projectFULL.service;

import cu.yoel.projectFULL.models.Tag;

import java.util.List;

public interface TagService {
    Tag save(String tagName, String tagColor);
    List<Tag> getTags();
}
