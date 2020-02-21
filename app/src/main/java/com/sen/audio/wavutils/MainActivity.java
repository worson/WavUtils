package com.sen.audio.wavutils;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.lib.audio.wav.PcmUtil;
import com.lib.audio.wav.WavFileWriter;
import com.lib.audio.wav.WavHeader;
import com.lib.audio.wav.WavUtil;
import com.sen.audio.wavutils.play.NativePlayer;
import com.sen.audio.wavutils.utils.AILog;
import com.sen.audio.wavutils.utils.AssetsUtil;
import com.sen.audio.wavutils.utils.GlobalContext;
import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivityTag";

    public static final int CUT_DURATION=750;

    private TextView tv_content;

    private int mLastPlayId=-1;
    private String mWavFilePath;
    private String mPcmFilePath;
    private String mCutWavFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GlobalContext.set(getApplication());
        initViews();
    }

    private void initViews() {
        mWavFilePath=GlobalContext.get().getFilesDir().getPath()+"/test.wav";
        mPcmFilePath=GlobalContext.get().getFilesDir().getPath()+"/test.pcm";
        mCutWavFilePath=GlobalContext.get().getFilesDir().getPath()+"/test_cut.wav";
        if (!new File(mWavFilePath).exists()) {
            AssetsUtil.copyFile(GlobalContext.get(),"test.wav",mWavFilePath);
        }
        if (!new File(mPcmFilePath).exists()) {
            AssetsUtil.copyFile(GlobalContext.get(),new File(mPcmFilePath).getName(),mPcmFilePath);
        }
        tv_content=findViewById(R.id.tv_content);

        findViewById(R.id.bt_media_play).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                NativePlayer.getInstance().cancel(mLastPlayId);
                mLastPlayId=NativePlayer.getInstance().play(new File(mWavFilePath),null);
            }
        });

        findViewById(R.id.bt_cut_wav).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                long cutDuration=WavUtil.cutWavTail(mCutWavFilePath,mCutWavFilePath,CUT_DURATION);
                showWavInfo("裁剪后音频",mCutWavFilePath);
                AILog.i(TAG, "onClick: cutWavTail "+cutDuration);
            }
        });

        findViewById(R.id.bt_cut_wav_play).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                NativePlayer.getInstance().cancel(mLastPlayId);
                mLastPlayId=NativePlayer.getInstance().play(new File(mCutWavFilePath),null);
            }
        });




        TextView bt_wav_info=findViewById(R.id.bt_wav_info);
        bt_wav_info.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showWavInfo("原始音频",mWavFilePath);
            }
        });

        findViewById(R.id.bt_create_silent_wav).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                createSilentWav(GlobalContext.get().getFilesDir().getPath()+"/test_silent.wav",CUT_DURATION);
            }
        });

        findViewById(R.id.bt_wrap_pcm).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                PcmUtil.toWav(mPcmFilePath,GlobalContext.get().getFilesDir().getPath()+"/test_pcm.wav");
                showWavInfo("Pcm转换音频",GlobalContext.get().getFilesDir().getPath()+"/test_pcm.wav");
            }
        });


        showWavInfo("原始音频",mWavFilePath);


    }

    private void createSilentWav(String filePath, int duration) {
        WavHeader header = WavUtil.getWavInfo(mWavFilePath);
        int byteSize=(int)WavUtil.getDurationMsByteSize(header,duration);
        WavFileWriter writer = new WavFileWriter();
        try {
            writer.openFile(filePath,header);
            byte[] buffer=new byte[2048];
            int sizeNeedWrite=byteSize;
            while (sizeNeedWrite>buffer.length) {
                sizeNeedWrite-=buffer.length;
                writer.writeData(buffer,0,buffer.length);
            }
            if (sizeNeedWrite>0) {
                writer.writeData(buffer,0,Math.min(buffer.length,sizeNeedWrite));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                writer.closeFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showWavInfo(String tittle,String wavFilePath) {
        tv_content.setText("");
        StringBuilder sb = new StringBuilder();
        WavHeader header = WavUtil.getWavInfo(wavFilePath);
        sb.append(tittle+"\n");
        sb.append(String.format("%s:%s\n","音频时长",header.getDuration()));
        sb.append(String.format("%s:%s\n","采样率",header.getSampleRate()));
        sb.append(String.format("%s:%s\n","每帧位数",header.getBitsPerSample()));
        sb.append(String.format("%s:%s\n","音频格式",header.getAudioFormat()));
        sb.append(String.format("%s:%s\n","块对齐",header.getBlockAlign()));
        sb.append(String.format("%s:%s\n","Byte率",header.getByteRate()));
        sb.append(String.format("%s:%s\n","ChunkID",header.getChunkID()));
        sb.append(String.format("%s:%s\n","ChunkSize",header.getChunkSize()));
        sb.append(String.format("%s:%s\n","Format",header.getFormat()));
        sb.append(String.format("%s:%s\n","通道数",header.getNumChannel()));
        sb.append(String.format("%s:%s\n","SubChunk1ID",header.getSubChunk1ID()));
        sb.append(String.format("%s:%s\n","SubChunk1Size",header.getSubChunk1Size()));
        sb.append(String.format("%s:%s\n","SubChunk2ID",header.getSubChunk2ID()));
        sb.append(String.format("%s:%s\n","SubChunk2Size",header.getSubChunk2Size()));
        tv_content.setText(sb.toString());
    }
}
