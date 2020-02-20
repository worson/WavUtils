package com.lib.audio.wav;

import java.io.IOException;

/**
 * 说明:
 *
 * @author wangshengxing  02.20 2020
 */
public class WavUtil {

    /**
     * 获取wav文件文件信息
     * @param fullPath
     * @return
     */
    public static WavHeader getWavInfo(String fullPath){
        WavHeader header=null;
        WavFileReader reader = new WavFileReader();
        try {
            if (reader.openFile(fullPath)) {
                header=reader.getmWavFileHeader();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                reader.closeFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return header;
    }
}
