package com.sen.audio.wavutils;

import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.lib.audio.wav.WavHeader;
import com.lib.audio.wav.WavUtil;
import com.sen.audio.wavutils.play.NativePlayer;
import com.sen.audio.wavutils.utils.AssetsUtil;
import com.sen.audio.wavutils.utils.GlobalContext;
import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivityTag";
    private TextView tv_content;

    private int mLastPlayId=-1;
    private String mWavFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GlobalContext.set(getApplication());
        initViews();
    }

    private void initViews() {
        mWavFilePath=GlobalContext.get().getFilesDir().getPath()+"/test.wav";
        if (!new File(mWavFilePath).exists()) {
            AssetsUtil.copyFile(GlobalContext.get(),"test.wav",mWavFilePath);
        }

        tv_content=findViewById(R.id.tv_content);

        findViewById(R.id.bt_media_play).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AssetFileDescriptor descriptor = null;
                try {
                    NativePlayer.getInstance().cancel(mLastPlayId);
                    descriptor = getAssets().openFd("test.mp3");
                    mLastPlayId=NativePlayer.getInstance().play(descriptor,null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });





        TextView bt_wav_info=findViewById(R.id.bt_wav_info);
        bt_wav_info.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showWavInfo();
            }
        });

        showWavInfo();


    }

    private void showWavInfo() {
        tv_content.setText("");
        StringBuilder sb = new StringBuilder();
        WavHeader header = WavUtil.getWavInfo(mWavFilePath);
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
