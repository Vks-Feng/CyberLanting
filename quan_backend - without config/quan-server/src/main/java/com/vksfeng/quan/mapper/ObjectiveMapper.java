package com.vksfeng.quan.mapper;

import com.vksfeng.quan.objectivehub_pojo.entity.Objective;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ObjectiveMapper {
    void insert(Objective objective);

    Objective selectById(Long objectiveId);

    void updateById(Objective objective, Long objectiveId);

    void deleteById(Long objectiveId);

    List<Long> getAllSubObjectivesId(Long objectiveId);

    void deleteRelationByChildId(Long objectiveId);

    void deleteRelationByFatherId(Long objectiveId);

    List<Long> getAllObjectivesId(Long userId);

    List<Objective> getAllMainObjectives(List<Long> mainObjectivesIds);

    void createObjectiveRelation(Long parentObjectiveId, Long childObjectiveId);

    void deleteObjectiveRelation(Long parentObjectiveId, Long childObjectiveId);

    boolean isSubObjective(Long objectiveId);

    Integer getObjectiveCount(Long userId);

    Integer getCompletedObjectiveCount(Long userId);

    void updateProgress(Double progress, Long objectiveId);

    void addAiGuideForObjective(Long objectiveId, String aiGuide);

    String getAiGuideForObjective(Long objectiveId);

    void deleteAiGuideForObjective(Long objectiveId);

    void completeObjectiveById(LocalDateTime now, Long objectiveId);

    Long getFatherObjectiveIdBySubObjectiveId(Long objectiveId);
}
