package com.github.fileUtils;

import java.io.Serializable;


/**
 *
 * @Description: Iprange 
 *
 * @author bean
 *
 */
public class IPrange implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 6424986590164289602L;
    private long start;
    private long end;
    private String province;
    private String supplier;


    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }



    public long getStart() {
        return start;
    }
    public void setStart(long start) {
        this.start = start;
    }
    public long getEnd() {
        return end;
    }
    public void setEnd(long end) {
        this.end = end;
    }
}
