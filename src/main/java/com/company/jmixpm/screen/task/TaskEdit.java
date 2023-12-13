package com.company.jmixpm.screen.task;

import com.company.jmixpm.app.TaskService;
import com.company.jmixpm.entity.SubTask;
import com.company.jmixpm.entity.Task;
import io.jmix.ui.component.BrowserFrame;
import io.jmix.ui.component.FileStorageResource;
import io.jmix.ui.component.FileStorageUploadField;
import io.jmix.ui.component.SingleFileUploadField;
import io.jmix.ui.model.CollectionPropertyContainer;
import io.jmix.ui.model.DataContext;
import io.jmix.ui.model.InstanceContainer;
import io.jmix.ui.screen.*;
import io.jmix.ui.upload.TemporaryStorage;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@UiController("Task_.edit")
@UiDescriptor("task-edit.xml")
@EditedEntityContainer("taskDc")
public class TaskEdit extends StandardEditor<Task> {
    @Autowired
    private TaskService taskService;
    @Autowired
    private BrowserFrame attachmentBrowserFrame;
    @Autowired
    private FileStorageUploadField importSubtasksField;
    @Autowired
    private TemporaryStorage temporaryStorage;
    @Autowired
    private DataContext dataContext;
    @Autowired
    private CollectionPropertyContainer<SubTask> subTasksDc;

    @Subscribe
    public void onInitEntity(InitEntityEvent<Task> event) {
        event.getEntity().setAssignee(taskService.findLeastBusyUser());
    }

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        refreshAttachmentBrowserFrame();
    }

    @Subscribe(id = "taskDc", target = Target.DATA_CONTAINER)
    public void onTaskDcItemPropertyChange(final InstanceContainer.ItemPropertyChangeEvent<Task> event) {
        if ("attachment".equals(event.getProperty())) {
            refreshAttachmentBrowserFrame();
        }
    }

    private void refreshAttachmentBrowserFrame() {
        Task task = getEditedEntity();
        if (task.getAttachment() != null) {
            attachmentBrowserFrame.setSource(FileStorageResource.class)
                    .setFileReference(task.getAttachment())
                    .setMimeType(task.getAttachment().getContentType());
        }
    }

    @Subscribe("importSubtasksField")
    public void onImportSubtasksFieldFileUploadSucceed(final SingleFileUploadField.FileUploadSucceedEvent event)
            throws IOException {
        UUID fileId = importSubtasksField.getFileId();
        if (fileId == null) {
            return;
        }

        File file = temporaryStorage.getFile(fileId);
        if (file != null) {
            processFile(file);
            temporaryStorage.deleteFile(fileId);
        }
    }

    private void processFile(File file) throws IOException {
        List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
        for (String line : lines) {
            SubTask subTask = dataContext.create(SubTask.class);
            subTask.setName(line);
            subTask.setTask(getEditedEntity());
            subTasksDc.getMutableItems().add(subTask);
        }
    }
}