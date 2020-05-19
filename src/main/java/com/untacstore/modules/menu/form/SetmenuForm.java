package com.untacstore.modules.menu.form;

import com.untacstore.modules.menu.Menu;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
public class SetmenuForm {
    @NotBlank
    private String title;

    private List<Menu> menuList = new ArrayList<>();

    private Integer totalPrice = 0;

    private String explanation;
}
