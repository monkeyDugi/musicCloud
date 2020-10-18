package com.musiccloud.domain;

import javax.persistence.*;

@Entity
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name = "video_no")
    private String videoNo;
    private String image;
    private int count; // 재생 횟수
}
