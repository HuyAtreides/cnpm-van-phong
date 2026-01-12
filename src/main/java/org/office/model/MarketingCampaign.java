package org.office.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "marketingCampaign")
@Data
public class MarketingCampaign {
    @Id
    @Column(name = "campaign_id")
    private Integer campaignId;

    private String content;
    
    @Column(name = "is_delete")
    private Integer isDelete;

    @ManyToOne
    @JoinColumn(name = "voucher_id")
    private Voucher voucher;
    
    @OneToMany(mappedBy = "marketingCampaign", cascade = CascadeType.ALL)
    private List<CampaignImage> images;
}

