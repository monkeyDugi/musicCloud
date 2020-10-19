package com.musiccloud.domain;

import javax.persistence.*;

@Entity
public class PlayList extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int playCount;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "video_id")
    private Video video;
}
