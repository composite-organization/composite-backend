package kr.composite.api.vote.ui;

import jakarta.validation.Valid;
import java.net.URI;
import kr.composite.api.user.domain.User;
import kr.composite.api.user.ui.requestuser.RequestUser;
import kr.composite.api.vote.application.VoteWidgetService;
import kr.composite.api.vote.domain.VoteStatus;
import kr.composite.api.vote.ui.apiSpec.VoteWidgetApiSpec;
import kr.composite.api.vote.ui.dto.request.PostVoteSubmissionRequest;
import kr.composite.api.vote.ui.dto.request.PostVoteWidgetRequest;
import kr.composite.api.vote.ui.dto.request.UpdateVoteStatusRequest;
import kr.composite.api.vote.ui.dto.response.CreateVoteWidgetResponse;
import kr.composite.api.vote.ui.dto.response.GetVoteWidgetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VoteWidgetController implements VoteWidgetApiSpec {

    private final VoteWidgetService voteWidgetService;

    @Override
    @PostMapping("/vote-widgets")
    public ResponseEntity<CreateVoteWidgetResponse> createVoteWidget(
            @RequestUser User user,
            @Valid @RequestBody PostVoteWidgetRequest request
    ) {
        CreateVoteWidgetResponse response = voteWidgetService.addVoteWidget(user, request);

        return ResponseEntity.created(URI.create("/vote-widgets/" + response.id())).body(response);
    }

    @Override
    @GetMapping("/vote-widgets/{voteWidgetId}")
    public ResponseEntity<GetVoteWidgetResponse> readVoteWidget(
            @RequestUser User user,
            @PathVariable("voteWidgetId") Long voteWidgetId
    ) {
        GetVoteWidgetResponse response = voteWidgetService.findVoteWidget(user, voteWidgetId);

        return ResponseEntity.ok().body(response);
    }

    @Override
    @PatchMapping("/vote-widgets/{voteWidgetId}/status")
    public ResponseEntity<Void> updateVoteStatus(
            @RequestUser User user,
            @PathVariable("voteWidgetId") Long voteWidgetId,
            @Valid @RequestBody UpdateVoteStatusRequest request
    ) {
        voteWidgetService.updateVoteStatus(user, voteWidgetId, VoteStatus.from(request.status()));

        return ResponseEntity.ok().build();
    }

    @Override
    @PostMapping("/vote-widgets/{voteWidgetId}/submissions")
    public ResponseEntity<Void> createVoteSubmission(
            @RequestUser User user,
            @PathVariable("voteWidgetId") Long voteWidgetId,
            @Valid @RequestBody PostVoteSubmissionRequest request
    ) {
        voteWidgetService.submitVote(user, voteWidgetId, request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @DeleteMapping("/vote-widgets/{voteWidgetId}")
    public ResponseEntity<Void> deleteVoteWidget(
            @RequestUser User user,
            @PathVariable("voteWidgetId") Long voteWidgetId
    ) {
        voteWidgetService.removeVoteWidget(user, voteWidgetId);

        return ResponseEntity.noContent().build();
    }
}
