package com.company.jmixpm.screen.task;

import com.company.jmixpm.entity.Task;
import io.jmix.ui.UiComponents;
import io.jmix.ui.component.Component;
import io.jmix.ui.component.LinkButton;
import io.jmix.ui.download.Downloader;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("Task_.browse")
@UiDescriptor("task-browse.xml")
@LookupComponent("tasksTable")
public class TaskBrowse extends StandardLookup<Task> {
    @Autowired
    private UiComponents uiComponents;
    @Autowired
    private Downloader downloader;

    @Install(to = "tasksTable.attachment", subject = "columnGenerator")
    private Component tasksTableAttachmentColumnGenerator(final Task task) {
        if (task.getAttachment() != null) {
            LinkButton linkButton = uiComponents.create(LinkButton.NAME);
            linkButton.setCaption(task.getAttachment().getFileName());
            linkButton.addClickListener(clickEvent -> downloader.download(task.getAttachment()));
            return linkButton;
        }
        return null;
    }
}