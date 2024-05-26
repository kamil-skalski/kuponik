package pl.kuponik.model;

import lombok.Getter;


@Getter
public enum NominalValue {
    TEN(100),
    TWENTY(200),
    FIFTY(500);

    private final int requiredPoints;

    NominalValue(int requiredPoints) {
        this.requiredPoints = requiredPoints;
    }
}
