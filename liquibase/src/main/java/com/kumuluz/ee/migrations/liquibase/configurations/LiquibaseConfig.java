package com.kumuluz.ee.migrations.liquibase.configurations;

import java.util.List;

public class LiquibaseConfig {

    private String jndiName;
    private String file;
    private boolean startupDropAll;
    private boolean startupUpdate;
    private List<String> contexts;
    private List<String> labels;

    public String getJndiName() {
        return jndiName;
    }

    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public boolean isStartupDropAll() {
        return startupDropAll;
    }

    public void setStartupDropAll(boolean startupDropAll) {
        this.startupDropAll = startupDropAll;
    }

    public boolean isStartupUpdate() {
        return startupUpdate;
    }

    public void setStartupUpdate(boolean startupUpdate) {
        this.startupUpdate = startupUpdate;
    }

    public List<String> getContexts() {
        return contexts;
    }

    public void setContexts(List<String> contexts) {
        this.contexts = contexts;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }
}
