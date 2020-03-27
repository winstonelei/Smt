package com.github.fileUtils;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author winstone
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class Alarm{
    public String alarmIp;

    public String alarmType;

    public String alarmSourceIp;

    public String alarmDuration;

    public Long alarmBeginTime;

    public Long alarmEndTime;

    public Long maxAlarmTrafficRate;

    public String alarmId;

    public String getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    public String getAlarmIp() {
        return alarmIp;
    }

    public void setAlarmIp(String alarmIp) {
        this.alarmIp = alarmIp;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public String getAlarmSourceIp() {
        return alarmSourceIp;
    }

    public void setAlarmSourceIp(String alarmSourceIp) {
        this.alarmSourceIp = alarmSourceIp;
    }

    public String getAlarmDuration() {
        return alarmDuration;
    }

    public void setAlarmDuration(String alarmDuration) {
        this.alarmDuration = alarmDuration;
    }

    public Long getAlarmBeginTime() {
        return alarmBeginTime;
    }

    public void setAlarmBeginTime(Long alarmBeginTime) {
        this.alarmBeginTime = alarmBeginTime;
    }

    public Long getAlarmEndTime() {
        return alarmEndTime;
    }

    public void setAlarmEndTime(Long alarmEndTime) {
        this.alarmEndTime = alarmEndTime;
    }

    public Long getMaxAlarmTrafficRate() {
        return maxAlarmTrafficRate;
    }

    public void setMaxAlarmTrafficRate(Long maxAlarmTrafficRate) {
        this.maxAlarmTrafficRate = maxAlarmTrafficRate;
    }
}
