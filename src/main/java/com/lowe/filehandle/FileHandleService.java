package com.lowe.filehandle;

import com.lowe.model.ResultData;

import java.io.File;
import java.util.List;

/**
 * Created on 2022/2/10 22:02
 *
 * @author Lowe Yang
 */
public interface FileHandleService {

    /**
     * 1. 按浏览量从高到低排序的前 20 个页面。
     * 2. Top20 每个页面的前 20 个用户。
     * 3. 按浏览量从低到高排序的前 20 个页面。
     */
    ResultData getAnalysisResult(File file) throws Exception;

    /**
     * 每个子文件的排序结果汇总后 重新排序,
     * 再次计算如下结果
     * <p>
     * 1. 按浏览量从高到低排序的前 20 个页面。
     * 2. Top20 每个页面的前 20 个用户。
     * 3. 按浏览量从低到高排序的前 20 个页面。
     */
    ResultData mergeResultData(List<ResultData> resultDataList) throws Exception;

}
