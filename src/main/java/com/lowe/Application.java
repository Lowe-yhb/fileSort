package com.lowe;

import com.lowe.filecut.FileCutService;
import com.lowe.filecut.impl.FileCutHashServiceImpl;
import com.lowe.filehandle.FileHandleService;
import com.lowe.filehandle.impl.FileHandleServiceImpl;
import com.lowe.model.PageData;
import com.lowe.model.ResultData;
import com.lowe.model.UrlCount;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created on 2022/2/12 13:39
 *
 * @author Lowe Yang
 * 主测试类
 */
public class Application {
    /*
        代码部分提供文件处理以及规则排序功能, 其他部分功能暂未编码
        todo
        需求: 客户希望可以通过部署多个服务提高文件处理能力；
              同一个文件不可以被处理多次；
              程序可能因为一些无法预料的情况导致崩溃，失败后文件可以由其他机器重新处理；

        解决方案:
              1. 通过redis分布式锁来保证同一个文件不可被重复处理
              2. 在redis中记录文件处理的状态, 处理中/处理完成
              3. redis中记录处理中状态, 添加失效时间, 如果状态失效被redis移除, 那么这个文件可被再次处理
                 失效时间需要根据实际数据量, 服务器性能进行评估
     */

    /*
    文件处理方案:
             1. 由于内存限制, 不能将文件一次性读取, 需要进行拆分
    规则排序方案
             1. 将子文件按规则排序, 最后汇总再次进行排序
             2. 排序采用堆排序进行处理 - 优先级队列方式
     */
    public static void main(String[] args) throws Exception {

        //1. 文件切割, 将10G的文件切割成100个小文件
        final FileCutService fileCutService = new FileCutHashServiceImpl();
        List<File> subFileList = fileCutService.fileCut("D:\\home\\", "test.txt", 100);
        //2. 从每个小文件中分析需要的数据
        List<ResultData> resultDataList = newArrayList();
        final FileHandleService fileHandleService = new FileHandleServiceImpl();
        for (File file : subFileList) {
            final ResultData analysisResult = fileHandleService.getAnalysisResult(file);
            resultDataList.add(analysisResult);
        }
        //3. 所有的结果归, 再次排序
        ResultData resultData = fileHandleService.mergeResultData(resultDataList);

        //4输出结果
        System.out.println("按浏览量从高到低排序的前 20 个页面。");
        for (int i = resultData.getTop20Pages().size() - 1; i >= 0; i--) {
            UrlCount urlCount = resultData.getTop20Pages().get(i);
            System.out.println(urlCount.getUrl() + ":访问次数" + urlCount.getCount());
        }

        System.out.println("===================================");

        System.out.println("Top20 每个页面的前 20 个用户。");
        for (int i = resultData.getTop20Pages().size() - 1; i >= 0; i--) {
            UrlCount urlCount = resultData.getTop20Pages().get(i);
            List<String> users = resultData.getTop20PagesTopUsers().get(urlCount.getUrl())
                    .stream().map(PageData::getId).collect(Collectors.toList());
            System.out.println(urlCount.getUrl() + ":前20个用户" + users);
        }

        System.out.println("===================================");

        //c. 按浏览量从低到高排序的前 20 个页面。
        for (int i = resultData.getBottom20Pages().size() - 1; i >= 0; i--) {
            UrlCount urlCount = resultData.getBottom20Pages().get(i);
            System.out.println(urlCount.getUrl() + ":访问次数" + urlCount.getCount());
        }
    }
}
