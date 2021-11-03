//package com.ssafy.match.invite.entity;
//
//import java.time.LocalDateTime;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.Table;
//import lombok.AccessLevel;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@Getter
//@Builder
//@Entity
//@Table(name = "link")
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@AllArgsConstructor
//public class Link {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "decode_uri")
//    private String decode_Uri;
//
//    @Column(name = "encode_uri")
//    private String encode_Uri;
//
//    @Column(name = "expire_date")
//    private LocalDateTime expireDate;
//
//
//}
