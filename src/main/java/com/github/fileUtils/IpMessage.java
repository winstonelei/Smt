package com.github.fileUtils;

import java.io.Serializable;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author winstone
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class IpMessage implements Serializable {

    private String beginIp;

    private String endIp;

    private String location;

    public String getBeginIp() {
        return beginIp;
    }

    public void setBeginIp(String beginIp) {
        this.beginIp = beginIp;
    }

    public String getEndIp() {
        return endIp;
    }

    public void setEndIp(String endIp) {
        this.endIp = endIp;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
