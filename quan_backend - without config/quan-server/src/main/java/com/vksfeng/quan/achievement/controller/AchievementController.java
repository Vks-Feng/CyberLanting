package com.vksfeng.quan.achievement.controller;

import com.vksfeng.quan.achievement.pojo.dto.AchievementDTO;
import com.vksfeng.quan.achievement.pojo.vo.UserAchievementVO;
import com.vksfeng.quan.achievement.service.AchievementService;
import com.vksfeng.quan.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/achievement")
public class AchievementController {

    @Autowired
    private AchievementService achievementService;

    @Operation(summary = "获取用户成就")
    @GetMapping
    public Result<List<UserAchievementVO>> getUserAchievement() {
        return achievementService.getUserAchievement();
    }

    @Operation(summary = "插入成就（管理员接口）")
    @PostMapping("/admin/insert")
    public Result insertAchievement(@RequestBody AchievementDTO achievementDTO) {
        return achievementService.insertAchievement(achievementDTO);
    }
}
