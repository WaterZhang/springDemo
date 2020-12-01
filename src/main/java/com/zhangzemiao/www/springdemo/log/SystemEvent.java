package com.zhangzemiao.www.springdemo.log;

public enum SystemEvent implements ISystemEvent {

    UNHANDLED_EXCEPTION(1, "Unhandled exception"),
    UNHANDLED_REDIRECT_EXCEPTION(2, "Unhandled redirect exception"),

    SAMPLE_SYS_EVENT(10, "Sample system event"),

    //Github API
    GITHUB_CONTRIBUTORS_ERROR(1000, "error get git contributors"),
    ;

    private final int id;
    private final String description;

    SystemEvent(
        final int id,
        final String description) {

        this.id = id;
        this.description = description;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getDescription() {
        return description;
    }
}
