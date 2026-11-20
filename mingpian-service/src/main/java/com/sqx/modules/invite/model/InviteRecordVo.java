package com.sqx.modules.invite.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sqx.modules.invite.entity.InviteRecord;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author chengming
 * @since 2026/1/13
 */
@Data
public class InviteRecordVo extends InviteRecord {

    private String userName;

    private String avatar;

    private String phone;

    private Long cardId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime inviteTime;
}
