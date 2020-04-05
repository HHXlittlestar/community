package com.huhx.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

//分页查询的时候服务端向网页回传的信息
@Data
public class PageInfoDTO<T> {

    private boolean showNext;//当前页面是否含有下一页的按钮
    private boolean showEndPage;//当前页面是否含有直达最后一页的按钮
    private boolean showPre;//当前页面是否含有上一页的按钮
    private boolean showFirstPage;//当前页面是否含有直达第一页的按钮
    private int totalCount;//查询出来的总的记录条数
    private int totalPage;//计算出来的总的页数
    private int page;//当前的页码（用于前端高亮页码使用）
    private List<Integer> pages;//用于存放当前页需要显示的页码
    private List<T> datas;//用于存放查找到的内容


    public void setPageInfo(int totalCount, int page, int size) {
        this.totalCount = totalCount;
        this.page = page;
        this.totalPage = (totalCount % size == 0) ? (totalCount / size) : (totalCount / size + 1);
        //当前页码前面要显示3页，超过3页则只显示3页，后面也只显示3页
        this.pages = new ArrayList<>();
        if (page <= 4){
            if (totalPage < page + 3){
                for (int i = 1; i <= totalPage; i++) {
                    pages.add(i);
                }
            }else {
                for (int i = 1; i <= page + 3; i++) {
                    pages.add(i);
                }
            }
        }else{
            if (totalPage < page + 3){
                for (int i = 1; i <= totalPage; i++) {
                    pages.add(i);
                }
            }else {
                for (int i = page - 3; i <= page + 3; i++) {
                    pages.add(i);
                }
            }
        }
        this.showFirstPage = !pages.contains(1);
        this.showEndPage = !pages.contains(totalPage);
        this.showNext = (page < totalPage);
        this.showPre = (page > 1);
    }
}
