package com.lowe.filecut;

/**
 * Created on 2022/2/10 22:02
 *
 * @author Lowe Yang
 * <p>
 * 文件切割服务类
 * 提供文件分割下标生成的方法
 */
public interface FileIndexService {

    /**
     * 获取url地址对应的文件标
     *
     * @param url       文件内容中url
     * @param fileCount 文件切割个数
     * @return 对应的文件下标
     */
    int getIndex(String url, int fileCount);
}
