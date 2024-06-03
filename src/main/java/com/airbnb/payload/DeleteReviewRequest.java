package com.airbnb.payload;

import com.airbnb.entity.PropertyUser;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteReviewRequest {
    private long reviewId;
}
