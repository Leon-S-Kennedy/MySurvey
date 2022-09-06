package com.libowen.diaocha.data_object;

import lombok.Data;

@Data
public class QidToRefCountDo {
    public Integer qid;
    public Integer refCount;

    public QidToRefCountDo(int qid, int ref_count) {
        this.qid=qid;
        this.refCount=ref_count;

    }
}
