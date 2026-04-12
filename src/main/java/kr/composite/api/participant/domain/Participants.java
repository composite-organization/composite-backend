package kr.composite.api.participant.domain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Participants {

    private final List<Participant> participants;

    public Participants(List<Participant> participants) {
        this.participants = List.copyOf(participants);
    }

    Map<Long, Participant> indexById() {
        return participants.stream()
                .collect(Collectors.toMap(Participant::getId, participant -> participant));
    }
}