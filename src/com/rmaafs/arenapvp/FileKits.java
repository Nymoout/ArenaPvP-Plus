package com.rmaafs.arenapvp;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.file.FileConfiguration;

public class FileKits {

    File file;
    public FileConfiguration cfile;

    public FileKits(File f, FileConfiguration cf) {
        file = f;
        cfile = cf;
    }

    public void save() {
        try {
            cfile.save(file);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
