package org.example.vanphong.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "campaignImage")
@Data
public class CampaignImage {
    @Id
    @Column(name = "image_id")
    private Integer imageId;

    @Column(name = "image_path")
    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "campaign_id")
    private MarketingCampaign marketingCampaign;
}

