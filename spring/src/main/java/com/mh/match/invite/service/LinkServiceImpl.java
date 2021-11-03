package com.mh.match.invite.service;

import com.mh.match.invite.dto.InviteLinkResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
@RequiredArgsConstructor
public class LinkServiceImpl implements LinkService{

    @Transactional
    public InviteLinkResponseDto makeInviteLink(String category, Long id){
        return InviteLinkResponseDto.from(ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/" + category + "/")
            .path(id.toString())
            .toUriString());
    }

}
