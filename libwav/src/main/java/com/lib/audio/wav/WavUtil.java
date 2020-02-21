package com.lib.audio.wav;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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


    /**
     * 删除文件指定指定时长
     * @param inputPath
     * @param outputPath
     * @param durationMs ms
     * @return
     */
    public static long cutWavTail(String inputPath,String outputPath,int durationMs){
        long cutsize=-1;
        long sizeNeedWrite=-1;
        long sizeNeedCut=-1;
        WavHeader header=null;
        File readFile=new File(inputPath);
        if ( (!readFile.exists()) || outputPath==null || outputPath.length()<=0) {
            return cutsize;
        }
        boolean sameFile=inputPath.equals(outputPath);

        File tmpWriteFile=new File(sameFile?(outputPath+".tmp"):outputPath);
        WavFileReader reader = new WavFileReader();
        WavFileWriter writer = null;
        try {
            if (reader.openFile(inputPath)) {
                header=reader.getmWavFileHeader();
                writer = new WavFileWriter();
                writer.openFile(tmpWriteFile.getAbsolutePath(),header.getSampleRate(),header.getNumChannel(),header.getBitsPerSample());
                long originSize=header.getDataByteSize();
                sizeNeedCut= getDurationMsByteSize(header,durationMs);
                sizeNeedWrite=originSize-sizeNeedCut;
                byte[] buffer=new byte[(int)(Math.min(2048,sizeNeedCut))];
                int readSize=-1;
                while (((readSize=reader.readData(buffer,0,buffer.length))==buffer.length)
                    && sizeNeedWrite>buffer.length){
                    sizeNeedWrite-=readSize;
                    writer.writeData(buffer,0,readSize);
                }
                if (readSize>0 && sizeNeedWrite>0) {
                    int writeSize=(int)Math.min(readSize,sizeNeedWrite);
                    sizeNeedWrite-=writeSize;
                    writer.writeData(buffer,0,writeSize);
                }
                cutsize=sizeNeedCut-sizeNeedWrite;
//                Log.i(TAG, String.format("cutWavTail: originSize=%s,sizeNeedCut=%s,lastReadSize=%s,sizeNeedWrite=%s",originSize,sizeNeedCut,readSize,sizeNeedWrite));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                reader.closeFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (writer != null) {
                try {
                    writer.closeFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //写说数据写完了
            if (sizeNeedWrite==0) {
                if (sameFile) {
                    if (readFile.delete()) {
                        tmpWriteFile.renameTo(readFile);
                    }
                }
            }
        }
        cutsize=(sizeNeedWrite==0)?cutsize:-1;
        return cutsize>0?getByteSizeDurationMs(header,cutsize):cutsize;
    }


    /**
     * 获取转换时长对应的byte数据
     * @param header
     * @param durationMs
     * @return
     */
    public static long getDurationMsByteSize(WavHeader header , long durationMs){
        double byteSize=header.getSampleRate()/1000.0*header.getNumChannel()*header.getBitsPerSample()*durationMs/8;
        return (long)byteSize;
    }


    /**获取byte数目对应的时长
     * @param header
     * @param bytesize
     * @return
     */
    public static long getByteSizeDurationMs(WavHeader header , long bytesize){
        double duration=bytesize*8.0/(header.getNumChannel()*header.getBitsPerSample()*header.getBitsPerSample());
        return (long) duration;

    }
}
