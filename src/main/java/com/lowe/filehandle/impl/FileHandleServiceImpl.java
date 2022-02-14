package com.lowe.filehandle.impl;

import com.google.common.base.Splitter;
import com.lowe.filehandle.FileHandleService;
import com.lowe.model.PageData;
import com.lowe.model.ResultData;
import com.lowe.model.UrlCount;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

/**
 * Created on 2022/2/10 22:02
 *
 * @author Lowe Yang
 */
public class FileHandleServiceImpl implements FileHandleService {

    @Override
    public ResultData getAnalysisResult(File file) throws Exception {

        List<PageData> pageDataList = newArrayList();
        //读取所有数据
        try (LineIterator lineIterator = FileUtils.lineIterator(file, "UTF-8");) {
            while (lineIterator.hasNext()) {
                //读取行数据
                final String line = lineIterator.nextLine();
                //按照格式分割, 数据格式为 ID,URL,DATE
                final List<String> dataList = Splitter.on(",").splitToList(line);
                //组装数据
                final PageData pageData = new PageData(dataList.get(0), dataList.get(1), dataList.get(2));
                pageDataList.add(pageData);
            }
        }

        //分组计算 key为url, value为url对应的行数据
        Map<String, List<PageData>> urlGroup = pageDataList.stream().collect(Collectors.groupingBy(PageData::getUrl));
        //计算url出现的次数
        List<UrlCount> urlCounts = newArrayList();
        for (Map.Entry<String, List<PageData>> entry : urlGroup.entrySet()) {
            urlCounts.add(new UrlCount(entry.getKey(), entry.getValue().size()));
        }


        return dataSort(urlCounts, urlGroup);
    }

    /**
     * 分组排序
     *
     * @param urlCountList url出现的次数
     * @param urlGroup     key为url, value为url对应的行数据
     * @return
     */
    private ResultData dataSort(List<UrlCount> urlCountList, Map<String, List<PageData>> urlGroup) {

        //使用优先队列来处理排序问题
        //队列中保存出现次数最多（map中value最大）的20组键值对
        PriorityQueue<UrlCount> topQueue = new PriorityQueue<>((o1, o2) -> o1.getCount() - o2.getCount());
        ///队列中保存出现次数最少（map中value最大）的20组键值对
        PriorityQueue<UrlCount> bottomQueue = new PriorityQueue<>((o1, o2) -> o2.getCount() - o1.getCount());


        for (UrlCount urlCount : urlCountList) {
            if (topQueue.size() < 20) {
                topQueue.add(urlCount);
            } else if (urlCount.getCount().compareTo(topQueue.peek().getCount()) > 0) {
                topQueue.poll();
                topQueue.add(urlCount);
            }

            if (bottomQueue.size() < 20) {
                bottomQueue.add(urlCount);
            } else if (urlCount.getCount().compareTo(bottomQueue.peek().getCount()) < 0) {
                bottomQueue.poll();
                bottomQueue.add(urlCount);
            }
        }


        //topQueue就是 次数最多的前20
        final List<UrlCount> top20Pages = newArrayList();
        while (topQueue.peek() != null) {
            top20Pages.add(topQueue.poll());
        }

        //bottomQueue就是 次数最少的前20
        final List<UrlCount> bottom20Pages = newArrayList();
        while (bottomQueue.peek() != null) {
            bottom20Pages.add(bottomQueue.poll());
        }

        //获取top20, 每个页面的前20用户
        final Map<String, List<PageData>> top20PagesTopUsers = newHashMap();

        for (UrlCount top20Page : top20Pages) {
            //获取页面对应的数据
            final String url = top20Page.getUrl();
            List<PageData> pageDatas = urlGroup.get(url);
            //排序获取最早的20个用户
            List<PageData> dateSort = pageDatas.stream()
                    .sorted(Comparator.comparing(PageData::getDate))
                    .collect(Collectors.toList());

            top20PagesTopUsers.put(url, dateSort);
        }


        final ResultData resultData = new ResultData();
        resultData.setTop20Pages(top20Pages);
        resultData.setTop20PagesTopUsers(top20PagesTopUsers);
        resultData.setBottom20Pages(bottom20Pages);


        return resultData;
    }

    @Override
    public ResultData mergeResultData(List<ResultData> resultDataList) throws Exception {
        Map<String, List<PageData>> urlGroup = newHashMap();
        List<UrlCount> urlCounts = newArrayList();

        for (ResultData resultData : resultDataList) {
            urlCounts.addAll(resultData.getTop20Pages());
            urlCounts.addAll(resultData.getBottom20Pages());

            urlGroup.putAll(resultData.getTop20PagesTopUsers());
        }

        return dataSort(urlCounts, urlGroup);
    }
}
