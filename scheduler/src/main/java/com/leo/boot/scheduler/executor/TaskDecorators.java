package com.leo.boot.scheduler.executor;

import org.springframework.core.task.TaskDecorator;

import java.util.LinkedList;
import java.util.List;

public class TaskDecorators implements TaskDecorator {

    private List<TaskDecorator> decorators = new LinkedList<>();

    @Override
    public Runnable decorate(Runnable runnable) {
        return decorators.stream()
                .reduce(this::merge)
                .map(decorator -> decorator.decorate(runnable))
                .orElse(runnable);
    }

    public TaskDecorators add(TaskDecorator taskDecorator) {
        decorators.add(taskDecorator);
        return this;
    }

    private TaskDecorator merge(TaskDecorator a, TaskDecorator b) {
        return r -> a.decorate(b.decorate(r));
    }
}
