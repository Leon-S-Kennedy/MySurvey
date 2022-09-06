package com.libowen.diaocha.data_object;

import lombok.Data;

@Data
public class SidToRefCountDo {
    public Integer sid;
    public Integer refCount;

    public SidToRefCountDo(int sid, int ref_count) {
        this.sid=sid;
        this.refCount=ref_count;

    }

}
