package com.zzyl.aigent.config;

import com.zzyl.aigent.tool.NursingProjectTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringAIConfig {

    /**
     * 创建并返回一个ChatClient的Spring Bean实例。
     * @param openAiChatModel
     * @return
     */
    @Bean//让框架自动调用此方法
    public ChatClient chatClient(OpenAiChatModel openAiChatModel, NursingProjectTool nursingProjectTool) {
        return ChatClient
                .builder(openAiChatModel)
                .defaultTools(nursingProjectTool)
                .defaultSystem("您是一家名为“中州养老”的职业养老机构的客户聊天助手，你的名字叫小智。请以友好、乐于助人和愉快的方式解答用户的各种问题。\n" +
                        "            如果需要查询护理项目时，要调用find_nursingproject_tool工具 ")
                .build();
    }
}