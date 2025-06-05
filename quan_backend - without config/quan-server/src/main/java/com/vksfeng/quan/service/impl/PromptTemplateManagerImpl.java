package com.vksfeng.quan.service.impl;

import com.vksfeng.quan.constant.RedisConstant;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;


import java.nio.file.*;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class PromptTemplateManagerImpl {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String REDIS_KEY_PREFIX = RedisConstant.PROMPT_TEMPLATE_KEY + ":";

    @Value("classpath:prompts")
    private Resource promptsDirectory;

    // 文件夹路径
    private Path promptsPath;

    @PostConstruct
    public void loadPromptTemplates() {
        try {
            File folder = promptsDirectory.getFile();
            promptsPath = folder.toPath();

            // 先加载现有模板
            loadTemplatesFromFolder(promptsPath);

            // 启动监听器监控文件变化
            startFileWatchService();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load prompt templates", e);
        }
    }

    private void loadTemplatesFromFolder(Path folder) throws IOException {
        File[] files = folder.toFile().listFiles((dir, name) -> name.endsWith(".txt") || name.endsWith(".md"));
        if (files != null) {
            for (File file : files) {
                String code = file.getName().substring(0, file.getName().lastIndexOf('.'));
                String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
                redisTemplate.opsForValue().set(REDIS_KEY_PREFIX + code, content);
            }
        }
    }

    // 文件监听方法
    private void startFileWatchService() {
        Thread watchThread = new Thread(() -> {
            try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
                // 注册文件夹，监听修改和创建事件
                promptsPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);

                while (true) {
                    WatchKey key;
                    try {
                        key = watchService.poll(10, TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        break;
                    }

                    if (key == null) continue;

                    for (WatchEvent<?> event : key.pollEvents()) {
                        if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE || event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                            String filename = event.context().toString();
                            if (filename.endsWith(".txt") || filename.endsWith(".md")) {
                                // 文件修改或新增时更新缓存
                                String code = filename.substring(0, filename.lastIndexOf('.'));
                                File file = new File(promptsPath.toFile(), filename);
                                String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
                                redisTemplate.opsForValue().set(REDIS_KEY_PREFIX + code, content);
                            }
                        }
                    }

                    // 重置密钥
                    boolean valid = key.reset();
                    if (!valid) {
                        break;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to start file watch service", e);
            }
        });

        watchThread.setDaemon(true);
        watchThread.start();
    }

    public String getContent(String code) {
        return redisTemplate.opsForValue().get(REDIS_KEY_PREFIX + code);
    }

    public String render(String code, Map<String, String> variables) {
        String template = getContent(code);
        if (template == null) return null;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            // 使用正则表达式来替换所有的占位符
            template = template.replaceAll("\\{" + entry.getKey() + "\\}", entry.getValue());
        }
        return template;
    }
}

