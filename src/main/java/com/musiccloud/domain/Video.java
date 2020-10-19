package com.musiccloud.domain;

import javax.persistence.*;

@Entity
public class Video extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String image;
    private int playCount;

    @Column(name = "video_no")
    private String videoNo;
}
