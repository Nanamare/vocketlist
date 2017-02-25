package com.vocketlist.android.dto;

import java.io.Serializable;

/**
 * 데이터 : 페이징
 *
 * @author Jungho Song (dev@threeword.com)
 * @since 2017. 2. 25.
 */
public class Paging implements Serializable {
    private int page_size;
    private int page_current;
    private int page_count;
    private int count;
}
