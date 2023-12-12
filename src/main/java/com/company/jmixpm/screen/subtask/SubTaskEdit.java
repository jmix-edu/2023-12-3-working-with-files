package com.company.jmixpm.screen.subtask;

import com.company.jmixpm.entity.SubTask;
import io.jmix.ui.screen.EditedEntityContainer;
import io.jmix.ui.screen.StandardEditor;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;

@UiController("tm_SubTask.edit")
@UiDescriptor("sub-task-edit.xml")
@EditedEntityContainer("subTaskDc")
public class SubTaskEdit extends StandardEditor<SubTask> {
}