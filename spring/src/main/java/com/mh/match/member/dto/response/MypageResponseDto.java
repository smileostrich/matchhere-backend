package com.mh.match.member.dto.response;

import com.mh.match.member.entity.Member;
import com.mh.match.member.entity.MemberSns;
import com.mh.match.s3.dto.DBFileDto;
import com.mh.match.s3.entity.DBFile;
import com.mh.match.common.dto.DetailPositionInterface;
import com.mh.match.member.dto.inter.CareerInterface;
import com.mh.match.member.dto.inter.CertificationInterface;
import com.mh.match.member.dto.inter.EducationInterface;
import com.mh.match.member.dto.inter.MemberTechstackInterface;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
public class MypageResponseDto {

    @ApiModelProperty(name = "email", example = "my_email@gmail.com")
    private String email;

    @ApiModelProperty(name = "name", example = "문일민")
    private String name;

    @ApiModelProperty(name = "cover_pic", example = "http://cdn.matchhere.me/path/coverpic.png")
    private DBFileDto cover_pic;

    @ApiModelProperty(name = "bio", example = "let me introduce")
    private String bio;

    @ApiModelProperty(name = "nickname", example = "별명")
    private String nickname;

    @ApiModelProperty(name = "city", example = "부산")
    private String city;

    @ApiModelProperty(name = "position", example = "개발자")
    private String position;

    @ApiModelProperty(name = "techList", example = "[{\"name\":\"python\", \"level\":\"상\", \"img_uri\":\"http://cdn.matchhere.me/path/python.png\"}, {\"name\":\"java\", \"level\":\"중\", \"img_uri\":\"http://cdn.matchhere.me/path/java.png\"}]")
    private List<MemberTechstackInterface> techList = new ArrayList<>();

    @ApiModelProperty(name = "dpositionList", example = "[{\"name\":\"프론트엔드\"}, {\"name\":\"데브옵스\"}]")
    private List<DetailPositionInterface> dpositionList = new ArrayList<>();

    private List<CareerInterface> careerList = new ArrayList<>();
    private List<CertificationInterface> certificationList = new ArrayList<>();

    @ApiModelProperty(name = "educationList", example = "[{\"id\": 2, \"state\": \"졸업\", \"description\": \"서울대학교가 낳은 최고의 인재\", \"institution\": \"서울대학교\", \"major\": \"컴퓨터과학과\", \"end_date\": \"2020-12-31\",\"start_date\": \"2020-12-31\", \"degree\": \"학사\" }]")
    private List<EducationInterface> educationList = new ArrayList<>();

    @ApiModelProperty(name = "portfolio", example = "http://cdn.matchhere.me/path/portfolio.pdf")
    private DBFileDto portfolio;

    @ApiModelProperty(name = "portfolio_uri", example = "http://cdn.matchhere.me/path/myportfolio.pdf")
    private String portfolio_uri;

    @ApiModelProperty(name = "snsList", example = "[{\"id\":1, \"snsName\":\"github\", \"snsAccount\":\"gitid\"},{\"id\":2, \"snsName\":\"twitter\", \"snsAccount\":\"twitterid\"}]")
    private List<MemberSns> snsList = new ArrayList<>();

    public static MypageResponseDto of(Member member, List<CareerInterface> careers, List<EducationInterface> educations, List<CertificationInterface> certifications, List<MemberTechstackInterface> techList, List<MemberSns> snsList, List<DetailPositionInterface> dpositionList) {
        return MypageResponseDto.builder()
                .email(member.getEmail())
                .name(member.getName())
                .cover_pic(DBFileDto.of(member.getCover_pic()))
                .bio(member.getBio())
                .nickname(member.getNickname())
                .city(member.getCity())
                .position(member.getPosition())
                .techList(techList)
                .dpositionList(dpositionList)
                .careerList(careers)
                .certificationList(certifications)
                .educationList(educations)
                .portfolio(DBFileDto.of(member.getPortfolio()))
                .portfolio_uri(member.getPortfolio_uri())
                .snsList(snsList)
                .build();
    }

    @Builder
    public MypageResponseDto(String email, String name, DBFileDto cover_pic, String bio, String nickname, String city, String position, List<MemberTechstackInterface> techList, List<DetailPositionInterface> dpositionList, List<CareerInterface> careerList, List<CertificationInterface> certificationList, List<EducationInterface> educationList, DBFileDto portfolio, String portfolio_uri, List<MemberSns> snsList) {
        this.email = email;
        this.name = name;
        this.cover_pic = cover_pic;
        this.bio = bio;
        this.nickname = nickname;
        this.city = city;
        this.position = position;
        this.techList = techList;
        this.dpositionList = dpositionList;
        this.careerList = careerList;
        this.certificationList = certificationList;
        this.educationList = educationList;
        this.portfolio = portfolio;
        this.portfolio_uri = portfolio_uri;
        this.snsList = snsList;
    }
}