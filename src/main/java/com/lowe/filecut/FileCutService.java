package com.lowe.filecut;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created on 2022/2/10 22:02
 *
 * @author Lowe Yang
 * <p>
 * 文件切割服务类
 * 提供文件切割相关方法
 */
public interface FileCutService {

    /**
     * 对指定文件进行切割处理
     *
     * @param filePath  源文件路径
     * @param fileName  源文件名称
     * @param fileCount 文件数量
     * @return 返回切割后的文件名称集合
     */
    List<File> fileCut(String filePath, String fileName, int fileCount) throws IOException;

    /**
     * 获取子文件全称
     *
     * @param filePath 源文件路径
     * @param fileName 源文件名称
     * @param index    文件下标
     * @return 返回子文件全称
     */
    String getSubFileFullName(String filePath, String fileName, int index);
}
