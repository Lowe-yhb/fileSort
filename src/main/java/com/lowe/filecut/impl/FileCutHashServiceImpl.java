package com.lowe.filecut.impl;

import com.google.common.base.Splitter;
import com.lowe.filecut.FileCutService;
import com.lowe.filecut.FileIndexService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created on 2022/2/10 22:05
 *
 * @author Lowe Yang
 * <p>
 * 文件切割服务实现类
 * 通过内容Hash值, 进行切割
 */
public class FileCutHashServiceImpl implements FileCutService {

    private FileIndexService fileIndexService = new FileIndexHashServiceImpl();

    @Override
    public List<File> fileCut(String filePath, String fileName, int fileCount) throws IOException {
        //获取所有的文件对象
        final List<File> fileList = this.getSubFileList(filePath, fileName, fileCount);

        final String fullFileName = filePath + fileName;
        try (LineIterator lineIterator = FileUtils.lineIterator(new File(fullFileName), "UTF-8");) {
            while (lineIterator.hasNext()) {
                //读取行数据
                final String line = lineIterator.nextLine();
                //按照格式分割, 数据格式为 ID,URL,DATE
                final List<String> dataList = Splitter.on(",").splitToList(line);
                //获取地址
                final String url = dataList.get(1);
                //获取文件下标
                final int index = fileIndexService.getIndex(url, fileCount);
                //获取对应的子文件
                final File subFile = fileList.get(index);
                //写入当前数据
                FileUtils.writeLines(subFile, "UTF-8", newArrayList(line), Boolean.TRUE);
            }
        }

        return fileList;
    }

    @Override
    public String getSubFileFullName(String filePath, String fileName, int index) {
        return filePath + fileName + "_" + index;
    }

    private List<File> getSubFileList(String filePath, String fileName, int fileCount) throws IOException {
        final List<File> fileList = newArrayList();
        for (int i = 0; i < fileCount; i++) {
            //获取文件全名
            final String subFileName = getSubFileFullName(filePath, fileName, i);
            final File subFile = new File(subFileName);
            //文件内容清空
            FileUtils.writeStringToFile(subFile, "", StandardCharsets.UTF_8, Boolean.FALSE);

            fileList.add(subFile);
        }

        return fileList;
    }
}
