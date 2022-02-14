package com.lowe.filecut.impl;

import com.lowe.filecut.FileIndexService;

import java.util.Objects;

/**
 * Created on 2022/2/10 22:05
 *
 * @author Lowe Yang
 * <p>
 * 文件切割服务实现类
 * 通过内容Hash值, 进行切割
 */
public class FileIndexHashServiceImpl implements FileIndexService {

    @Override
    public int getIndex(String url, int fileCount) {
        //1. 根据url取hash值
        final int hash = Objects.hashCode(url);
        //2. 根据文件个数取模
        return (hash % fileCount + fileCount) % fileCount;
    }


}
