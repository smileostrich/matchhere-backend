package com.mh.match.invite.service;

import com.mh.match.invite.dto.InviteLinkResponseDto;

public interface LinkService {

    InviteLinkResponseDto makeInviteLink(String category, Long projectId);
}
