/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tophey.common;

import java.io.Serializable;
import java.util.List;
import com.tophey.common.QueryResult;

/**
 *
 * @author xiang.fu
 */
public class PageResult<T extends Serializable> implements Serializable {
    private static final long serialVersionUID = 5403232797920251068L;
    private List<T> pageList;
    private int curPage;
    private int totalCount;
    private int pageSize;
    private int currentSize;

    public PageResult(int currentpage, int pagesize) {
        this.curPage = currentpage;
        this.pageSize = pagesize;
    }
    
    public PageResult(int totalCount, int curPage, int pageSize,
            List<T> pageList) {
        this.pageList = pageList;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.curPage = curPage;
    }

    public List<T> getPageList() {
        return pageList;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getCurPage() {
        return curPage;
    }

    public int getPageCount() {
        if (pageSize == 0 || totalCount == 0) {
            return 1;
        } else {
            return ((totalCount - 1) / pageSize) + 1;
        }
    }

    public int getFromIndex() {
        return (curPage - 1) * pageSize;
    }

    public static int recalculateCurPage(int totalCount, int curPage,
            int pageSize) {
        if (totalCount <= (curPage - 1) * pageSize) {
            if (totalCount == 0) {
                return 1;
            } else {
                return ((totalCount - 1) / pageSize) + 1;
            }
        } else {
            return curPage;
        }
    }

    /**
     * 获取开始索引
     *
     * @param curPage 起始页码是1
     * @param pageSize 页数大于0
     * @return
     */
    public static int getFromIndex(int curPage, int pageSize) {
        return (curPage - 1) * pageSize;
    }
    
    public int getFirstResult() {
        return PageResult.getFromIndex(this.curPage, this.pageSize);
    }
    
    public void setQueryResult(QueryResult qr) {
        this.pageList = qr.getResultlist();
        this.totalCount = qr.getRecordtotal();
        
    }
    
    public int getCurrentSize() {
        return pageList.size();
    }
}
