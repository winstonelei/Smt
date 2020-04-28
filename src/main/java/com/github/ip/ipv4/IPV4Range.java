package com.github.ip.ipv4;

import java.io.Serializable;


public class IPV4Range implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 6424986590164289602L;
    private long start;
    private long end;
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
