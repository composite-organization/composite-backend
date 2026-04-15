package kr.composite.api.vote.infrastructure;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import kr.composite.api.vote.domain.VoteStatus;

@Converter(autoApply = true)
public class VoteStatusConverter implements AttributeConverter<VoteStatus, String> {

    @Override
    public String convertToDatabaseColumn(VoteStatus voteStatus) {
        if (voteStatus == null) {
            return null;
        }

        return voteStatus.getDescription();
    }

    @Override
    public VoteStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        return VoteStatus.fromDescription(dbData);
    }
}
