package com.matrix.disruptor;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.fasterxml.jackson.databind.ser.std.ByteBufferSerializer;
import com.lmax.disruptor.EventHandler;
import com.matrix.util.FileUtil;
import com.matrix.util.JsonUtil;
import com.matrix.util.ReuseByteBuffer;

public class CommandEventHandler implements EventHandler<CommandEvent> {
	private AggregateRootRepository aggregateRootRepository;

	private String snapshotFileBasePath = "./snapshot";

	private String jFileBasePath = ".";

	private JournalFile jFile;

	private ReuseByteBuffer reuseByteBuffer;

	public CommandEventHandler(AggregateRootRepository aggregateRootRepository) {
		this.aggregateRootRepository = aggregateRootRepository;
	}

	public CommandEventHandler(AggregateRootRepository aggregateRootRepository, String snapshotFileBasePath,
			String jFileBasePath) {
		this.aggregateRootRepository = aggregateRootRepository;
		this.snapshotFileBasePath = snapshotFileBasePath;
		this.jFileBasePath = jFileBasePath;
	}

	@Override
	public void onEvent(CommandEvent event, long sequence, boolean endOfBatch) throws Exception {
		if (jFile == null) {
			String recentFileName = FileUtil.getRecentFileName(jFileBasePath);
			if (recentFileName == null || recentFileName.equals("")) {
				recentFileName = jFileBasePath + "/" + System.currentTimeMillis();
			}
			jFile = new JournalFile(recentFileName);
			reuseByteBuffer = new ReuseByteBuffer(ByteBuffer.allocateDirect(1024 * 1024));
		}
		if (!event.isSnapshot()) {
			Command cmd = event.getCmd();
			DeferredResult result = event.getResult();
			Function function = event.getFunction();
			if (function != null) {
				try {
					Object v = function.run();
					recordJournalFile(cmd, v);
					result.setResult(v);
				} catch (Exception e) {
					result.setException(e);
				}
				return;
			}
			Process process = event.getProcess();
			if (process != null) {
				try {
					process.run();
					recordJournalFile(cmd, null);
					result.setResult(null);
				} catch (Exception e) {
					result.setException(e);
				}
				return;
			}
		} else {
			try {
				saveSnapshot();
				jFile.close();
				jFile = new JournalFile(jFileBasePath + "/" + System.currentTimeMillis());
			} catch (Throwable e) {
				System.exit(0);// 任何失败系统停机。
			}
		}
	}

	private void recordJournalFile(Command cmd, Object result) {
		ByteBuffer bb = reuseByteBuffer.take();
		try {
			if (cmdResult != null) {
				CRPair pair = new CRPair(cmd, result);
				ByteBufferSerializer.objToByteBuffer(pair, bb);
			} else {
				ByteBufferSerializer.objToByteBuffer(cmd, bb);
			}
			jFile.write(bb);
		} catch (Throwable e) {
			System.exit(0);// 任何失败系统停机。
		}
	}

	private void saveSnapshot() throws IOException {
		Snapshot snapshoot = coreSnapshotFactory.createSnapshoot();
		JsonUtil.saveObjToJsonFile(fileBasePath, snapshoot.getCreateTime() + "", snapshoot);
	}

}
