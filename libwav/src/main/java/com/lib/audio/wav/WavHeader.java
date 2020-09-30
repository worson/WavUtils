
package com.lib.audio.wav;

public class WavHeader {

	public static final int WAV_FILE_HEADER_SIZE = 44;
	public static final int WAV_CHUNKSIZE_EXCLUDE_DATA = 36;

	public static final int WAV_CHUNKSIZE_OFFSET = 4;
	public static final int WAV_SUB_CHUNKSIZE1_OFFSET = 16;
	public static final int WAV_SUB_CHUNKSIZE2_OFFSET = 40;

	protected String mChunkID = "RIFF";
	protected int mChunkSize = 0;
	protected String mFormat = "WAVE";

	protected String mSubChunk1ID = "fmt ";
	protected int mSubChunk1Size = 16;
	protected short mAudioFormat = 1;
	protected short mNumChannel = 1;
	protected int mSampleRate = 8000;
	protected int mByteRate = 0;
	protected short mBlockAlign = 0;
	protected short mBitsPerSample = 8;

	protected String mSubChunk2ID = "data";
	protected int mSubChunk2Size = 0;

	public WavHeader() {

	}

	public WavHeader(int sampleRateInHz, int channels, int bitsPerSample) {
		mSampleRate = sampleRateInHz;
		mBitsPerSample = (short) bitsPerSample;
		mNumChannel = (short) channels;
		mByteRate = mSampleRate * mNumChannel * mBitsPerSample / 8;
		mBlockAlign = (short) (mNumChannel * mBitsPerSample / 8);
	}

	public String getChunkID() {
		return mChunkID;
	}

	public int getChunkSize() {
		return mChunkSize;
	}

	public String getFormat() {
		return mFormat;
	}

	public String getSubChunk1ID() {
		return mSubChunk1ID;
	}

	public int getSubChunk1Size() {
		return mSubChunk1Size;
	}

	public short getAudioFormat() {
		return mAudioFormat;
	}

	public short getNumChannel() {
		return mNumChannel;
	}

	public int getSampleRate() {
		return mSampleRate;
	}

	public int getByteRate() {
		return mByteRate;
	}

	public short getBlockAlign() {
		return mBlockAlign;
	}

	public short getBitsPerSample() {
		return mBitsPerSample;
	}

	public String getSubChunk2ID() {
		return mSubChunk2ID;
	}

	public int getSubChunk2Size() {
		return mSubChunk2Size;
	}

	public int getDataByteSize() {
		return getSubChunk2Size();
	}

	public int getDuration() {
		return (int) (1000 * 8.0 * getDataByteSize() / (getSampleRate() * getBitsPerSample() * getNumChannel()));
	}

	@Override
	public String toString() {
		return "WavFileHeader{" +
			"mChunkID='" + mChunkID + '\'' +
			", mChunkSize=" + mChunkSize +
			", mFormat='" + mFormat + '\'' +
			", mSubChunk1ID='" + mSubChunk1ID + '\'' +
			", mSubChunk1Size=" + mSubChunk1Size +
			", mAudioFormat=" + mAudioFormat +
			", mNumChannel=" + mNumChannel +
			", mSampleRate=" + mSampleRate +
			", mByteRate=" + mByteRate +
			", mBlockAlign=" + mBlockAlign +
			", mBitsPerSample=" + mBitsPerSample +
			", mSubChunk2ID='" + mSubChunk2ID + '\'' +
			", mSubChunk2Size=" + mSubChunk2Size +
			'}';
	}
}

