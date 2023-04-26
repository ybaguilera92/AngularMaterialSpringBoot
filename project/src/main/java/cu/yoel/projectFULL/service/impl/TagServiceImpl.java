package cu.yoel.projectFULL.service.impl;

import cu.yoel.projectFULL.models.Tag;
import cu.yoel.projectFULL.repositories.TagRepository;
import cu.yoel.projectFULL.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    TagRepository tagRepository;

    @Override
    public Tag save(String tagTitle, String tagColor) {
        Tag tag = new Tag();
        tag.setTitle(tagTitle);
        tag.setColor(tagColor);
        return tagRepository.save(tag);
    }

    @Override
    public List<Tag> getTags() {
        return tagRepository.findAll();
    }
}
