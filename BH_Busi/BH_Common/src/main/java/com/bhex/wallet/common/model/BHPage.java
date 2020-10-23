package com.bhex.wallet.common.model;

import java.util.List;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/4/10
 * Time: 15:44
 */
public class BHPage<T> {
    public int page;
    public int page_size;
    public int total;
    public List<T> items;
    public int unread;

}
