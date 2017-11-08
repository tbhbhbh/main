package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import seedu.address.commons.util.FileUtil;

//@@author JunQuann
public class ImageFileStorage {

    private String dirPath;

    public ImageFileStorage(String dirPath) {
        this.dirPath = dirPath;
    }

    public String getImageFilePath(String imageName) {
        return dirPath + imageName;
    }

    public void copyImage(String currentImagePath, String imageName) throws IOException {
        File currentImage = new File(currentImagePath);
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(currentImage));
        createImageDir();
        String newImagePath = getImageFilePath(imageName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(newImagePath));
        boolean done = false;

        while(!done) {

            byte[] buf = new byte[1024];
            int readLength = bis.read(buf, 0, 1024);

            if (readLength < 1024) {
                done = true;
            }

            bos.write(buf);
        }

        bis.close();
        bos.close();
    }

    private void createImageDir() throws IOException {
        requireNonNull(dirPath);
        FileUtil.createDirs(new File(dirPath));
    }


}
