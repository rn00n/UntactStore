package com.untacstore.modules.store.form;

import lombok.Data;

import javax.persistence.Basic;
import javax.persistence.FetchType;
import javax.persistence.Lob;

@Data
public class StoreProfileForm {
//    private String path;
// 상점설정으로 이동
//    private String name;

    //TODO 영업
    private String operatingTime;
    
    private String address;

    private String phone;

    private String shortDescription;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String fullDescription;
}
