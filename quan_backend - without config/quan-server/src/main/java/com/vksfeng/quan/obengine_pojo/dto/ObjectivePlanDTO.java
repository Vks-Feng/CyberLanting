package com.vksfeng.quan.obengine_pojo.dto;

import com.vksfeng.quan.objectivehub_pojo.entity.Objective;
import com.vksfeng.quan.objectivehub_pojo.vo.ObjectiveVO;
import lombok.Data;

import java.util.List;

@Data
public class ObjectivePlanDTO {
    private Objective masterObjective;
    private List<SubObjectiveDTO> subObjectives;
}
