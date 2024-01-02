package com.SachinApps.Whatscan.Pro.WhatsClone.manage.Models;

import java.io.File;

public class files_model {
    private File file;
    private String filename;
    private boolean selected;
    private String size;
    private String type;

    public files_model(String str, String str2, String str3, File file, boolean z) {
        this.filename = str;
        this.type = str2;
        this.size = str3;
        this.file = file;
        this.selected = z;
    }

    public String getFilename() {
        return this.filename;
    }

    public void setFilename(String str) {
        this.filename = str;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String str) {
        this.type = str;
    }

    public String getSize() {
        return this.size;
    }

    public void setSize(String str) {
        this.size = str;
    }

    public File getFile() {
        return this.file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setSelected(boolean z) {
        this.selected = z;
    }
}
