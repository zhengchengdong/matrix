package com.matrix.dddsupport.recovery.commandreplay;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 记录日志的文件。顺序写是其特点。
 *
 * @author zheng chengdong
 */
public class JournalFile implements Closeable {

	private FileChannel fc;

	private File file;

	private RandomAccessFile rFile;

	private int writtenCount = 0;

	/**
	 * 如果文件不存在，创建文件。如果已存在，则到文件最末尾，准备写。
	 *
	 * @param name
	 *            完整文件名
	 * @throws Exception
	 */
	public JournalFile(String name) throws Exception {
		file = new File(name);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		rFile = new RandomAccessFile(file, "rw");
		fc = rFile.getChannel();
		fc.position(fc.size());
	}

	public void write(ByteBuffer bb) throws Exception {
		fc.write(bb);
		writtenCount++;
	}

	/**
	 * 文件所属目录
	 *
	 * @return
	 */
	public String getDirectory() {
		return file.getParentFile().getPath();
	}

	public int getWrittenCount() {
		return writtenCount;
	}

	@Override
	public void close() throws IOException {
		fc.close();
		rFile.close();
	}

}
