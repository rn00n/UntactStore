package com.untacstore.modules.store.form;

import com.untacstore.modules.account.Account;
import lombok.Data;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

@Data
public class StoreSettingsForm {
    @NotBlank
    private String licensee;

}
