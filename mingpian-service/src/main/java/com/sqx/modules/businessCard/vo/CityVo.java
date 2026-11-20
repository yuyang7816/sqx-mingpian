package com.sqx.modules.businessCard.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CityVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String city;

    private List<CityVo> cityVoList;
}
