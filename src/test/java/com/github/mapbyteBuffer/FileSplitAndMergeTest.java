package com.github.mapbyteBuffer;

import com.github.filesplitmerge.FileMergeAndSplitUtil;

public class FileSplitAndMergeTest {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
/*		    System.out.println(FileMergeAndSplitUtil.currentWorkDir);

	        StringBuilder sb = new StringBuilder();

	        long originFileSize = 1024 * 1024 * 2;// 100M
	        int blockFileSize = 1024 * 1024 * 10;// 15M

	        // 生成一个大文件
	        for (int i = 0; i < originFileSize; i++) {
	            sb.append("A");
	        }

	        String fileName = FileMergeAndSplitUtil.currentWorkDir + "origin.myfile";
	        System.out.println(fileName);
	        System.out.println(FileMergeAndSplitUtil.write(fileName, sb.toString()));

	        // 追加内容
	        sb.setLength(0);
	        sb.append("0123456789");
	        FileMergeAndSplitUtil.append(fileName, sb.toString());*/
		    FileMergeAndSplitUtil.currentWorkDir="F:\\tmp\\";
		    String fileName = "F:\\tmp\\ppp.wmv";

		    int blockFileSize = 1024 * 1024 * 10;// 15M

	        FileMergeAndSplitUtil FileMergeAndSplitUtil = new FileMergeAndSplitUtil();

	        // 将origin.myfile拆分
	        FileMergeAndSplitUtil.splitBySize(fileName, blockFileSize);

	        Thread.sleep(1000);// 稍等10秒，等前面的小文件全都写完

	        // 合并成新文件
	        FileMergeAndSplitUtil.mergePartFiles(FileMergeAndSplitUtil.currentWorkDir, ".wmv",
	                blockFileSize, FileMergeAndSplitUtil.currentWorkDir + "ttta.wmv");
	}

}
