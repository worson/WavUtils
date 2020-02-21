package com.app.audio.wavutils;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.lib.audio.player.NativePlayer;
import com.lib.audio.wav.PcmUtil;
import com.lib.audio.wav.WavFileWriter;
import com.lib.audio.wav.WavHeader;
import com.lib.audio.wav.WavUtil;
import com.lib.common.androidbase.global.GlobalContext;
import com.lib.common.androidbase.resource.AssetsUtil;
import com.lib.common.androidbase.utils.ToastUtil;
import com.lib.common.dlog.DLog;
import com.lib.common.io.string.Strings;
import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivityTag";

    public static final int CUT_DURATION=750;

    private TextView tv_content;

    private int mLastPlayId=-1;
    private String mWavFilePath;
    private File mPcmFile;
    private String mCutWavFilePath;
    private String mCurrentAudioFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GlobalContext.set(getApplication());
        initViews();
    }

    private void initViews() {
        mWavFilePath=GlobalContext.get().getFilesDir().getPath()+"/test.wav";
        mPcmFile =new File(GlobalContext.get().getFilesDir().getPath()+"/test.pcm");
        mCutWavFilePath=GlobalContext.get().getFilesDir().getPath()+"/test_cut.wav";
        if (!new File(mWavFilePath).exists()) {
            AssetsUtil.copyFile(GlobalContext.get(),"test.wav",mWavFilePath);
        }
        if (!mPcmFile.exists()) {
            AssetsUtil.copyFile(GlobalContext.get(),mPcmFile.getName(), mPcmFile.getPath());
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
                long cutDuration=WavUtil.cutWavTail(mWavFilePath,mCutWavFilePath,CUT_DURATION);
                showWavInfo("裁剪后音频",mCutWavFilePath);
                mCurrentAudioFilePath=mCutWavFilePath;
                DLog.i(TAG, "onClick: cutWavTail "+cutDuration);
            }
        });

        findViewById(R.id.bt_current_wav_play).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Strings.notEmpty(mCurrentAudioFilePath)) {
                    NativePlayer.getInstance().cancel(mLastPlayId);
                    mLastPlayId=NativePlayer.getInstance().play(new File(mCurrentAudioFilePath),null);
                }else {
                    ToastUtil.longToast("当前无处理过的音频");
                }

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
                mCurrentAudioFilePath=GlobalContext.get().getFilesDir().getPath()+"/test_silent.wav";
            }
        });

        findViewById(R.id.bt_wrap_pcm).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String newFilePath=GlobalContext.get().getFilesDir().getPath()+"/test_pcm.wav";
                PcmUtil.toWav(mPcmFile.getAbsolutePath(),newFilePath,new WavHeader(16000,1,16));
                showWavInfo("Pcm转换音频",newFilePath);
                mCurrentAudioFilePath=newFilePath;
                DLog.i(TAG, "onClick: pcm转换后的音频为 "+newFilePath);
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
